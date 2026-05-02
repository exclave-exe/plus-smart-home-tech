package ru.yandex.practicum.telemetry.collector.service.handler;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TelemetryProducer {

    private final KafkaTemplate<String, SpecificRecordBase> kafkaTemplate;

    public <T extends SpecificRecordBase> void send(String topic, String key, T value) {
        kafkaTemplate.send(topic, key, value);
    }

    public <T extends SpecificRecordBase> void send(String topic, T value) {
        kafkaTemplate.send(topic, value);
    }
}
