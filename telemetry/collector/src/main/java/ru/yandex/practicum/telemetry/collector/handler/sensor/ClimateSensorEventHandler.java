package ru.yandex.practicum.telemetry.collector.handler.sensor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.ClimateSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.telemetry.collector.handler.TelemetryProducer;

import java.time.Instant;

@Component
public class ClimateSensorEventHandler extends BaseSensorHandler<ClimateSensorAvro> {

    public ClimateSensorEventHandler(@Value("${kafka.topic.telemetry.sensors-topic}") String topic, TelemetryProducer telemetryProducer) {
        super(topic, telemetryProducer);
    }

    @Override
    public void handle(SensorEventProto sensorEventProto) {
        ClimateSensorAvro climateSensorAvro = mapToAvro(sensorEventProto);

        SensorEventAvro sensorEventAvro = SensorEventAvro.newBuilder()
                .setId(sensorEventProto.getId())
                .setHubId(sensorEventProto.getHubId())
                .setTimestamp(Instant.ofEpochSecond(
                        sensorEventProto.getTimestamp().getSeconds(),
                        sensorEventProto.getTimestamp().getNanos()))
                .setPayload(climateSensorAvro)
                .build();

        producer.send(topic, sensorEventAvro);
    }

    @Override
    protected ClimateSensorAvro mapToAvro(SensorEventProto sensorEventProto) {
        ClimateSensorProto climateSensorProto = sensorEventProto.getClimateSensor();

        return ClimateSensorAvro.newBuilder()
                .setTemperatureC(climateSensorProto.getTemperatureC())
                .setHumidity(climateSensorProto.getHumidity())
                .setCo2Level(climateSensorProto.getCo2Level())
                .build();
    }

    @Override
    public SensorEventProto.PayloadCase getType() {
        return SensorEventProto.PayloadCase.CLIMATE_SENSOR;
    }

}
