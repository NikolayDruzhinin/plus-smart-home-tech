package ru.practicum.aggregator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.practicum.aggregator.configuration.EventPublisher;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

/**
 * Класс AggregationStarter, ответственный за запуск агрегации данных.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AggregationStarter {
    private final SnapshotService snapshotService;
    private final EventPublisher eventPublisher;

    @KafkaListener(topics = "${aggregator.kafka.topics.sensors}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(SensorEventAvro event) {
        log.info("Listen event {}", event);
        snapshotService.updateState(event)
                .ifPresent(sensorsSnapshotAvro -> {
                    log.info("Send snapshot {}", sensorsSnapshotAvro);
                    eventPublisher.send(sensorsSnapshotAvro.getHubId(), sensorsSnapshotAvro);
                });
    }
}
