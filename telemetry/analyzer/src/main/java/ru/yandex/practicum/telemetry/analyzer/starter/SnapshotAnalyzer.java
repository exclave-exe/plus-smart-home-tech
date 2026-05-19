package ru.yandex.practicum.telemetry.analyzer.starter;

import com.google.protobuf.Timestamp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.telemetry.analyzer.model.Action;
import ru.yandex.practicum.telemetry.analyzer.model.Condition;
import ru.yandex.practicum.telemetry.analyzer.model.Scenario;
import ru.yandex.practicum.telemetry.analyzer.repository.ScenarioRepository;

import java.time.Instant;
import java.util.Map;

import static ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc.HubRouterControllerBlockingStub;
import static ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SnapshotAnalyzer {

    private final ScenarioRepository scenarioRepository;

    private static final int TRUE_INT = 1;
    private static final int FALSE_INT = 0;

    @GrpcClient("hub-router")
    private HubRouterControllerBlockingStub routerClient;

    public void process(SensorsSnapshotAvro snapshot) {
        scenarioRepository.findByHubId(snapshot.getHubId())
                .stream()
                .filter(scenario -> matches(snapshot, scenario))
                .forEach(this::executeScenario);
    }

    private boolean matches(SensorsSnapshotAvro snapshot, Scenario scenario) {
        return scenario.getConditions()
                .entrySet()
                .stream()
                .allMatch(entry ->
                        validateCondition(
                                snapshot,
                                entry.getKey(),
                                entry.getValue()
                        )
                );
    }

    private boolean validateCondition(SensorsSnapshotAvro snapshot, String sensorId, Condition condition) {
        SensorStateAvro sensorState = snapshot.getSensorsState().get(sensorId);
        if (sensorState == null) return false;
        Object payload = sensorState.getData();
        return switch (payload) {
            case ClimateSensorAvro climate -> switch (condition.getType()) {
                case TEMPERATURE -> condition.check(climate.getTemperatureC());
                case CO2LEVEL -> condition.check(climate.getCo2Level());
                case HUMIDITY -> condition.check(climate.getHumidity());
                default -> false;
            };
            case LightSensorAvro light -> condition.getType() == LUMINOSITY
                    && condition.check(light.getLuminosity());
            case MotionSensorAvro motion -> condition.getType() == MOTION
                    && condition.check(motion.getMotion() ? TRUE_INT : FALSE_INT);
            case TemperatureSensorAvro temperature -> condition.getType() == TEMPERATURE
                    && condition.check(temperature.getTemperatureC());
            case SwitchSensorAvro sw -> condition.getType() == SWITCH
                    && condition.check(sw.getState() ? TRUE_INT : FALSE_INT);
            default -> false;
        };
    }

    @Transactional
    protected void executeScenario(Scenario scenario) {
        Timestamp timestamp = buildTimestamp();
        for (Map.Entry<String, Action> entry : scenario.getActions().entrySet()) {
            String sensorId = entry.getKey();
            Action action = entry.getValue();
            DeviceActionProto actionProto = buildAction(sensorId, action);
            DeviceActionRequest request = DeviceActionRequest.newBuilder()
                    .setHubId(scenario.getHubId())
                    .setScenarioName(scenario.getName())
                    .setAction(actionProto)
                    .setTimestamp(timestamp)
                    .build();
            sendRequest(request, scenario, action);
        }
    }

    private DeviceActionProto buildAction(String sensorId, Action action) {
        DeviceActionProto.Builder builder = DeviceActionProto.newBuilder()
                .setSensorId(sensorId)
                .setType(convertActionType(action.getType()));
        if (action.getType() == ActionTypeAvro.SET_VALUE) {
            builder.setValue(action.getValue());
        }
        return builder.build();
    }

    private void sendRequest(DeviceActionRequest request, Scenario scenario, Action action) {
        try {
            routerClient.handleDeviceAction(request);
        } catch (Exception ex) {
            log.error("Не удалось выполнить {}", action.getType(), ex);
        }
    }

    private Timestamp buildTimestamp() {
        Instant now = Instant.now();
        return Timestamp.newBuilder()
                .setSeconds(now.getEpochSecond())
                .setNanos(now.getNano())
                .build();
    }

    private ActionTypeProto convertActionType(ActionTypeAvro type) {
        return switch (type) {
            case ACTIVATE -> ActionTypeProto.ACTIVATE;
            case DEACTIVATE -> ActionTypeProto.DEACTIVATE;
            case INVERSE -> ActionTypeProto.INVERSE;
            case SET_VALUE -> ActionTypeProto.SET_VALUE;
        };
    }
}
