package ru.practicum.aggregator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.practicum.aggregator.configuration.AppKafkaProperties;

@EnableConfigurationProperties(AppKafkaProperties.class)
@SpringBootApplication(scanBasePackages = {
        "ru.practicum.kafka.serializer",
        "ru.practicum.collector",
        "ru.practicum.aggregator"
})
public class AggregatorApp {
    public static void main(String[] args) {
        SpringApplication.run(AggregatorApp.class, args);
    }
}