package ru.yandex.practicum.telemetry.analyzer.starter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.telemetry.analyzer.kafka.HubConsumer;
import ru.yandex.practicum.telemetry.analyzer.model.Sensor;
import ru.yandex.practicum.telemetry.analyzer.service.DeviceService;
import ru.yandex.practicum.telemetry.analyzer.service.ScenarioService;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class HubStarter implements Runnable {
    private final HubConsumer hubConsumer;
    private final DeviceService deviceService;
    private final ScenarioService scenarioService;

    @Override
    public void run() {
        Runtime.getRuntime().addShutdownHook(new Thread(hubConsumer::wakeup));
        hubConsumer.subscribe();
        try {
            while (true) {
                ConsumerRecords<String, HubEventAvro> records = hubConsumer.poll();
                if (!records.isEmpty()) {
                    for (ConsumerRecord<String, HubEventAvro> record : records) {
                        dispatch(record.value());
                    }
                    hubConsumer.commitSync();
                }
            }
        } catch (WakeupException ignored) {
        } catch (Exception exception) {
            log.error("Error", exception);
        } finally {
            hubConsumer.close();
        }
    }

    private void dispatch(HubEventAvro hubEvent) {
        switch (hubEvent.getPayload()) {
            case DeviceAddedEventAvro deviceAdded -> DeviceAddedHandler(hubEvent.getHubId(), deviceAdded);
            case DeviceRemovedEventAvro deviceRemoved -> DeviceRemovedHandler(hubEvent.getHubId(), deviceRemoved);
            case ScenarioAddedEventAvro scenarioAdded -> ScenarioAddedHandler(hubEvent.getHubId(), scenarioAdded);
            case ScenarioRemovedEventAvro scenarioRemoved ->
                    ScenarioRemovedHandler(hubEvent.getHubId(), scenarioRemoved);
            default -> log.warn("Not found handler", hubEvent);
        }
    }

    private void DeviceAddedHandler(String hubId, DeviceAddedEventAvro event) {
        Optional<Sensor> sensor = deviceService.findByIdAndHubId(hubId, event.getId());

        if (sensor.isPresent()) return;

        Sensor newSensor = new Sensor();
        newSensor.setHubId(hubId);
        newSensor.setId(event.getId());

        deviceService.save(newSensor);
    }

    private void DeviceRemovedHandler(String hubId, DeviceRemovedEventAvro event) {
        Optional<Sensor> sensor = deviceService.findByIdAndHubId(event.getId(), hubId);
        sensor.ifPresent(deviceService::delete);
    }

    private void ScenarioAddedHandler(String hubId, ScenarioAddedEventAvro event) {
        scenarioService.save(event, hubId);
    }

    private void ScenarioRemovedHandler(String hubId, ScenarioRemovedEventAvro event) {
        scenarioService.delete(event.getName(), hubId);
    }
}
