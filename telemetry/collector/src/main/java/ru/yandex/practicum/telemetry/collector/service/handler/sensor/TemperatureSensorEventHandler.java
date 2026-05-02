package ru.yandex.practicum.telemetry.collector.service.handler.sensor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEventType;
import ru.yandex.practicum.telemetry.collector.model.sensor.TemperatureSensorEvent;
import ru.yandex.practicum.telemetry.collector.service.handler.TelemetryProducer;

@Component
public class TemperatureSensorEventHandler extends BaseSensorHandler<TemperatureSensorAvro> {

    public TemperatureSensorEventHandler(@Value("${kafka.topic.telemetry.sensors-topic}") String topic, TelemetryProducer telemetryProducer) {
        super(topic, telemetryProducer);
    }

    @Override
    public void handle(SensorEvent sensorEvent) {
        TemperatureSensorAvro temperatureSensorAvro = mapToAvro(sensorEvent);

        SensorEventAvro sensorEventAvro = SensorEventAvro.newBuilder()
                .setId(sensorEvent.getId())
                .setHubId(sensorEvent.getHubId())
                .setTimestamp(sensorEvent.getTimestamp())
                .setPayload(temperatureSensorAvro)
                .build();

        producer.send(topic, sensorEventAvro);
    }

    @Override
    protected TemperatureSensorAvro mapToAvro(SensorEvent event) {
        TemperatureSensorEvent temperatureSensorEvent = (TemperatureSensorEvent) event;
        return TemperatureSensorAvro.newBuilder()
                .setTemperatureC(temperatureSensorEvent.getTemperatureC())
                .setTemperatureF(temperatureSensorEvent.getTemperatureF())
                .build();
    }

    @Override
    public SensorEventType getType() {
        return SensorEventType.TEMPERATURE_SENSOR_EVENT;
    }
}
