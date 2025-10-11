package ru.practicum.collector.handler.hub;

import org.springframework.stereotype.Component;
import ru.practicum.collector.configuration.EventPublisher;
import ru.practicum.collector.model.hub.DeviceRemovedEvent;
import ru.practicum.collector.model.hub.HubEvent;
import ru.practicum.collector.model.hub.HubEventType;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.util.Map;

@Component
public class DeviceRemovedEventHandler extends BaseHubEventHandler {

    public DeviceRemovedEventHandler(EventPublisher publisher) {
        super(publisher);
    }

    @Override
    public HubEventType getMessageType() {
        return HubEventType.DEVICE_REMOVED;
    }

    @Override
    public void handle(HubEvent event) {
        var avro = mapToAvro(event);
        publisher.sendToHubs(null, avro, Map.of(
                "event-type", event.getType().toString(),
                "schema", avro.getSchema().getFullName(),
                "traceId", java.util.UUID.randomUUID().toString()
        ));
    }

    @Override
    protected HubEventAvro mapToAvro(HubEvent event) {
        DeviceRemovedEvent e = (DeviceRemovedEvent) event;
        var payload = DeviceRemovedEventAvro.newBuilder()
                .setId(e.getId())
                .build();
        return HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();
    }
}
