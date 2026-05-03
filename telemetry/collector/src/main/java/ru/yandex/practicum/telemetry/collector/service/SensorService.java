package ru.yandex.practicum.telemetry.collector.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.telemetry.collector.handler.sensor.SensorHandler;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEventType;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SensorService {

    private final Map<SensorEventType, SensorHandler> sensorHandlerMap;

    public SensorService(Set<SensorHandler> sensorHandlerSet) {
        this.sensorHandlerMap = sensorHandlerSet.stream()
                .collect(Collectors.toMap(SensorHandler::getType, Function.identity()));
    }

    public void handle(SensorEvent sensorEvent) {

        SensorHandler sensorHandler = sensorHandlerMap.get(sensorEvent.getType());

        if (sensorHandler == null) {
            throw new IllegalArgumentException(
                    "Обработчик для события " + sensorEvent.getType() + " не найден");
        }

        sensorHandler.handle(sensorEvent);
    }

}
