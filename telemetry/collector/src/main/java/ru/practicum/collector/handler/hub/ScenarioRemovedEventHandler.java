package ru.practicum.collector.handler.hub;

import org.springframework.stereotype.Component;
import ru.practicum.collector.configuration.EventPublisher;
import ru.practicum.collector.model.hub.HubEvent;
import ru.practicum.collector.model.hub.HubEventType;
import ru.practicum.collector.model.hub.ScenarioRemovedEvent;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

import java.util.Map;

@Component
public class ScenarioRemovedEventHandler extends BaseHubEventHandler {

    public ScenarioRemovedEventHandler(EventPublisher publisher) {
        super(publisher);
    }

    @Override
    public HubEventType getMessageType() {
        return HubEventType.SCENARIO_REMOVED;
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
        ScenarioRemovedEvent e = (ScenarioRemovedEvent) event;
        var payload = ScenarioRemovedEventAvro.newBuilder()
                .setName(e.getName())
                .build();
        return HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();
    }
}
