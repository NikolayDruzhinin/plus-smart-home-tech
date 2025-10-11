package ru.practicum.collector.handler.sensor;

import org.springframework.stereotype.Component;
import ru.practicum.collector.configuration.EventPublisher;
import ru.practicum.collector.model.sensor.SensorEvent;
import ru.practicum.collector.model.sensor.SensorEventType;
import ru.practicum.collector.model.sensor.SwitchSensorEvent;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;

import java.util.Map;

@Component
public class SwitchSensorEventHandler extends BaseSensorEventHandler {

    protected SwitchSensorEventHandler(EventPublisher publisher) {
        super(publisher);
    }

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.SWITCH_SENSOR_EVENT;
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
        SwitchSensorEvent e = (SwitchSensorEvent) event;
        var payload = SwitchSensorAvro.newBuilder()
                .setState(e.getState())
                .build();
        return SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();
    }
}
