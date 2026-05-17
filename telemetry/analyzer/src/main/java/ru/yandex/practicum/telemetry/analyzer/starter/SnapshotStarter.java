package ru.yandex.practicum.telemetry.analyzer.starter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.telemetry.analyzer.kafka.SnapshotConsumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class SnapshotStarter {
    private final SnapshotConsumer snapshotConsumer;
    private final SnapshotAnalyzer snapshotAnalyzer;

    public void start() {
        Runtime.getRuntime().addShutdownHook(new Thread(snapshotConsumer::wakeup));
        snapshotConsumer.subscribe();
        try {
            while (true) {
                ConsumerRecords<String, SensorsSnapshotAvro> records = snapshotConsumer.poll();

                if (!records.isEmpty()) {
                    int count = 0;
                    for (ConsumerRecord<String, SensorsSnapshotAvro> record : records) {
                        snapshotAnalyzer.process(record.value());
                    }
                    snapshotConsumer.commitSync();
                }
            }
        } catch (WakeupException ignored) {
        } catch (Exception exception) {
            log.error("Error", exception);
        } finally {
            snapshotConsumer.close();
        }
    }
}
