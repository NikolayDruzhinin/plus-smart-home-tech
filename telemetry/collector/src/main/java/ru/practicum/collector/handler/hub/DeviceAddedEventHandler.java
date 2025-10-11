package ru.practicum.collector.handler.hub;

import org.springframework.stereotype.Component;
import ru.practicum.collector.configuration.EventPublisher;
import ru.practicum.collector.model.hub.DeviceAddedEvent;
import ru.practicum.collector.model.hub.HubEvent;
import ru.practicum.collector.model.hub.HubEventType;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.util.Map;

@Component
public class DeviceAddedEventHandler extends BaseHubEventHandler {
    public DeviceAddedEventHandler(EventPublisher publisher) {
        super(publisher);
    }

    @Override
    public HubEventType getMessageType() {
        return HubEventType.DEVICE_ADDED;
    }

    @Override
    public void handle(HubEvent event) {
        HubEventAvro avro = mapToAvro(event);
        publisher.sendToHubs(null, avro, Map.of(
                "event-type", event.getType().toString(),
                "schema", avro.getSchema().getFullName(),
                "traceId", java.util.UUID.randomUUID().toString()
        ));
    }

    @Override
    protected HubEventAvro mapToAvro(HubEvent event) {
        DeviceAddedEvent e = (DeviceAddedEvent) event;
        var payload = DeviceAddedEventAvro.newBuilder()
                .setId(e.getId())
                .setType(DeviceTypeAvro.valueOf(String.valueOf(e.getDeviceType())))
                .build();

        return HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();
    }
}
