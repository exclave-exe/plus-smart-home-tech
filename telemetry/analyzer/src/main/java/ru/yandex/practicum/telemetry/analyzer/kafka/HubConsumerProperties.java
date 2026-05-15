package ru.yandex.practicum.telemetry.analyzer.kafka;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("aggregator.kafka.consumer.hub")
@Setter
@Getter
public class HubConsumerProperties {

    private String bootstrapServers;
    private String keyDeserializer;
    private String valueDeserializer;
    private String groupId;
    private Boolean enableAutoCommit;
    private String hubTopic;

}
