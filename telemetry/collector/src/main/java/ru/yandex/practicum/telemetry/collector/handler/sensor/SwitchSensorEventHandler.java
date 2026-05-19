package ru.yandex.practicum.telemetry.collector.handler.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SwitchSensorProto;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;

@Component
public class SwitchSensorEventHandler extends BaseSensorHandler<SwitchSensorAvro> {

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
