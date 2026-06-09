package ru.yandex.practicum.telemetry.collector.kafka;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;

@Component
public class CollectorProducer {

    private final Producer<String, SpecificRecordBase> producer;
    private final String sensorTopic;
    private final String hubTopic;

    public CollectorProducer(
            Producer<String, SpecificRecordBase> producer,
            ProducerProperties properties
    ) {
        this.producer = producer;
        this.sensorTopic = properties.getSensorsTopic();
        this.hubTopic = properties.getHubsTopic();
    }

    public void sendToSensorTopic(String key, SpecificRecordBase message) {
        send(sensorTopic, key, message);
    }

    public void sendToHubTopic(String key, SpecificRecordBase message) {
        send(hubTopic, key, message);
    }

    private void send(String topic, String key, SpecificRecordBase message) {
        producer.send(new ProducerRecord<>(topic, key, message));
    }
}
