package ru.yandex.practicum.telemetry.aggregator.kafka;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("aggregator.kafka.consumer")
@Setter
@Getter
public class ConsumerProperties {

    private String bootstrapServers;
    private String keyDeserializer;
    private String valueDeserializer;
    private String groupId;
    private Boolean enableAutoCommit;
    private String sensorsTopic;

}
