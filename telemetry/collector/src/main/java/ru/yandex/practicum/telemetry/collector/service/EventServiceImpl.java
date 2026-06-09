package ru.yandex.practicum.telemetry.collector.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.telemetry.collector.handler.hub.HubHandler;
import ru.yandex.practicum.telemetry.collector.handler.sensor.SensorHandler;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEventType;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEventType;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EventServiceImpl implements EventService {

    private final Map<SensorEventType, SensorHandler> sensorHadlersMap;
    private final Map<HubEventType, HubHandler> hubHadlersMap;

    public EventServiceImpl(Set<SensorHandler> sensorHandlersSet, Set<HubHandler> hubHandlersSet) {
        this.sensorHadlersMap = sensorHandlersSet.stream()
                .collect(Collectors.toMap(SensorHandler::getType, Function.identity()));

        this.hubHadlersMap = hubHandlersSet.stream()
                .collect(Collectors.toMap(HubHandler::getType, Function.identity()));
    }

    @Override
    public void handleSensor(SensorEvent sensorEvent) {
        SensorHandler sensorHandler = sensorHadlersMap.get(sensorEvent.getType());

        if (sensorHandler == null) {
            throw new IllegalArgumentException(
                    "Обработчик для события " + sensorEvent.getType() + " не найден");
        }

        sensorHandler.handle(sensorEvent);
    }

    @Override
    public void handleHub(HubEvent hubEvent) {
        HubHandler hubHandler = hubHadlersMap.get(hubEvent.getType());

        if (hubHandler == null) {
            throw new IllegalArgumentException(
                    "Обработчик для события " + hubEvent.getType() + " не найден");
        }

        hubHandler.handle(hubEvent);
    }

}