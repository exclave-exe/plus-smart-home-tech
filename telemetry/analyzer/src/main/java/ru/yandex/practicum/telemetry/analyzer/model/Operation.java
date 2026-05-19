package ru.yandex.practicum.telemetry.analyzer.model;

import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;

import java.util.function.BiFunction;

public enum Operation {
    EQUALS,
    GREATER_THAN,
    LOWER_THAN;

    public static Operation from(ConditionOperationAvro operation) {
        if (operation == null) return null;
        return Operation.valueOf(operation.name());
    }

    public boolean apply(Integer left, Integer right) {
        if (left == null || right == null) return false;

        return switch (this) {
            case EQUALS -> left.compareTo(right) == 0;
            case GREATER_THAN -> left.compareTo(right) > 0;
            case LOWER_THAN -> left.compareTo(right) < 0;
        };
    }
}