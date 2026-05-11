package ru.yandex.practicum.telemetry.aggregator.kafka;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
public class ConsumerAggregator {

    private final Consumer<String, SpecificRecordBase> consumer;
    private final String sensorTopic;

    public ConsumerAggregator(
            Consumer<String, SpecificRecordBase> consumer,
            ConsumerProperties properties
    ) {
        this.consumer = consumer;
        this.sensorTopic = properties.getSensorsTopic();
    }

    public void subscribe() {
        consumer.subscribe(List.of(sensorTopic));
    }

    public ConsumerRecords<String, SpecificRecordBase> poll(Duration duration) {
        return consumer.poll(duration);
    }

    public void commitAsync() {
        consumer.commitAsync();
    }

    public void commitSync() {
        consumer.commitSync();
    }

    public void wakeup() {
        consumer.wakeup();
    }

}
