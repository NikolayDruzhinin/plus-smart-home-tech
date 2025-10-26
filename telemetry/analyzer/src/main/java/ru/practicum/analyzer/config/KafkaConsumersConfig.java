package ru.practicum.analyzer.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(AppKafkaProperties.class)
public class KafkaConsumersConfig {

    private final AppKafkaProperties props;

    public KafkaConsumersConfig(AppKafkaProperties props) {
        this.props = props;
    }

    private Map<String, Object> baseProps(AppKafkaProperties.ConsumerSpec spec) {
        Map<String, Object> m = new HashMap<>();
        m.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, spec.getBootstrapServers());
        m.put(ConsumerConfig.GROUP_ID_CONFIG, spec.getGroupId());
        m.put(ConsumerConfig.CLIENT_ID_CONFIG, spec.getClientId());
        m.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, props.getCommon().getAutoOffsetReset());

        String keyDeser = props.getCommon().getKeyDeserializer();
        m.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                keyDeser != null ? keyDeser : StringDeserializer.class);

        m.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, spec.getValueDeserializer());

        return m;
    }

    @Bean(name = "hubsKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, Object> hubsFactory() {
        AppKafkaProperties.ConsumerSpec spec = props.getConsumers().get("hubs");
        var cf = new DefaultKafkaConsumerFactory<String, Object>(baseProps(spec));
        var f = new ConcurrentKafkaListenerContainerFactory<String, Object>();
        f.setConsumerFactory(cf);
        f.setConcurrency(spec.getConcurrency());
        return f;
    }

    @Bean(name = "snapshotsKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, Object> snapshotsFactory() {
        AppKafkaProperties.ConsumerSpec spec = props.getConsumers().get("snapshots");
        var cf = new DefaultKafkaConsumerFactory<String, Object>(baseProps(spec));
        var f = new ConcurrentKafkaListenerContainerFactory<String, Object>();
        f.setConsumerFactory(cf);
        f.setConcurrency(spec.getConcurrency());
        return f;
    }

    @Bean("hubsTopics")
    public List<String> hubsTopics() {
        return props.getConsumers().get("hubs").getTopics();
    }

    @Bean("snapshotsTopics")
    public List<String> snapshotsTopics() {
        return props.getConsumers().get("snapshots").getTopics();
    }
}

