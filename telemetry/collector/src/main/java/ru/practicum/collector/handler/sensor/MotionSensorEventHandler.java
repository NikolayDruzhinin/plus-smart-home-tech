package ru.practicum.collector.handler.sensor;

import org.springframework.stereotype.Component;
import ru.practicum.collector.configuration.EventPublisher;
import ru.yandex.practicum.grpc.telemetry.event.MotionSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.time.Instant;

@Component
public class MotionSensorEventHandler extends BaseSensorEventHandler {

    protected MotionSensorEventHandler(EventPublisher publisher) {
        super(publisher);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.MOTION_SENSOR_PROTO;
    }

    @Override
    public void handle(SensorEventProto event) {
        var avro = mapToAvro(event);
        publisher.send(event.getId(), avro);
    }

    @Override
    protected SensorEventAvro mapToAvro(SensorEventProto event) {
        MotionSensorProto e = event.getMotionSensorProto();
        var payload = MotionSensorAvro.newBuilder()
                .setLinkQuality(e.getLinkQuality())
                .setMotion(e.getMotion())
                .setVoltage(e.getVoltage())
                .build();
        return SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(Instant.ofEpochSecond(event.getTimestamp().getSeconds(), event.getTimestamp().getNanos()))
                .setPayload(payload)
                .build();

    }
}
