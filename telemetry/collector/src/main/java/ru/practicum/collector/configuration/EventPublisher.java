package ru.practicum.collector.configuration;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.practicum.kafka.serializer.GeneralAvroSerializer;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
@AllArgsConstructor
@Slf4j
public class EventPublisher {
    private final KafkaTemplate<String, byte[]> template;
    private final AppKafkaProperties properties;
    private final GeneralAvroSerializer generalAvroSerializer;

    public CompletableFuture<Void> send(String key, SpecificRecordBase recordBase) {
        String topic;
        String eventType;
        if (recordBase.getSchema().equals(HubEventAvro.getClassSchema())) {
            topic = properties.topics().hubs();
            eventType = "hub-event";
        } else if (recordBase.getSchema().equals(SensorEventAvro.getClassSchema())) {
            topic = properties.topics().sensors();
            eventType = "sensor-event";
        } else {
            throw new RuntimeException("Unsupported schema " + recordBase.getSchema().toString());
        }

        Map<String, String> headers = Map.of(
                "event-type", eventType,
                "schema", recordBase.getSchema().getFullName(),
                "traceId", java.util.UUID.randomUUID().toString()
        );
        byte[] payload = generalAvroSerializer.serialize(topic, recordBase);

        var record = new ProducerRecord<>(topic, key, payload);
        headers.forEach((k, v) ->
                record.headers().add(new RecordHeader(k, v.getBytes(StandardCharsets.UTF_8))));

        return template.send(record)
                .thenAccept(res -> {
                    log.info("Sent {} bytes in topic [{}]", payload.length, topic);
                })
                .exceptionally(ex -> {
                    log.error("Error sending event in topic [{}]: {}", topic, ex.getMessage());
                    return null;
                });
    }
}
