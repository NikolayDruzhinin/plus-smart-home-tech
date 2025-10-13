package ru.practicum.collector.handler.hub;

import org.springframework.stereotype.Component;
import ru.practicum.collector.configuration.EventPublisher;
import ru.practicum.collector.model.hub.HubEvent;
import ru.practicum.collector.model.hub.HubEventType;
import ru.practicum.collector.model.hub.ScenarioAddedEvent;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.List;

@Component
public class ScenarioAddedEventHandler extends BaseHubEventHandler {

    public ScenarioAddedEventHandler(EventPublisher publisher) {
        super(publisher);
    }

    @Override
    public HubEventType getMessageType() {
        return HubEventType.SCENARIO_ADDED;
    }

    @Override
    public void handle(HubEvent event) {
        var avro = mapToAvro(event);
        publisher.send(null, avro);
    }

    @Override
    protected HubEventAvro mapToAvro(HubEvent event) {
        ScenarioAddedEvent e = (ScenarioAddedEvent) event;
        List<DeviceActionAvro> actions = e.getActions().stream()
                .map(action -> DeviceActionAvro.newBuilder()
                        .setType(ActionTypeAvro
                                .valueOf(String.valueOf(action.type())))
                        .setSensorId(action.sensorId())
                        .setValue(action.value() == null ? 0 : action.value())
                        .build())
                .toList();

        List<ScenarioConditionAvro> conditions = e.getConditions().stream()
                .map(cond -> ScenarioConditionAvro.newBuilder()
                        .setType(ConditionTypeAvro
                                .valueOf(String.valueOf(cond.type())))
                        .setValue(cond.value())
                        .setOperation(ConditionOperationAvro
                                .valueOf(String.valueOf(cond.operation())))
                        .setSensorId(cond.sensorId())
                        .build())
                .toList();

        var payload = ScenarioAddedEventAvro.newBuilder()
                .setActions(actions)
                .setName(e.getName())
                .setConditions(conditions)
                .build();

        return HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();
    }
}
