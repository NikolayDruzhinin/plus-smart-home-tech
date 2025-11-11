package ru.practicum.collector.handler.sensor;

import org.springframework.stereotype.Component;
import ru.practicum.collector.configuration.EventPublisher;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.TemperatureSensorProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

import java.time.Instant;

@Component
public class TemperatureSensorEventHandler extends BaseSensorEventHandler {

    protected TemperatureSensorEventHandler(EventPublisher publisher) {
        super(publisher);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.TEMPERATURE_SENSOR_PROTO;
    }

    @Override
    public void handle(SensorEventProto event) {
        var avro = mapToAvro(event);
        publisher.send(event.getId(), avro);
    }

    @Override
    protected SensorEventAvro mapToAvro(SensorEventProto event) {
        TemperatureSensorProto e = event.getTemperatureSensorProto();
        var payload = TemperatureSensorAvro.newBuilder()
                .setTemperatureC(e.getTemperatureC())
                .setTemperatureF(e.getTemperatureF())
                .build();
        return SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(Instant.ofEpochSecond(event.getTimestamp().getSeconds(), event.getTimestamp().getNanos()))
                .setPayload(payload)
                .build();
    }
}
