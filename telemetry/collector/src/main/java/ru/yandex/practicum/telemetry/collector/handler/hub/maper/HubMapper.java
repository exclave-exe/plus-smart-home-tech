package ru.yandex.practicum.telemetry.collector.handler.hub.maper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.*;
import ru.yandex.practicum.kafka.telemetry.event.*;


@Component
public class HubMapper {

    public static DeviceTypeAvro mapToDeviceTypeAvro(DeviceTypeProto deviceTypeProto) {
        return switch (deviceTypeProto) {
            case DeviceTypeProto.MOTION_SENSOR -> DeviceTypeAvro.MOTION_SENSOR;
            case DeviceTypeProto.TEMPERATURE_SENSOR -> DeviceTypeAvro.TEMPERATURE_SENSOR;
            case DeviceTypeProto.LIGHT_SENSOR -> DeviceTypeAvro.LIGHT_SENSOR;
            case DeviceTypeProto.CLIMATE_SENSOR -> DeviceTypeAvro.CLIMATE_SENSOR;
            case DeviceTypeProto.SWITCH_SENSOR -> DeviceTypeAvro.SWITCH_SENSOR;
            case DeviceTypeProto.UNRECOGNIZED -> null;
        };
    }

    public static DeviceActionAvro mapToDeviceActionAvro(DeviceActionProto deviceActionProto) {
        return DeviceActionAvro.newBuilder()
                .setSensorId(deviceActionProto.getSensorId())
                .setType(mapToActionTypeAvro(deviceActionProto.getType()))
                .setValue(deviceActionProto.getValue())
                .build();
    }

    private static ActionTypeAvro mapToActionTypeAvro(ActionTypeProto actionTypeProto) {
        return switch (actionTypeProto) {
            case ActionTypeProto.ACTIVATE -> ActionTypeAvro.ACTIVATE;
            case ActionTypeProto.DEACTIVATE -> ActionTypeAvro.DEACTIVATE;
            case ActionTypeProto.INVERSE -> ActionTypeAvro.INVERSE;
            case ActionTypeProto.SET_VALUE -> ActionTypeAvro.SET_VALUE;
            case ActionTypeProto.UNRECOGNIZED -> null;
        };
    }

    public static ScenarioConditionAvro mapToScenarioConditionAvro(ScenarioConditionProto scenarioConditionProto) {
        Object value = null;

        if (scenarioConditionProto.hasIntValue()) {
            value = scenarioConditionProto.getIntValue();
        } else if (scenarioConditionProto.hasBoolValue()) {
            value = scenarioConditionProto.getBoolValue();
        }

        return ScenarioConditionAvro.newBuilder()
                .setSensorId(scenarioConditionProto.getSensorId())
                .setType(mapToConditionTypeAvro(scenarioConditionProto.getType()))
                .setOperation(mapToConditionOperationAvro(scenarioConditionProto.getOperation()))
                .setValue(value)
                .build();
    }

    private static ConditionTypeAvro mapToConditionTypeAvro(ConditionTypeProto conditionTypeProto) {
        return switch (conditionTypeProto) {
            case ConditionTypeProto.MOTION -> ConditionTypeAvro.MOTION;
            case ConditionTypeProto.LUMINOSITY -> ConditionTypeAvro.LUMINOSITY;
            case ConditionTypeProto.SWITCH -> ConditionTypeAvro.SWITCH;
            case ConditionTypeProto.TEMPERATURE -> ConditionTypeAvro.TEMPERATURE;
            case ConditionTypeProto.CO2LEVEL -> ConditionTypeAvro.CO2LEVEL;
            case ConditionTypeProto.HUMIDITY -> ConditionTypeAvro.HUMIDITY;
            case ConditionTypeProto.UNRECOGNIZED -> null;
        };
    }

    private static ConditionOperationAvro mapToConditionOperationAvro(ConditionOperationProto conditionOperationProto) {
        return switch (conditionOperationProto) {
            case ConditionOperationProto.EQUALS -> ConditionOperationAvro.EQUALS;
            case ConditionOperationProto.GREATER_THAN -> ConditionOperationAvro.GREATER_THAN;
            case ConditionOperationProto.LOWER_THAN -> ConditionOperationAvro.LOWER_THAN;
            case ConditionOperationProto.UNRECOGNIZED -> null;
        };
    }
}