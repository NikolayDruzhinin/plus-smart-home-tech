package ru.practicum.collector.handler.sensor;

import org.springframework.stereotype.Component;
import ru.practicum.collector.configuration.EventPublisher;
import ru.practicum.collector.model.sensor.MotionSensorEvent;
import ru.practicum.collector.model.sensor.SensorEvent;
import ru.practicum.collector.model.sensor.SensorEventType;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.util.Map;

@Component
public class MotionSensorEventHandler extends BaseSensorEventHandler {

    protected MotionSensorEventHandler(EventPublisher publisher) {
        super(publisher);
    }

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.MOTION_SENSOR_EVENT;
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
        MotionSensorEvent e = (MotionSensorEvent) event;
        var payload = MotionSensorAvro.newBuilder()
                .setLinkQuality(e.getLinkQuality())
                .setMotion(e.getMotion())
                .setVoltage(e.getVoltage())
                .build();
        return SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();

    }
}
