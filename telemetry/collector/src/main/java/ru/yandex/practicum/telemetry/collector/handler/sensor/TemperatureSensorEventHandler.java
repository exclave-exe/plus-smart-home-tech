package ru.yandex.practicum.telemetry.collector.handler.sensor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.TemperatureSensorProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;
import ru.yandex.practicum.telemetry.collector.handler.CollectorProducer;

import java.time.Instant;

@Component
public class TemperatureSensorEventHandler extends BaseSensorHandler<TemperatureSensorAvro> {

    public TemperatureSensorEventHandler(@Value("${kafka.topic.telemetry.sensors-topic}") String topic, CollectorProducer collectorProducer) {
        super(topic, collectorProducer);
    }

    @Override
    public void handle(SensorEventProto sensorEventProto) {
        TemperatureSensorAvro temperatureSensorAvro = mapToAvro(sensorEventProto);

        SensorEventAvro sensorEventAvro = SensorEventAvro.newBuilder()
                .setId(sensorEventProto.getId())
                .setHubId(sensorEventProto.getHubId())
                .setTimestamp(Instant.ofEpochSecond(
                        sensorEventProto.getTimestamp().getSeconds(),
                        sensorEventProto.getTimestamp().getNanos()))
                .setPayload(temperatureSensorAvro)
                .build();

        producer.send(topic, sensorEventAvro);
    }

    @Override
    protected TemperatureSensorAvro mapToAvro(SensorEventProto sensorEventProto) {
        TemperatureSensorProto temperatureSensorProto = sensorEventProto.getTemperatureSensor();
        return TemperatureSensorAvro.newBuilder()
                .setTemperatureC(temperatureSensorProto.getTemperatureC())
                .setTemperatureF(temperatureSensorProto.getTemperatureF())
                .build();
    }

    @Override
    public SensorEventProto.PayloadCase getType() {
        return SensorEventProto.PayloadCase.TEMPERATURE_SENSOR;
    }

}
