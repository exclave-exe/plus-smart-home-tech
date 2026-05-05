package ru.yandex.practicum.telemetry.collector.handler.sensor;

import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

public interface SensorHandler {

    void handle(SensorEventProto sensorEventProto);

    SensorEventProto.PayloadCase getType();

}
