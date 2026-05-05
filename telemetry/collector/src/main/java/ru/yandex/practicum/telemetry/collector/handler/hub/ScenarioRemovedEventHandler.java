package ru.yandex.practicum.telemetry.collector.handler.hub;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioRemovedEventProto;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;
import ru.yandex.practicum.telemetry.collector.handler.TelemetryProducer;

import java.time.Instant;

@Component
public class ScenarioRemovedEventHandler extends BaseHubHandler<ScenarioRemovedEventAvro> {


    protected ScenarioRemovedEventHandler(@Value("${kafka.topic.telemetry.hubs-topic}") String topic, TelemetryProducer producer) {
        super(topic, producer);
    }

    @Override
    public void handle(HubEventProto hubEventProto) {
        ScenarioRemovedEventAvro scenarioRemovedEventAvro = mapToAvro(hubEventProto);

        HubEventAvro hubEventAvro = HubEventAvro.newBuilder()
                .setHubId(hubEventProto.getHubId())
                .setTimestamp(Instant.ofEpochSecond(
                        hubEventProto.getTimestamp().getSeconds(),
                        hubEventProto.getTimestamp().getNanos()))
                .setPayload(scenarioRemovedEventAvro)
                .build();

        producer.send(topic, hubEventAvro);
    }

    @Override
    protected ScenarioRemovedEventAvro mapToAvro(HubEventProto hubEventProto) {
        ScenarioRemovedEventProto scenarioRemovedEventProto = hubEventProto.getScenarioRemoved();

        return ScenarioRemovedEventAvro.newBuilder()
                .setName(scenarioRemovedEventProto.getName())
                .build();
    }

    @Override
    public HubEventProto.PayloadCase getType() {
        return HubEventProto.PayloadCase.SCENARIO_REMOVED;
    }

}
