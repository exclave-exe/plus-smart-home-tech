package ru.yandex.practicum.telemetry.collector.service;

import ru.yandex.practicum.telemetry.collector.model.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;

public interface EventService {

    void handleSensor(SensorEvent event);

    void handleHub(HubEvent event);
}
