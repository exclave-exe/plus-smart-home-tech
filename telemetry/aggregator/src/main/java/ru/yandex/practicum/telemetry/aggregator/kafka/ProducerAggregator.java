package ru.yandex.practicum.telemetry.aggregator.kafka;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;

@Component
public class ProducerAggregator {

    private final Producer<String, SpecificRecordBase> producer;
    private final String snapshotsTopic;

    public ProducerAggregator(
            Producer<String, SpecificRecordBase> producer,
            ProducerProperties properties
    ) {
        this.producer = producer;
        this.snapshotsTopic = properties.getSnapshotsTopic();
    }

    public void sendToSnapshotsTopic(String key, SpecificRecordBase message) {
        send(snapshotsTopic, key, message);
    }

    private void send(String topic, String key, SpecificRecordBase message) {
        producer.send(new ProducerRecord<>(topic, key, message));
    }
}