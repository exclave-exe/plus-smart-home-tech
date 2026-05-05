package ru.yandex.practicum.telemetry.collector.handler.sensor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SwitchSensorProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;
import ru.yandex.practicum.telemetry.collector.handler.TelemetryProducer;

import java.time.Instant;

@Component
public class SwitchSensorEventHandler extends BaseSensorHandler<SwitchSensorAvro> {

    public SwitchSensorEventHandler(@Value("${kafka.topic.telemetry.sensors-topic}") String topic, TelemetryProducer telemetryProducer) {
        super(topic, telemetryProducer);
    }

    @Override
    public void handle(SensorEventProto sensorEventProto) {
        SwitchSensorAvro switchSensorAvro = mapToAvro(sensorEventProto);

        SensorEventAvro sensorEventAvro = SensorEventAvro.newBuilder()
                .setId(sensorEventProto.getId())
                .setHubId(sensorEventProto.getHubId())
                .setTimestamp(Instant.ofEpochSecond(
                        sensorEventProto.getTimestamp().getSeconds(),
                        sensorEventProto.getTimestamp().getNanos()))
                .setPayload(switchSensorAvro)
                .build();

        producer.send(topic, sensorEventAvro);
    }

    @Override
    protected SwitchSensorAvro mapToAvro(SensorEventProto sensorEventProto) {
        SwitchSensorProto switchSensorProto = sensorEventProto.getSwitchSensor();

        return SwitchSensorAvro.newBuilder()
                .setState(switchSensorProto.getState())
                .build();
    }

    @Override
    public SensorEventProto.PayloadCase getType() {
        return SensorEventProto.PayloadCase.SWITCH_SENSOR;
    }

}
