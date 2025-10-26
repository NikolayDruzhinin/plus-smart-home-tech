package ru.practicum.analyzer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "app.kafka")
public class AppKafkaProperties {
    private Common common = new Common();
    private Map<String, ConsumerSpec> consumers;

    public static class Common {
        private String autoOffsetReset = "earliest";
        private String keyDeserializer;
        // getters/setters
        public String getAutoOffsetReset() { return autoOffsetReset; }
        public void setAutoOffsetReset(String v) { this.autoOffsetReset = v; }
        public String getKeyDeserializer() { return keyDeserializer; }
        public void setKeyDeserializer(String v) { this.keyDeserializer = v; }
    }

    public static class ConsumerSpec {
        private String bootstrapServers;
        private String groupId;
        private String clientId;
        private String valueDeserializer;
        private List<String> topics;
        private Integer concurrency = 1;
        // getters/setters
        public String getBootstrapServers() { return bootstrapServers; }
        public void setBootstrapServers(String v) { this.bootstrapServers = v; }
        public String getGroupId() { return groupId; }
        public void setGroupId(String v) { this.groupId = v; }
        public String getClientId() { return clientId; }
        public void setClientId(String v) { this.clientId = v; }
        public String getValueDeserializer() { return valueDeserializer; }
        public void setValueDeserializer(String v) { this.valueDeserializer = v; }
        public List<String> getTopics() { return topics; }
        public void setTopics(List<String> v) { this.topics = v; }
        public Integer getConcurrency() { return concurrency; }
        public void setConcurrency(Integer v) { this.concurrency = v; }
    }

    // getters/setters
    public Common getCommon() { return common; }
    public void setCommon(Common common) { this.common = common; }
    public Map<String, ConsumerSpec> getConsumers() { return consumers; }
    public void setConsumers(Map<String, ConsumerSpec> consumers) { this.consumers = consumers; }
}

