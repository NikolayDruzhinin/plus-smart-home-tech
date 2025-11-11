package ru.practicum.collector.handler.sensor;

import org.springframework.stereotype.Component;
import ru.practicum.collector.configuration.EventPublisher;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SwitchSensorProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;

import java.time.Instant;

@Component
public class SwitchSensorEventHandler extends BaseSensorEventHandler {

    protected SwitchSensorEventHandler(EventPublisher publisher) {
        super(publisher);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.SWITCH_SENSOR_PROTO;
    }

    @Override
    public void handle(SensorEventProto event) {
        var avro = mapToAvro(event);
        publisher.send(event.getId(), avro);
    }

    @Override
    protected SensorEventAvro mapToAvro(SensorEventProto event) {
        SwitchSensorProto e = event.getSwitchSensorProto();
        var payload = SwitchSensorAvro.newBuilder()
                .setState(e.getState())
                .build();
        return SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(Instant.ofEpochSecond(event.getTimestamp().getSeconds(), event.getTimestamp().getNanos()))
                .setPayload(payload)
                .build();
    }
}
