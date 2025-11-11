package ru.practicum.collector.handler.hub;

import org.springframework.stereotype.Component;
import ru.practicum.collector.configuration.EventPublisher;
import ru.yandex.practicum.grpc.telemetry.event.DeviceAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.time.Instant;

@Component
public class DeviceAddedEvent extends BaseHubEventHandler {
    public DeviceAddedEvent(EventPublisher publisher) {
        super(publisher);
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.DEVICE_ADDED;
    }

    @Override
    public void handle(HubEventProto event) {
        HubEventAvro avro = mapToAvro(event);
        publisher.send(null, avro);
    }

    @Override
    protected HubEventAvro mapToAvro(HubEventProto event) {
        DeviceAddedEventProto e = event.getDeviceAdded();
        var payload = DeviceAddedEventAvro.newBuilder()
                .setId(e.getId())
                .setType(DeviceTypeAvro.valueOf(String.valueOf(e.getType())))
                .build();

        return HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(Instant.ofEpochSecond(event.getTimestamp().getSeconds(), event.getTimestamp().getNanos()))
                .setPayload(payload)
                .build();
    }
}
