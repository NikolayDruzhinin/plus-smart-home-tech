package ru.practicum.collector.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "collector.kafka")
public record AppKafkaProperties(Topics topics) {
    public record Topics(String hubs, String sensors, String snapshots) {
    }
}
