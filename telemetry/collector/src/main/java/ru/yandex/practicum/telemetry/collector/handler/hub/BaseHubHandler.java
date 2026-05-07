package ru.yandex.practicum.telemetry.collector.handler.hub;

import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.telemetry.collector.handler.CollectorProducer;

public abstract class BaseHubHandler<V extends SpecificRecordBase> implements HubHandler {

    protected final String topic;
    protected final CollectorProducer producer;

    protected BaseHubHandler(String topic, CollectorProducer producer) {
        this.topic = topic;
        this.producer = producer;
    }

    protected abstract V mapToAvro(HubEventProto hubEventProto);
}