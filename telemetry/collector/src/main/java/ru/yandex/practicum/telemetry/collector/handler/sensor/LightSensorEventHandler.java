package ru.yandex.practicum.telemetry.collector.handler.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.LightSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;

@Component
public class LightSensorEventHandler extends BaseSensorHandler<LightSensorAvro> {

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
