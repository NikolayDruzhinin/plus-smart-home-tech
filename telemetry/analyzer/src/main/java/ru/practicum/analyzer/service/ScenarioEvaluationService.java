package ru.practicum.analyzer.service;

import com.google.protobuf.Timestamp;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.analyzer.mapper.DeviceActionRequestMapper;
import ru.practicum.analyzer.model.Condition;
import ru.practicum.analyzer.model.Scenario;
import ru.practicum.analyzer.repository.ScenarioRepository;
import ru.practicum.analyzer.service.grpc.HubRouterClient;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScenarioEvaluationService {

    private final ScenarioRepository scenarioRepository;
    private final HubRouterClient hubRouterClient;

    @Transactional
    public void evaluateAndExecute(SensorsSnapshotAvro snapshot) {
        String hubId = snapshot.getHubId();
        Map<String, SensorStateAvro> states = snapshot.getSensorsState();

        List<Scenario> scenarios = scenarioRepository.findByHubId(hubId);
        if (scenarios.isEmpty()) {
            log.info("No scenarios for hub {}", hubId);
            return;
        }

        log.info("Get {} scenarios for hub {}", scenarios.size(), hubId);
        for (Scenario scenario : scenarios) {
            log.info("Check scenario '{}'", scenario.getName());
            scenario.getConditions().forEach((sensorId, condition) -> {
                SensorStateAvro state = states.get(sensorId);
                if (state == null) {
                    log.warn("No sensor id {} in snapshot", sensorId);
                } else {
                    log.info("Sensor condition {}: type = {}", sensorId, state.getData().getClass().getSimpleName());
                }
            });
        }

        for (Scenario scenario : scenarios) {
            boolean matched = scenario.getConditions().entrySet().stream().allMatch(entry -> {
                String sensorId = entry.getKey();
                Condition condition = entry.getValue();
                SensorStateAvro state = states.get(sensorId);
                if (state == null) return false;
                return evaluateCondition(condition, state);
            });

            if (matched) {
                log.info("Scenario '{}' activated", scenario.getName());
                scenario.getActions().forEach((sensorId, action) -> {
                    DeviceActionRequest request = DeviceActionRequestMapper.map(scenario, hubId, sensorId, action);
                    hubRouterClient.sendAction(request);
                });
            }
        }
    }

    private boolean evaluateCondition(Condition condition, SensorStateAvro state) {
        Integer actual = extractValueFromSensor(condition, state);
        Integer expected = condition.getValueInt();

        log.info("Check condition: actual = {}, expected = {}, operation = {}", actual, expected, condition.getOperation());

        if (actual == null || expected == null) {
            log.warn("Can't compare: actual или expected = null");
            return false;
        }

        return switch (condition.getOperation()) {
            case "EQUALS" -> actual.equals(expected);
            case "GREATER_THAN" -> actual > expected;
            case "LOWER_THAN" -> actual < expected;
            default -> {
                log.warn("Unknown operation: {}", condition.getOperation());
                yield false;
            }
        };
    }

    private Integer extractValueFromSensor(Condition condition, SensorStateAvro state) {
        Object data = state.getData();
        switch (data.getClass().getSimpleName()) {
            case "MotionSensorAvro":
                return ((MotionSensorAvro) data).getMotion() ? 1 : 0;
            case "TemperatureSensorAvro":
                return ((TemperatureSensorAvro) data).getTemperatureC();
            case "LightSensorAvro":
                return ((LightSensorAvro) data).getLuminosity();
            case "SwitchSensorAvro":
                return ((SwitchSensorAvro) data).getState() ? 1 : 0;
            case "ClimateSensorAvro":
                ClimateSensorAvro sensor = (ClimateSensorAvro) data;
                return switch (condition.getType()) {
                    case TEMPERATURE -> sensor.getTemperatureC();
                    case HUMIDITY -> sensor.getHumidity();
                    case CO2LEVEL -> sensor.getCo2Level();
                    default -> {
                        log.warn("Unknown condition type for ClimateSensorAvro: {}", condition.getType());
                        yield null;
                    }
                };
            default:
                log.warn("Unknown sensor type: {}", data.getClass().getSimpleName());
                return null;
        }
    }

    private Timestamp toProtoTimestamp(long millis) {
        Instant instant = Instant.ofEpochMilli(millis);
        return Timestamp.newBuilder()
                .setSeconds(instant.getEpochSecond())
                .setNanos(instant.getNano())
                .build();
    }
}
