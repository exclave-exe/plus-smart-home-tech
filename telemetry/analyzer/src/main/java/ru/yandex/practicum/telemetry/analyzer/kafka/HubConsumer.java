package ru.yandex.practicum.telemetry.analyzer.kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.time.Duration;
import java.util.List;

@Component
public class HubConsumer {

    private final Consumer<String, HubEventAvro> consumer;
    private final String sensorTopic;
    private final Duration timeout;


    public HubConsumer(
            Consumer<String, HubEventAvro> consumer,
            HubConsumerProperties properties
    ) {
        this.consumer = consumer;
        this.sensorTopic = properties.getHubTopic();
        this.timeout = properties.getTimeout();
    }

    public void subscribe() {
        consumer.subscribe(List.of(sensorTopic));
    }

    public ConsumerRecords<String, HubEventAvro> poll() {
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
