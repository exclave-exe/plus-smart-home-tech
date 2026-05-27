package ru.yandex.practicum.telemetry.collector.handler.hub;

import ru.yandex.practicum.telemetry.collector.model.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEventType;

public interface HubHandler {

    void handle(HubEvent hubEvent);

    HubEventType getType();

}

