package ru.yandex.practicum.telemetry.collector.handler.hub;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioAddedEventProto;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.telemetry.collector.handler.CollectorProducer;
import ru.yandex.practicum.telemetry.collector.handler.hub.maper.HubMapper;

import java.time.Instant;
import java.util.stream.Collectors;

@Component
public class ScenarioAddedEventHandler extends BaseHubHandler<ScenarioAddedEventAvro> {

    protected ScenarioAddedEventHandler(@Value("${kafka.topic.telemetry.hubs-topic}") String topic,
                                        CollectorProducer collectorProducer) {
        super(topic, collectorProducer);
    }

    @Override
    public void handle(HubEventProto hubEventProto) {
        ScenarioAddedEventAvro scenarioAddedEventAvro = mapToAvro(hubEventProto);

        HubEventAvro hubEventAvro = HubEventAvro.newBuilder()
                .setHubId(hubEventProto.getHubId())
                .setTimestamp(Instant.ofEpochSecond(
                        hubEventProto.getTimestamp().getSeconds(),
                        hubEventProto.getTimestamp().getNanos()))
                .setPayload(scenarioAddedEventAvro)
                .build();

        producer.send(topic, hubEventAvro);
    }

    @Override
    protected ScenarioAddedEventAvro mapToAvro(HubEventProto hubEventProto) {
        ScenarioAddedEventProto scenarioAddedEventProto = hubEventProto.getScenarioAdded();

        return ScenarioAddedEventAvro.newBuilder()
                .setName(scenarioAddedEventProto.getName())
                .setActions(scenarioAddedEventProto.getActionList().stream()
                        .map(HubMapper::mapToDeviceActionAvro)
                        .collect(Collectors.toList()))
                .setConditions(scenarioAddedEventProto.getConditionList().stream()
                        .map(HubMapper::mapToScenarioConditionAvro)
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public HubEventProto.PayloadCase getType() {
        return HubEventProto.PayloadCase.SCENARIO_ADDED;
    }

}
