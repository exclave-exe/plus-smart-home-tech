package ru.yandex.practicum.telemetry.collector.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.telemetry.collector.handler.hub.HubHandler;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEventType;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class HubService {

    private final Map<HubEventType, HubHandler> hubHandlerMap;

    public HubService(Set<HubHandler> hubHandlerSet) {
        this.hubHandlerMap = hubHandlerSet.stream()
                .collect(Collectors.toMap(HubHandler::getType, Function.identity()));
    }

    public void handle(HubEvent hubEvent) {
        HubHandler hubHandler = hubHandlerMap.get(hubEvent.getType());

        if (hubHandler == null) {
            throw new IllegalArgumentException(
                    "Обработчик для события " + hubEvent.getType() + " не найден");
        }

        hubHandler.handle(hubEvent);
    }

}
