package ru.practicum.collector.handler.sensor;

import org.springframework.stereotype.Component;
import ru.practicum.collector.configuration.EventPublisher;
import ru.practicum.collector.model.sensor.SensorEvent;
import ru.practicum.collector.model.sensor.SensorEventType;
import ru.practicum.collector.model.sensor.TemperatureSensorEvent;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

import java.util.Map;

@Component
public class TemperatureSensorEventHandler extends BaseSensorEventHandler {

    protected TemperatureSensorEventHandler(EventPublisher publisher) {
        super(publisher);
    }

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.TEMPERATURE_SENSOR_EVENT;
    }

    @Override
    public void handle(SensorEvent event) {
        var avro = mapToAvro(event);
        publisher.sendToSensors(event.getId(), avro, Map.of(
                "event-type", event.getType().toString(),
                "schema", avro.getSchema().getFullName(),
                "traceId", java.util.UUID.randomUUID().toString()
        ));
    }

    @Override
    protected SensorEventAvro mapToAvro(SensorEvent event) {
        TemperatureSensorEvent e = (TemperatureSensorEvent) event;
        var payload = TemperatureSensorAvro.newBuilder()
                .setTemperatureC(e.getTemperatureC())
                .setTemperatureF(e.getTemperatureF())
                .build();
        return SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();
    }
}
