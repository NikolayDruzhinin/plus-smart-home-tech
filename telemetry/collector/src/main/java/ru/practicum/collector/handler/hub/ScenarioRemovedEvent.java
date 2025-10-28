package ru.practicum.collector.handler.hub;

import org.springframework.stereotype.Component;
import ru.practicum.collector.configuration.EventPublisher;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioRemovedEventProto;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

import java.time.Instant;

@Component
public class ScenarioRemovedEvent extends BaseHubEventHandler {

    public ScenarioRemovedEvent(EventPublisher publisher) {
        super(publisher);
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.SCENARIO_REMOVED;
    }

    @Override
    public void handle(HubEventProto event) {
        var avro = mapToAvro(event);
        publisher.send(null, avro);
    }

    @Override
    protected HubEventAvro mapToAvro(HubEventProto event) {
        ScenarioRemovedEventProto e = event.getScenarioRemoved();
        var payload = ScenarioRemovedEventAvro.newBuilder()
                .setName(e.getName())
                .build();
        return HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(Instant.ofEpochSecond(event.getTimestamp().getSeconds(), event.getTimestamp().getNanos()))
                .setPayload(payload)
                .build();
    }
}
