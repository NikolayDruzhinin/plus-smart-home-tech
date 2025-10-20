package ru.practicum.collector.handler.hub;

import org.springframework.stereotype.Component;
import ru.practicum.collector.configuration.EventPublisher;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioConditionProto;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.time.Instant;
import java.util.List;

@Component
public class ScenarioAddedEvent extends BaseHubEventHandler {

    public ScenarioAddedEvent(EventPublisher publisher) {
        super(publisher);
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.SCENARIO_ADDED;
    }

    @Override
    public void handle(HubEventProto event) {
        var avro = mapToAvro(event);
        publisher.send(null, avro);
    }

    @Override
    protected HubEventAvro mapToAvro(HubEventProto event) {
        ScenarioAddedEventProto e = event.getScenarioAdded();
        List<DeviceActionAvro> actions = e.getActionList().stream()
                .map(action -> DeviceActionAvro.newBuilder()
                        .setType(ActionTypeAvro.valueOf(String.valueOf(action.getType())))
                        .setSensorId(action.getSensorId())
                        .setValue(action.getValue())
                        .build())
                .toList();

        List<ScenarioConditionAvro> conditions = e.getConditionList().stream()
                .map(cond -> ScenarioConditionAvro.newBuilder()
                        .setType(ConditionTypeAvro.valueOf(String.valueOf(cond.getType())))
                        .setValue(cond.getValueCase() == ScenarioConditionProto.ValueCase.BOOL_VALUE ?
                                cond.getBoolValue() : cond.getIntValue())
                        .setOperation(ConditionOperationAvro.valueOf(String.valueOf(cond.getOperation())))
                        .setSensorId(cond.getSensorId())
                        .build())
                .toList();

        var payload = ScenarioAddedEventAvro.newBuilder()
                .setActions(actions)
                .setName(e.getName())
                .setConditions(conditions)
                .build();

        return HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(Instant.ofEpochSecond(event.getTimestamp().getSeconds(), event.getTimestamp().getNanos()))
                .setPayload(payload)
                .build();
    }
}
