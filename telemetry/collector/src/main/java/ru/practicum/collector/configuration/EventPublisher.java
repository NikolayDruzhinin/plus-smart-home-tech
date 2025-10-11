package ru.practicum.collector.configuration;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.practicum.kafka.serializer.GeneralAvroSerializer;

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

    public CompletableFuture<Void> sendToSensors(String key, SpecificRecordBase recordBase, Map<String, String> headers) {
        String topic = properties.topics().sensors();
        byte[] payload = generalAvroSerializer.serialize(topic, recordBase);
        return send(topic, key, payload, headers);
    }

    public CompletableFuture<Void> sendToHubs(String key, SpecificRecordBase recordBase, Map<String, String> headers) {
        String topic = properties.topics().hubs();
        byte[] payload = generalAvroSerializer.serialize(topic, recordBase);
        return send(topic, key, payload, headers);
    }

    public CompletableFuture<Void> sendToSnapshots(String key, SpecificRecordBase recordBase, Map<String, String> headers) {
        String topic = properties.topics().snapshots();
        byte[] payload = generalAvroSerializer.serialize(topic, recordBase);
        return send(topic, key, payload, headers);
    }

    private CompletableFuture<Void> send(String topic, String key, byte[] payload, Map<String, String> headers) {
        var record = new ProducerRecord<>(topic, key, payload);
        if (headers != null) {
            headers.forEach((k, v) ->
                    record.headers().add(new RecordHeader(k, v.getBytes(StandardCharsets.UTF_8))));
        }
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
