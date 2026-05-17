package ru.yandex.practicum.telemetry.analyzer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;
import ru.yandex.practicum.telemetry.analyzer.model.Action;
import ru.yandex.practicum.telemetry.analyzer.model.Condition;
import ru.yandex.practicum.telemetry.analyzer.model.Operation;
import ru.yandex.practicum.telemetry.analyzer.model.Scenario;
import ru.yandex.practicum.telemetry.analyzer.repository.ActionRepository;
import ru.yandex.practicum.telemetry.analyzer.repository.ConditionRepository;
import ru.yandex.practicum.telemetry.analyzer.repository.ScenarioRepository;
import ru.yandex.practicum.telemetry.analyzer.repository.SensorRepository;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
@RequiredArgsConstructor
public class ScenarioService {

    private final ScenarioRepository scenarioRepository;
    private final ConditionRepository conditionRepository;
    private final ActionRepository actionRepository;
    private final SensorRepository sensorRepository;

    private static final int TRUE_INT = 1;
    private static final int FALSE_INT = 0;

    public Scenario save(ScenarioAddedEventAvro event, String hubId) {

        Set<String> sensorIds = collectSensorIds(event);
        if (!sensorRepository.existsByIdInAndHubId(sensorIds, hubId)) {
            throw new IllegalStateException("Один из датчиков отсутствует");
        }

        Scenario scenario = scenarioRepository.findByHubIdAndName(hubId, event.getName())
                .map(this::clearScenario)
                .orElseGet(() -> createScenario(event.getName(), hubId));

        fillConditions(scenario, event);
        fillActions(scenario, event);

        conditionRepository.saveAll(scenario.getConditions().values());
        actionRepository.saveAll(scenario.getActions().values());

        return scenarioRepository.save(scenario);
    }

    public void delete(String scenarioName, String hubId) {
        Optional<Scenario> scenarioOptional = scenarioRepository.findByHubIdAndName(hubId, scenarioName);
        if (scenarioOptional.isEmpty()) return;

        Scenario scenario = scenarioOptional.get();

        conditionRepository.deleteAll(scenario.getConditions().values());
        actionRepository.deleteAll(scenario.getActions().values());
        scenarioRepository.delete(scenario);
    }

    private Set<String> collectSensorIds(ScenarioAddedEventAvro event) {
        return Stream.concat(
                event.getConditions().stream().map(ScenarioConditionAvro::getSensorId),
                event.getActions().stream().map(DeviceActionAvro::getSensorId)
        ).collect(Collectors.toSet());
    }

    private Scenario createScenario(String name, String hubId) {
        Scenario scenario = new Scenario();
        scenario.setName(name);
        scenario.setHubId(hubId);

        return scenario;
    }

    private Scenario clearScenario(Scenario scenario) {
        conditionRepository.deleteAll(scenario.getConditions().values());
        actionRepository.deleteAll(scenario.getActions().values());

        scenario.getConditions().clear();
        scenario.getActions().clear();

        return scenario;
    }

    private void fillConditions(Scenario scenario, ScenarioAddedEventAvro event) {
        for (ScenarioConditionAvro conditionAvro : event.getConditions()) {
            Condition condition = new Condition();
            condition.setType(conditionAvro.getType());
            condition.setOperation(Operation.from(conditionAvro.getOperation()));
            condition.setValue(convertValue(conditionAvro.getValue()));

            scenario.addCondition(conditionAvro.getSensorId(), condition);
        }
    }

    private void fillActions(Scenario scenario, ScenarioAddedEventAvro event) {
        for (DeviceActionAvro actionAvro : event.getActions()) {
            Action action = new Action();
            action.setType(actionAvro.getType());

            if (ActionTypeAvro.SET_VALUE.equals(actionAvro.getType())) {
                action.setValue(convertValue(actionAvro.getValue()));
            }

            scenario.addAction(actionAvro.getSensorId(), action);
        }
    }

    private Integer convertValue(Object value) {

        if (value instanceof Integer integerValue) {
            return integerValue;
        }

        if (value instanceof Boolean booleanValue) {
            return booleanValue ? TRUE_INT : FALSE_INT;
        }

        return null;
    }
}
