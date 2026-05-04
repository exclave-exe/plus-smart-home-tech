package ru.yandex.practicum.telemetry.collector.handler.hub;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;
import ru.yandex.practicum.telemetry.collector.handler.TelemetryProducer;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEventType;
import ru.yandex.practicum.telemetry.collector.model.hub.ScenarioRemovedEvent;

@Component
public class ScenarioRemovedEventHandler extends BaseHubHandler<ScenarioRemovedEventAvro> {


    protected ScenarioRemovedEventHandler(@Value("${kafka.topic.telemetry.hubs-topic}") String topic, TelemetryProducer producer) {
        super(topic, producer);
    }

    @Override
    public void handle(HubEvent hubEvent) {
        ScenarioRemovedEventAvro scenarioRemovedEventAvro = mapToAvro(hubEvent);

        HubEventAvro hubEventAvro = HubEventAvro.newBuilder()
                .setHubId(hubEvent.getHubId())
                .setTimestamp(hubEvent.getTimestamp())
                .setPayload(scenarioRemovedEventAvro)
                .build();

        producer.send(topic, hubEventAvro);
    }

    @Override
    protected ScenarioRemovedEventAvro mapToAvro(HubEvent hubEvent) {
        ScenarioRemovedEvent scenarioRemovedEvent = (ScenarioRemovedEvent) hubEvent;

        return ScenarioRemovedEventAvro.newBuilder()
                .setName(scenarioRemovedEvent.getName())
                .build();
    }

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_REMOVED;
    }
}
