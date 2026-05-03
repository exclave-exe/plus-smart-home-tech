package ru.yandex.practicum.telemetry.collector.handler.hub;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.telemetry.collector.handler.TelemetryProducer;
import ru.yandex.practicum.telemetry.collector.handler.hub.maper.HubMapper;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEventType;
import ru.yandex.practicum.telemetry.collector.model.hub.ScenarioAddedEvent;

import java.util.stream.Collectors;

@Component
public class ScenarioAddedEventHandler extends BaseHubHandler<ScenarioAddedEventAvro> {

    protected ScenarioAddedEventHandler(@Value("${kafka.topic.telemetry.hubs-topic}") String topic,
                                        TelemetryProducer producer) {
        super(topic, producer);
    }

    @Override
    public void handle(HubEvent hubEvent) {
        ScenarioAddedEventAvro scenarioAddedEventAvro = mapToAvro(hubEvent);

        HubEventAvro hubEventAvro = HubEventAvro.newBuilder()
                .setHubId(hubEvent.getHubId())
                .setTimestamp(hubEvent.getTimestamp())
                .setPayload(scenarioAddedEventAvro)
                .build();

        producer.send(topic, hubEventAvro);
    }

    @Override
    protected ScenarioAddedEventAvro mapToAvro(HubEvent hubEvent) {
        ScenarioAddedEvent scenarioAddedEvent = (ScenarioAddedEvent) hubEvent;

        return ScenarioAddedEventAvro.newBuilder()
                .setName(scenarioAddedEvent.getName())
                .setActions(scenarioAddedEvent.getActions().stream()
                        .map(HubMapper::mapToDeviceActionAvro)
                        .collect(Collectors.toList()))
                .setConditions(scenarioAddedEvent.getConditions().stream()
                        .map(HubMapper::mapToScenarioConditionAvro)
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_ADDED;
    }
}
