package ru.yandex.practicum.telemetry.collector.service.handler.sensor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.telemetry.collector.model.sensor.LightSensorEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEventType;
import ru.yandex.practicum.telemetry.collector.service.handler.TelemetryProducer;

@Component
public class LightSensorEventHandler extends BaseSensorHandler<LightSensorAvro> {

    public LightSensorEventHandler(@Value("${kafka.topic.telemetry.sensors-topic}") String topic, TelemetryProducer telemetryProducer) {
        super(topic, telemetryProducer);
    }

    @Override
    public void handle(SensorEvent sensorEvent) {
        LightSensorAvro lightSensorAvro = mapToAvro(sensorEvent);

        SensorEventAvro sensorEventAvro = SensorEventAvro.newBuilder()
                .setId(sensorEvent.getId())
                .setHubId(sensorEvent.getHubId())
                .setTimestamp(sensorEvent.getTimestamp())
                .setPayload(lightSensorAvro)
                .build();

        producer.send(topic, sensorEventAvro);
    }

    @Override
    protected LightSensorAvro mapToAvro(SensorEvent sensorEvent) {
        LightSensorEvent lightSensorEvent = (LightSensorEvent) sensorEvent;

        return LightSensorAvro.newBuilder()
                .setLinkQuality(lightSensorEvent.getLinkQuality())
                .setLuminosity(lightSensorEvent.getLuminosity())
                .build();
    }

    @Override
    public SensorEventType getType() {
        return SensorEventType.LIGHT_SENSOR_EVENT;
    }

}
