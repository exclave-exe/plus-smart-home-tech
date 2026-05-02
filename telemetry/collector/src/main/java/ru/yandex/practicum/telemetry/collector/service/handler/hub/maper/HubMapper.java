package ru.yandex.practicum.telemetry.collector.service.handler.hub.maper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.telemetry.collector.model.hub.*;

@Component
public class HubMapper {

    public static DeviceTypeAvro mapToDeviceTypeAvro(DeviceType deviceType) {
        return switch (deviceType) {
            case DeviceType.MOTION_SENSOR -> DeviceTypeAvro.MOTION_SENSOR;
            case DeviceType.TEMPERATURE_SENSOR -> DeviceTypeAvro.TEMPERATURE_SENSOR;
            case DeviceType.LIGHT_SENSOR -> DeviceTypeAvro.LIGHT_SENSOR;
            case DeviceType.CLIMATE_SENSOR -> DeviceTypeAvro.CLIMATE_SENSOR;
            case DeviceType.SWITCH_SENSOR -> DeviceTypeAvro.SWITCH_SENSOR;
        };
    }

    public static DeviceActionAvro mapToDeviceActionAvro(DeviceAction deviceAction) {
        return DeviceActionAvro.newBuilder()
                .setSensorId(deviceAction.getSensorId())
                .setType(mapToActionTypeAvro(deviceAction.getType()))
                .setValue(deviceAction.getValue())
                .build();
    }

    public static ScenarioConditionAvro mapToScenarioConditionAvro(ScenarioCondition scenarioCondition) {
        return ScenarioConditionAvro.newBuilder()
                .setSensorId(scenarioCondition.getSensorId())
                .setType(mapToConditionTypeAvro(scenarioCondition.getType()))
                .setOperation(mapToConditionOperationAvro(scenarioCondition.getOperation()))
                .setValue(scenarioCondition.getValue())
                .build();
    }

    private static ActionTypeAvro mapToActionTypeAvro(ActionType actionType) {
        return switch (actionType) {
            case ActionType.ACTIVATE -> ActionTypeAvro.ACTIVATE;
            case ActionType.DEACTIVATE -> ActionTypeAvro.DEACTIVATE;
            case ActionType.INVERSE -> ActionTypeAvro.INVERSE;
            case ActionType.SET_VALUE -> ActionTypeAvro.SET_VALUE;
        };
    }

    private static ConditionTypeAvro mapToConditionTypeAvro(ConditionType conditionType) {
        return switch (conditionType) {
            case ConditionType.MOTION -> ConditionTypeAvro.MOTION;
            case ConditionType.LUMINOSITY -> ConditionTypeAvro.LUMINOSITY;
            case ConditionType.SWITCH -> ConditionTypeAvro.SWITCH;
            case ConditionType.TEMPERATURE -> ConditionTypeAvro.TEMPERATURE;
            case ConditionType.CO2LEVEL -> ConditionTypeAvro.CO2LEVEL;
            case ConditionType.HUMIDITY -> ConditionTypeAvro.HUMIDITY;
        };
    }

    private static ConditionOperationAvro mapToConditionOperationAvro(ConditionOperation conditionOperation) {
        return switch (conditionOperation) {
            case ConditionOperation.EQUALS -> ConditionOperationAvro.EQUALS;
            case ConditionOperation.GREATER_THAN -> ConditionOperationAvro.GREATER_THAN;
            case ConditionOperation.LOWER_THAN -> ConditionOperationAvro.LOWER_THAN;
        };
    }
}