package ru.yandex.practicum.telemetry.aggregator.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class AggregatorService {

    private final Map<String, SensorsSnapshotAvro> snapshots = new HashMap<>();

    public Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event) {

        String hubId = event.getHubId();
        String sensorId = event.getId();
        Instant timestamp = event.getTimestamp();
        Object payload = event.getPayload();

        SensorsSnapshotAvro currentSnapshot = snapshots.get(hubId);
        if (currentSnapshot == null) {
            Map<String, SensorStateAvro> states = new HashMap<>();

            states.put(sensorId, SensorStateAvro.newBuilder()
                    .setTimestamp(timestamp)
                    .setData(payload)
                    .build());

            SensorsSnapshotAvro snapshot = SensorsSnapshotAvro.newBuilder()
                    .setHubId(hubId)
                    .setTimestamp(timestamp)
                    .setSensorsState(states)
                    .build();

            snapshots.put(hubId, snapshot);
            return Optional.of(snapshot);
        }

        SensorStateAvro previousState = currentSnapshot.getSensorsState().get(sensorId);
        if (previousState != null) {
            if (previousState.getTimestamp().isAfter(timestamp)) {
                return Optional.empty();
            }
            if (previousState.getData().equals(payload)) {
                return Optional.empty();
            }
        }

        Map<String, SensorStateAvro> updatedStates = new HashMap<>(currentSnapshot.getSensorsState());

        updatedStates.put(sensorId, SensorStateAvro.newBuilder()
                .setTimestamp(timestamp)
                .setData(payload)
                .build());

        SensorsSnapshotAvro newSnapshot = SensorsSnapshotAvro.newBuilder()
                .setHubId(hubId)
                .setTimestamp(timestamp)
                .setSensorsState(updatedStates)
                .build();

        snapshots.put(hubId, newSnapshot);
        return Optional.of(newSnapshot);
    }

}
