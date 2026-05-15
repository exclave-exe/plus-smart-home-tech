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

    public HubConsumer(
            Consumer<String, HubEventAvro> consumer,
            HubConsumerProperties properties
    ) {
        this.consumer = consumer;
        this.sensorTopic = properties.getHubTopic();
    }

    public void subscribe() {
        consumer.subscribe(List.of(sensorTopic));
    }

    public ConsumerRecords<String, HubEventAvro> poll(Duration duration) {
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
