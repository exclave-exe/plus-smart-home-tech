package ru.yandex.practicum.telemetry.aggregator.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class AggregatorService {

    private final Map<String, SensorsSnapshotAvro> snapshots = new HashMap<>();

    public Optional<SensorsSnapshotAvro> updateState(SensorEventAvro sensorEventAvro) {

        String sensorId = sensorEventAvro.getId();
        String hubId = sensorEventAvro.getHubId();

        SensorsSnapshotAvro sensorsSnapshotAvro = snapshots.get(hubId);

        if (sensorsSnapshotAvro == null) {
            sensorsSnapshotAvro = createSensorSnapshotAvro(sensorEventAvro);
            snapshots.put(hubId, sensorsSnapshotAvro);
            return Optional.of(sensorsSnapshotAvro);
        }

        SensorStateAvro oldSensorStateAvro = sensorsSnapshotAvro.getSensorsState().get(sensorId);
        if (oldSensorStateAvro != null) {
            if (oldSensorStateAvro.getTimestamp().isAfter(sensorEventAvro.getTimestamp())) {
                return Optional.empty();
            }
            if (oldSensorStateAvro.getData().equals(sensorEventAvro.getPayload())) {
                return Optional.empty();
            }
        }

        SensorStateAvro updatedState = buildSensorStateAvro(sensorEventAvro);
        sensorsSnapshotAvro.getSensorsState().put(sensorId, updatedState);
        sensorsSnapshotAvro.setTimestamp(sensorEventAvro.getTimestamp());

        return Optional.of(sensorsSnapshotAvro);
    }

    private SensorsSnapshotAvro createSensorSnapshotAvro(SensorEventAvro sensorEventAvro) {
        Map<String, SensorStateAvro> sensorsState = new HashMap<>();
        sensorsState.put(sensorEventAvro.getId(), buildSensorStateAvro(sensorEventAvro));

        return SensorsSnapshotAvro.newBuilder()
                .setHubId(sensorEventAvro.getHubId())
                .setTimestamp(sensorEventAvro.getTimestamp())
                .setSensorsState(sensorsState)
                .build();
    }

    private SensorStateAvro buildSensorStateAvro(SensorEventAvro sensorEventAvro) {

        return SensorStateAvro.newBuilder()
                .setTimestamp(sensorEventAvro.getTimestamp())
                .setData(sensorEventAvro.getPayload())
                .build();
    }
}
