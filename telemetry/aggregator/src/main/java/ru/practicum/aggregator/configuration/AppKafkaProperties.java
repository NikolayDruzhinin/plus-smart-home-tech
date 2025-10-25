package ru.practicum.aggregator.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.kafka")
public record AppKafkaProperties(Topics topics) {
    public record Topics(String hubs, String sensors, String snapshots) {
    }
}
