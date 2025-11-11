package ru.practicum.collector.handler.hub;

import org.springframework.stereotype.Component;
import ru.practicum.collector.configuration.EventPublisher;
import ru.yandex.practicum.grpc.telemetry.event.DeviceRemovedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.time.Instant;

@Component
public class DeviceRemovedEvent extends BaseHubEventHandler {

    public DeviceRemovedEvent(EventPublisher publisher) {
        super(publisher);
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.DEVICE_REMOVED;
    }

    @Override
    public void handle(HubEventProto event) {
        var avro = mapToAvro(event);
        publisher.send(null, avro);
    }

    @Override
    protected HubEventAvro mapToAvro(HubEventProto event) {
        DeviceRemovedEventProto e = event.getDeviceRemoved();
        var payload = DeviceRemovedEventAvro.newBuilder()
                .setId(e.getId())
                .build();
        return HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(Instant.ofEpochSecond(event.getTimestamp().getSeconds(), event.getTimestamp().getNanos()))
                .setPayload(payload)
                .build();
    }
}
