package ru.yandex.practicum.telemetry.collector.kafka;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("collector.kafka.producer")
@Getter
@Setter
public class ProducerProperties {

    private String bootstrapServers;
    private String keySerializer;
    private String valueSerializer;
    private String sensorsTopic;
    private String hubsTopic;

}