package ru.yandex.practicum.telemetry.collector.controller;

import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEventType;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEventType;
import ru.yandex.practicum.telemetry.collector.service.handler.hub.HubHandler;
import ru.yandex.practicum.telemetry.collector.service.handler.sensor.SensorHandler;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;


@Validated
@RestController
@RequestMapping(path = "/events", consumes = MediaType.APPLICATION_JSON_VALUE)
public class EventController {

    private final Map<SensorEventType, SensorHandler> sensorEventHandlers;
    private final Map<HubEventType, HubHandler> hubEventHandlers;

    public EventController(Set<SensorHandler> sensorHandlerSet, Set<HubHandler> hubHandlerSet) {
        this.sensorEventHandlers = sensorHandlerSet.stream()
                .collect(Collectors.toMap(SensorHandler::getType, Function.identity()));
        this.hubEventHandlers = hubHandlerSet.stream()
                .collect(Collectors.toMap(HubHandler::getType, Function.identity()));
    }

    @PostMapping("/sensors")
    public void collectHubEvent(@Valid @RequestBody SensorEvent sensorEvent) {
        SensorHandler sensorHandler = sensorEventHandlers.get(sensorEvent.getType());
        if (sensorHandler == null) {
            throw new IllegalArgumentException("Обработчик для события " + sensorEvent.getType() + "не найден.");
        }
        sensorHandler.handle(sensorEvent);
    }

    @PostMapping("/hubs")
    public void collectHubEvent(@Valid @RequestBody HubEvent hubEvent) {
        HubHandler hubHandler = hubEventHandlers.get(hubEvent.getType());
        if (hubHandler == null) {
            throw new IllegalArgumentException("Обработчик для события " + hubEvent.getType() + "не найден.");
        }
        hubHandler.handle(hubEvent);
    }
}
