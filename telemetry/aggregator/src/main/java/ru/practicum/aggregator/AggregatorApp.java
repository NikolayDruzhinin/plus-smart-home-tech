package ru.practicum.aggregator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import ru.practicum.aggregator.configuration.AppKafkaProperties;
import ru.practicum.collector.CollectorApp;

@EnableConfigurationProperties(AppKafkaProperties.class)
@SpringBootApplication(scanBasePackages = {
        "ru.practicum.kafka.serializer",
        "ru.practicum.collector",
        "ru.practicum.aggregator"
})
public class AggregatorApp {
    public static void main(String[] args) {
        SpringApplication.run(AggregatorApp.class, args);

        ConfigurableApplicationContext collectorCtx =
                new SpringApplicationBuilder(CollectorApp.class)
                        .web(WebApplicationType.NONE)
                        .properties("grpc.server.port: 59091")
                        .run(args);
    }
}