package ru.yandex.practicum.telemetry.aggregator.kafka;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aggregator.kafka.producer")
@Setter
@Getter
public class ProducerProperties {

    private String bootstrapServers;
    private String keySerializer;
    private String valueSerializer;
    private String snapshotsTopic;

}
