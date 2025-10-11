package ru.practicum.collector.handler.sensor;

import org.springframework.stereotype.Component;
import ru.practicum.collector.configuration.EventPublisher;
import ru.practicum.collector.model.sensor.ClimateSensorEvent;
import ru.practicum.collector.model.sensor.SensorEvent;
import ru.practicum.collector.model.sensor.SensorEventType;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.util.Map;

@Component
public class ClimateSensorEventHandler extends BaseSensorEventHandler {

    protected ClimateSensorEventHandler(EventPublisher publisher) {
        super(publisher);
    }

    @Override
    protected SensorEventAvro mapToAvro(SensorEvent event) {
        ClimateSensorEvent e = (ClimateSensorEvent) event;
        var payload = ClimateSensorAvro.newBuilder()
                .setCo2Level(e.getCo2Level())
                .setHumidity(e.getHumidity())
                .setTemperatureC(e.getTemperatureC())
                .build();
        return SensorEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setId(event.getId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();
    }

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.CLIMATE_SENSOR_EVENT;
    }

    @Override
    public void handle(SensorEvent event) {
        var avro = mapToAvro(event);
        publisher.sendToSensors(null, avro, Map.of(
                "event-type", event.getType().toString(),
                "schema", avro.getSchema().getFullName(),
                "traceId", java.util.UUID.randomUUID().toString()
        ));
    }
}
