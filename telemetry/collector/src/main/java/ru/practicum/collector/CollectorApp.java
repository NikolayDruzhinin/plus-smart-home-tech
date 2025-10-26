package ru.practicum.collector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.practicum.collector.configuration.AppKafkaProperties;

@EnableConfigurationProperties(AppKafkaProperties.class)
@ConfigurationPropertiesScan
@SpringBootApplication(scanBasePackages = {
        "ru.practicum.kafka.serializer",
        "ru.practicum.collector"
})
public class CollectorApp {
    public static void main(String[] args) {
        SpringApplication.run(CollectorApp.class, args);
    }
}
