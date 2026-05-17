package ru.yandex.practicum.telemetry.aggregator.starter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.telemetry.aggregator.kafka.AggregatorConsumer;
import ru.yandex.practicum.telemetry.aggregator.kafka.AggregatorProducer;
import ru.yandex.practicum.telemetry.aggregator.service.AggregatorService;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AggregationStarter {

    private final AggregatorConsumer consumer;
    private final AggregatorProducer producer;
    private final AggregatorService service;

    public void start() {
        Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
        consumer.subscribe();

        try {
            while (true) {
                ConsumerRecords<String, SpecificRecordBase> records = consumer.poll();
                process(records);
                consumer.commitSync();
            }
        } catch (WakeupException ignored) {
        } catch (Exception e) {
            log.error("Error", e);
        } finally {
            consumer.close();
        }
    }

    private void process(ConsumerRecords<String, SpecificRecordBase> records) {
        for (ConsumerRecord<String, SpecificRecordBase> record : records) {
            SensorEventAvro event = (SensorEventAvro) record.value();
            Optional<SensorsSnapshotAvro> snapshot = service.updateState(event);
            snapshot.ifPresent(s -> producer.sendToSnapshotsTopic(null, s));
        }
    }
}