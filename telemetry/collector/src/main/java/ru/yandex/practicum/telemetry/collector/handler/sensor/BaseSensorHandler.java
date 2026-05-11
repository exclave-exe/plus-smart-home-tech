package ru.yandex.practicum.telemetry.collector.handler.sensor;

import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.time.Instant;

public abstract class BaseSensorHandler<V extends SpecificRecordBase> implements SensorHandler {

    protected abstract V mapToAvro(SensorEventProto sensorEventProto);

    @Override
    public SensorEventAvro handle(SensorEventProto sensorEventProto) {
        V anySensorEventAvro = mapToAvro(sensorEventProto);

        return SensorEventAvro.newBuilder()
                .setId(sensorEventProto.getId())
                .setHubId(sensorEventProto.getHubId())
                .setTimestamp(Instant.ofEpochSecond(
                        sensorEventProto.getTimestamp().getSeconds(),
                        sensorEventProto.getTimestamp().getNanos()))
                .setPayload(anySensorEventAvro)
                .build();
    }
}