package ru.yandex.practicum.telemetry.collector.service.handler.sensor;

import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.service.handler.TelemetryProducer;

public abstract class BaseSensorHandler<V extends SpecificRecordBase> implements SensorHandler {

    protected final String topic;
    protected final TelemetryProducer producer;

    protected BaseSensorHandler(String topic, TelemetryProducer producer) {
        this.topic = topic;
        this.producer = producer;
    }

    protected abstract V mapToAvro(SensorEvent sensorEvent);
}