package ru.practicum.collector.handler.sensor;

import org.springframework.stereotype.Component;
import ru.practicum.collector.configuration.EventPublisher;
import ru.yandex.practicum.grpc.telemetry.event.ClimateSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.time.Instant;

@Component
public class ClimateSensorEventHandler extends BaseSensorEventHandler {

    protected ClimateSensorEventHandler(EventPublisher publisher) {
        super(publisher);
    }

    @Override
    protected SensorEventAvro mapToAvro(SensorEventProto event) {
        ClimateSensorProto e = event.getClimateSensorProto();
        var payload = ClimateSensorAvro.newBuilder()
                .setCo2Level(e.getCo2Level())
                .setHumidity(e.getHumidity())
                .setTemperatureC(e.getTemperatureC())
                .build();
        return SensorEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setId(event.getId())
                .setTimestamp(Instant.ofEpochSecond(event.getTimestamp().getSeconds(), event.getTimestamp().getNanos()))
                .setPayload(payload)
                .build();
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.CLIMATE_SENSOR_PROTO;
    }

    @Override
    public void handle(SensorEventProto event) {
        var avro = mapToAvro(event);
        publisher.send(null, avro);
    }
}
