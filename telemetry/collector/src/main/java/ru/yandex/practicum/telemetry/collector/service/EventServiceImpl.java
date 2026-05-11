package ru.yandex.practicum.telemetry.collector.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.telemetry.collector.kafka.CollectorProducer;
import ru.yandex.practicum.telemetry.collector.handler.hub.HubHandler;
import ru.yandex.practicum.telemetry.collector.handler.sensor.SensorHandler;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EventServiceImpl implements EventService {

    private final CollectorProducer producer;
    private final Map<SensorEventProto.PayloadCase, SensorHandler> sensorHadlersMap;
    private final Map<HubEventProto.PayloadCase, HubHandler> hubHadlersMap;

    public EventServiceImpl(
            CollectorProducer producer,
            Set<SensorHandler> sensorHandlersSet,
            Set<HubHandler> hubHandlersSet
    ) {
       this.producer = producer;

       this.sensorHadlersMap = sensorHandlersSet.stream()
               .collect(Collectors.toMap(SensorHandler::getType, Function.identity()));
       this.hubHadlersMap = hubHandlersSet.stream()
               .collect(Collectors.toMap(HubHandler::getType, Function.identity()));
    }

    @Override
    public void handleSensor(SensorEventProto sensorEventProto) {
        SensorHandler sensorHandler = sensorHadlersMap.get(sensorEventProto.getPayloadCase());

        if (sensorHandler == null) {
            throw new IllegalArgumentException("Обработчик " + sensorEventProto.getPayloadCase() + " не найден");
        }

        SensorEventAvro message = sensorHandler.handle(sensorEventProto);
        producer.sendToSensorTopic(null, message);

    }

    @Override
    public void handleHub(HubEventProto hubEventProto) {
        HubHandler hubHandler = hubHadlersMap.get(hubEventProto.getPayloadCase());

        if (hubHandler == null) {
            throw new IllegalArgumentException("Обработчик " + hubEventProto.getPayloadCase() + " не найден");
        }

        HubEventAvro message = hubHandler.handle(hubEventProto);
        producer.sendToHubTopic(null, message);
    }

}