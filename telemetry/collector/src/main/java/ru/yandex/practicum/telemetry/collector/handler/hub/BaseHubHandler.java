package ru.yandex.practicum.telemetry.collector.handler.hub;

import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.telemetry.collector.handler.TelemetryProducer;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEvent;

public abstract class BaseHubHandler<V extends SpecificRecordBase> implements HubHandler {

    protected final String topic;
    protected final TelemetryProducer producer;

    protected BaseHubHandler(String topic, TelemetryProducer producer) {
        this.topic = topic;
        this.producer = producer;
    }

    protected abstract V mapToAvro(HubEvent hubEvent);
}