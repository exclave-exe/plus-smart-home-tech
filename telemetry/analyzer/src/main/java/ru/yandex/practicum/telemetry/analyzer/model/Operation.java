package ru.yandex.practicum.telemetry.analyzer.model;

import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;

import java.util.function.BiFunction;

public enum Operation {

    EQUALS((l, r) -> l.compareTo(r) == 0),
    GREATER_THAN((l, r) -> l.compareTo(r) > 0),
    LOWER_THAN((l, r) -> l.compareTo(r) < 0);

    private final BiFunction<Integer, Integer, Boolean> operation;

    Operation(BiFunction<Integer, Integer, Boolean> operation) {
        this.operation = operation;
    }

    public static Operation from(ConditionOperationAvro operation) {
        for (Operation value : values()) {
            if (value.name().equalsIgnoreCase(operation.name())) {
                return value;
            }
        }
        return null;
    }

    public boolean apply(Integer left, Integer right) {
        if (left == null || right == null) {
            return false;
        }
        return operation.apply(left, right);
    }
}