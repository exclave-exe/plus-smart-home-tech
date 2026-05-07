package ru.yandex.practicum.telemetry.collector.handler.sensor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.LightSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.telemetry.collector.handler.CollectorProducer;

import java.time.Instant;

@Component
public class LightSensorEventHandler extends BaseSensorHandler<LightSensorAvro> {

    public LightSensorEventHandler(@Value("${kafka.topic.telemetry.sensors-topic}") String topic, CollectorProducer collectorProducer) {
        super(topic, collectorProducer);
    }

    @Override
    public void handle(SensorEventProto sensorEventProto) {
        LightSensorAvro lightSensorAvro = mapToAvro(sensorEventProto);

        SensorEventAvro sensorEventAvro = SensorEventAvro.newBuilder()
                .setId(sensorEventProto.getId())
                .setHubId(sensorEventProto.getHubId())
                .setTimestamp(Instant.ofEpochSecond(
                        sensorEventProto.getTimestamp().getSeconds(),
                        sensorEventProto.getTimestamp().getNanos()))
                .setPayload(lightSensorAvro)
                .build();

        producer.send(topic, sensorEventAvro);
    }

    @Override
    protected LightSensorAvro mapToAvro(SensorEventProto sensorEventProto) {
        LightSensorProto lightSensorProto = sensorEventProto.getLightSensor();

        return LightSensorAvro.newBuilder()
                .setLinkQuality(lightSensorProto.getLinkQuality())
                .setLuminosity(lightSensorProto.getLuminosity())
                .build();
    }

    @Override
    public SensorEventProto.PayloadCase getType() {
        return SensorEventProto.PayloadCase.LIGHT_SENSOR;
    }

}
