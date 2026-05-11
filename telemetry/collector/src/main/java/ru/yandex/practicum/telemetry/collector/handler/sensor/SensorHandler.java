package ru.yandex.practicum.telemetry.collector.handler.sensor;

import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

public interface SensorHandler {

    SensorEventAvro handle(SensorEventProto sensorEventProto);

    SensorEventProto.PayloadCase getType();

}
