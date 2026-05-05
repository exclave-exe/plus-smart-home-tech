package ru.yandex.practicum.telemetry.collector.handler.hub;

import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;

public interface HubHandler {

    void handle(HubEventProto hubEventProto);

    HubEventProto.PayloadCase getType();

}

