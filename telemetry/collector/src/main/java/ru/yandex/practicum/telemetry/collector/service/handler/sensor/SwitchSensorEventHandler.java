package ru.yandex.practicum.telemetry.collector.service.handler.sensor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEventType;
import ru.yandex.practicum.telemetry.collector.model.sensor.SwitchSensorEvent;
import ru.yandex.practicum.telemetry.collector.service.handler.TelemetryProducer;

@Component
public class SwitchSensorEventHandler extends BaseSensorHandler<SwitchSensorAvro> {

    public SwitchSensorEventHandler(@Value("${kafka.topic.telemetry.sensors-topic}") String topic, TelemetryProducer telemetryProducer) {
        super(topic, telemetryProducer);
    }

    @Override
    public void handle(SensorEvent sensorEvent) {
        SwitchSensorAvro switchSensorAvro = mapToAvro(sensorEvent);

        SensorEventAvro sensorEventAvro = SensorEventAvro.newBuilder()
                .setId(sensorEvent.getId())
                .setHubId(sensorEvent.getHubId())
                .setTimestamp(sensorEvent.getTimestamp())
                .setPayload(switchSensorAvro)
                .build();

        producer.send(topic, sensorEventAvro);
    }

    @Override
    protected SwitchSensorAvro mapToAvro(SensorEvent sensorEvent) {
        SwitchSensorEvent switchSensorEvent = (SwitchSensorEvent) sensorEvent;

        return SwitchSensorAvro.newBuilder()
                .setState(switchSensorEvent.getState())
                .build();
    }

    @Override
    public SensorEventType getType() {
        return SensorEventType.SWITCH_SENSOR_EVENT;
    }
}
