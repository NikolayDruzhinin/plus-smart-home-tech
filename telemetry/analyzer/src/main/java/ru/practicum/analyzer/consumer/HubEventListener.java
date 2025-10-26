package ru.practicum.analyzer.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.practicum.analyzer.model.*;
import ru.practicum.analyzer.repository.ScenarioRepository;
import ru.practicum.analyzer.repository.SensorRepository;
import ru.yandex.practicum.kafka.telemetry.event.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class HubEventListener {
    private final SensorRepository sensorRepository;
    private final ScenarioRepository scenarioRepository;


    @KafkaListener(
            topics = "#{@hubsTopics}",
            containerFactory = "hubsKafkaListenerContainerFactory"
    )
    private void listen(HubEventAvro event) {
        String hubId = event.getHubId();
        Object payload = event.getPayload();

        if (payload instanceof DeviceAddedEventAvro deviceAdded) {
            Sensor sensor = new Sensor();
            sensor.setId(deviceAdded.getId());
            sensor.setHubId(hubId);
            sensorRepository.save(sensor);
            log.info("Sensor {} added in hub {}", deviceAdded.getId(), hubId);

        } else if (payload instanceof DeviceRemovedEventAvro deviceRemoved) {
            sensorRepository.deleteById(deviceRemoved.getId());
            log.info("Sensor deleted {}", deviceRemoved.getId());

        } else if (payload instanceof ScenarioAddedEventAvro scenarioAdded) {
            Scenario scenario = new Scenario();
            scenario.setHubId(hubId);
            scenario.setName(scenarioAdded.getName());

            scenarioAdded.getConditions().forEach(conditionAvro -> {
                String sensorId = conditionAvro.getSensorId();

                sensorRepository.findById(sensorId).orElseGet(() -> {
                    Sensor s = new Sensor();
                    s.setId(sensorId);
                    s.setHubId(hubId);
                    return sensorRepository.save(s);
                });

                Object rawValue = conditionAvro.getValue();

                Condition condition = new Condition();
                condition.setType(ConditionType.valueOf(conditionAvro.getType().name()));
                condition.setOperation(conditionAvro.getOperation().name());

                if (rawValue instanceof Integer i) {
                    condition.setValueInt(i);
                } else if (rawValue instanceof Boolean b) {
                    condition.setValueBool(b);
                } else {
                    log.warn("Unknown value type: {}", rawValue != null ? rawValue.getClass().getSimpleName() : "null");
                }

                scenario.getConditions().put(sensorId, condition);
            });

            scenarioAdded.getActions().forEach(actionAvro -> {
                String sensorId = actionAvro.getSensorId();

                sensorRepository.findById(sensorId).orElseGet(() -> {
                    Sensor s = new Sensor();
                    s.setId(sensorId);
                    s.setHubId(hubId);
                    return sensorRepository.save(s);
                });

                Action action = new Action();
                action.setType(actionAvro.getType().name());

                if (actionAvro.getValue() instanceof Integer i) {
                    action.setValue(i);
                }

                scenario.getActions().put(sensorId, action);
            });

            scenarioRepository.save(scenario);

            log.info("Scenario added '{}', conditions: {}, actions: {}",
                    scenarioAdded.getName(), scenarioAdded.getConditions().size(), scenarioAdded.getActions().size());
        } else if (payload instanceof ScenarioRemovedEventAvro scenarioRemoved) {
            scenarioRepository.findByHubIdAndName(hubId, scenarioRemoved.getName())
                    .ifPresentOrElse(
                            scenarioRepository::delete,
                            () -> log.warn("Scenario for removing not found '{}'", scenarioRemoved.getName())
                    );

        } else {
            log.warn("Unknown event type: {}", payload.getClass().getSimpleName());
        }
    }
}
