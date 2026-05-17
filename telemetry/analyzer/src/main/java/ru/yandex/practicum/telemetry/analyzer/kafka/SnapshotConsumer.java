package ru.yandex.practicum.telemetry.analyzer.kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.util.List;

@Component
public class SnapshotConsumer {

    private final Consumer<String, SensorsSnapshotAvro> consumer;
    private final String sensorTopic;
    private final Duration timeout;

    public SnapshotConsumer(
            Consumer<String, SensorsSnapshotAvro> consumer,
            SnapshotConsumerProperties properties
    ) {
        this.consumer = consumer;
        this.sensorTopic = properties.getSnapshotTopic();
        this.timeout = properties.getTimeout();
    }

    public void subscribe() {
        consumer.subscribe(List.of(sensorTopic));
    }

    public ConsumerRecords<String, SensorsSnapshotAvro> poll() {
        return consumer.poll(timeout);
    }

    public void close() {
        consumer.close();
    }

    public void commitSync() {
        consumer.commitSync();
    }

    public void wakeup() {
        consumer.wakeup();
    }

}
