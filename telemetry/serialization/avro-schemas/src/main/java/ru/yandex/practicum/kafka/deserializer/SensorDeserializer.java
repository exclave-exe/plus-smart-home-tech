package ru.yandex.practicum.kafka.deserializer;

import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

public class SensorDeserializer extends BaseAvroDeserializer<SensorEventAvro> {
    public SensorDeserializer() {
        super(SensorEventAvro.getClassSchema());
    }
}