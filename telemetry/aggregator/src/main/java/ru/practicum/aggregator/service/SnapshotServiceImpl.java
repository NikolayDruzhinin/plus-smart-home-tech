package ru.practicum.aggregator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class SnapshotServiceImpl implements SnapshotService {
    // храним актуальные снапшоты по hubId
    private final Map<String, SensorsSnapshotAvro> sensors = new ConcurrentHashMap<>();

    @Override
    public Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event) {
        // берём текущий снапшот для hubId (или создаём пустой)
        SensorsSnapshotAvro current = sensors.computeIfAbsent(
                event.getHubId(),
                hubId -> SensorsSnapshotAvro.newBuilder()
                        .setHubId(hubId)
                        .setTimestamp(event.getTimestamp())
                        .setSensorsState(new HashMap<>())
                        .build()
        );

        // делаем копию карты состояний
        Map<String, SensorStateAvro> sensorState = new HashMap<>(current.getSensorsState());

        SensorStateAvro prev = sensorState.get(event.getId());
        if (prev != null) {
            // если старое событие «новее» — игнорируем
            if (prev.getTimestamp().isAfter(event.getTimestamp())) {
                return Optional.empty();
            }
            // если полезная нагрузка не изменилась и timestamp не продвинулся — ничего не публикуем
            if (prev.getTimestamp().equals(event.getTimestamp())
                    && prev.getData().equals(event.getPayload())) {
                return Optional.empty();
            }
        }

        SensorStateAvro newState = SensorStateAvro.newBuilder()
                .setTimestamp(event.getTimestamp())
                .setData(event.getPayload())
                .build();
        sensorState.put(event.getId(), newState);

        SensorsSnapshotAvro updated = SensorsSnapshotAvro.newBuilder(current)
                .setHubId(event.getHubId())
                .setSensorsState(sensorState)
                .setTimestamp(event.getTimestamp()) // общий timestamp снапшота — «последний виденный»
                .build();

        sensors.put(event.getHubId(), updated);

        return Optional.of(updated);
    }
}
