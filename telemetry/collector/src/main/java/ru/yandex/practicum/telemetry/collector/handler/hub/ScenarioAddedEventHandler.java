package ru.yandex.practicum.telemetry.collector.handler.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioConditionProto;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.stream.Collectors;

@Component
public class ScenarioAddedEventHandler extends BaseHubHandler<ScenarioAddedEventAvro> {

    @Override
    protected ScenarioAddedEventAvro mapToAvro(HubEventProto hubEventProto) {
        ScenarioAddedEventProto scenarioAddedEventProto = hubEventProto.getScenarioAdded();

        return ScenarioAddedEventAvro.newBuilder()
                .setName(scenarioAddedEventProto.getName())
                .setActions(scenarioAddedEventProto.getActionList().stream()
                        .map(this::mapToDeviceActionAvro)
                        .collect(Collectors.toList()))
                .setConditions(scenarioAddedEventProto.getConditionList().stream()
                        .map(this::mapToScenarioConditionAvro)
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public HubEventProto.PayloadCase getType() {
        return HubEventProto.PayloadCase.SCENARIO_ADDED;
    }

    private DeviceActionAvro mapToDeviceActionAvro(DeviceActionProto deviceActionProto) {

        ActionTypeAvro type = switch (deviceActionProto.getType()) {
            case ACTIVATE -> ActionTypeAvro.ACTIVATE;
            case DEACTIVATE -> ActionTypeAvro.DEACTIVATE;
            case INVERSE -> ActionTypeAvro.INVERSE;
            case SET_VALUE -> ActionTypeAvro.SET_VALUE;
            case UNRECOGNIZED -> null;
        };

        return DeviceActionAvro.newBuilder()
                .setSensorId(deviceActionProto.getSensorId())
                .setType(type)
                .setValue(deviceActionProto.getValue())
                .build();
    }

    private ScenarioConditionAvro mapToScenarioConditionAvro(ScenarioConditionProto scenarioConditionProto) {
        Object value = null;

        if (scenarioConditionProto.hasIntValue()) {
            value = scenarioConditionProto.getIntValue();
        } else if (scenarioConditionProto.hasBoolValue()) {
            value = scenarioConditionProto.getBoolValue();
        }

        ConditionTypeAvro type = switch (scenarioConditionProto.getType()) {
            case MOTION -> ConditionTypeAvro.MOTION;
            case LUMINOSITY -> ConditionTypeAvro.LUMINOSITY;
            case SWITCH -> ConditionTypeAvro.SWITCH;
            case TEMPERATURE -> ConditionTypeAvro.TEMPERATURE;
            case CO2LEVEL -> ConditionTypeAvro.CO2LEVEL;
            case HUMIDITY -> ConditionTypeAvro.HUMIDITY;
            case UNRECOGNIZED -> throw new IllegalArgumentException("Unknown condition type");
        };

        ConditionOperationAvro operation = switch (scenarioConditionProto.getOperation()) {
            case EQUALS -> ConditionOperationAvro.EQUALS;
            case GREATER_THAN -> ConditionOperationAvro.GREATER_THAN;
            case LOWER_THAN -> ConditionOperationAvro.LOWER_THAN;
            case UNRECOGNIZED -> throw new IllegalArgumentException("Unknown operation type");
        };

        return ScenarioConditionAvro.newBuilder()
                .setSensorId(scenarioConditionProto.getSensorId())
                .setType(type)
                .setOperation(operation)
                .setValue(value)
                .build();
    }

}
