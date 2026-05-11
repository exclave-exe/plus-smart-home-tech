package ru.yandex.practicum.telemetry.collector.handler.hub;

import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

public interface HubHandler {

    HubEventAvro handle(HubEventProto hubEventProto);

    HubEventProto.PayloadCase getType();

}