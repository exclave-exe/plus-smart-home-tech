package ru.yandex.practicum.telemetry.collector.handler.hub;

import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.time.Instant;

public abstract class BaseHubHandler<V extends SpecificRecordBase> implements HubHandler {

    protected abstract V mapToAvro(HubEventProto hubEventProto);

    @Override
    public HubEventAvro handle(HubEventProto hubEventProto) {
        V anyHubEventAvro = mapToAvro(hubEventProto);

        return HubEventAvro.newBuilder()
                .setHubId(hubEventProto.getHubId())
                .setTimestamp(Instant.ofEpochSecond(
                        hubEventProto.getTimestamp().getSeconds(),
                        hubEventProto.getTimestamp().getNanos()))
                .setPayload(anyHubEventAvro)
                .build();
    }

}