package ru.practicum.analyzer.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.practicum.analyzer.service.ScenarioEvaluationService;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class SnapshotProcessor {
    private final ScenarioEvaluationService scenarioService;

    @KafkaListener(
            topics = "#{@snapshotsTopics}",
            containerFactory = "snapshotsKafkaListenerContainerFactory"
    )
    public void listen(SensorsSnapshotAvro snapshot) {
        scenarioService.evaluateAndExecute(snapshot);
    }
}
