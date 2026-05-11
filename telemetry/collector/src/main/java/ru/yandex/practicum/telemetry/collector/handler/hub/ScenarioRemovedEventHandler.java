package ru.yandex.practicum.telemetry.collector.handler.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioRemovedEventProto;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

@Component
public class ScenarioRemovedEventHandler extends BaseHubHandler<ScenarioRemovedEventAvro> {

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
