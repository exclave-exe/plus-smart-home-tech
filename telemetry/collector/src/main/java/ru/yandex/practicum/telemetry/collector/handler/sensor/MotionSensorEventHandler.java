package ru.yandex.practicum.telemetry.collector.handler.sensor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.MotionSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.telemetry.collector.handler.CollectorProducer;

import java.time.Instant;

@Component
public class MotionSensorEventHandler extends BaseSensorHandler<MotionSensorAvro> {

    public MotionSensorEventHandler(@Value("${kafka.topic.telemetry.sensors-topic}") String topic, CollectorProducer collectorProducer) {
        super(topic, collectorProducer);
    }

    @Override
    public void handle(SensorEventProto sensorEventProto) {
        MotionSensorAvro motionSensorAvro = mapToAvro(sensorEventProto);

        SensorEventAvro sensorEventAvro = SensorEventAvro.newBuilder()
                .setId(sensorEventProto.getId())
                .setHubId(sensorEventProto.getHubId())
                .setTimestamp(Instant.ofEpochSecond(
                        sensorEventProto.getTimestamp().getSeconds(),
                        sensorEventProto.getTimestamp().getNanos()))
                .setPayload(motionSensorAvro)
                .build();

        producer.send(topic, sensorEventAvro);
    }

    @Override
    protected MotionSensorAvro mapToAvro(SensorEventProto sensorEventProto) {
        MotionSensorProto motionSensorProto = sensorEventProto.getMotionSensor();

        return MotionSensorAvro.newBuilder()
                .setLinkQuality(motionSensorProto.getLinkQuality())
                .setMotion(motionSensorProto.getMotion())
                .setVoltage(motionSensorProto.getVoltage())
                .build();
    }

    @Override
    public SensorEventProto.PayloadCase getType() {
        return SensorEventProto.PayloadCase.MOTION_SENSOR;
    }

}
