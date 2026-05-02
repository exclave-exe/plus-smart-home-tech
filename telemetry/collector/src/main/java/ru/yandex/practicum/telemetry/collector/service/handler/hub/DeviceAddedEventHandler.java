package ru.yandex.practicum.telemetry.collector.service.handler.hub;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.collector.model.hub.DeviceAddedEvent;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEventType;
import ru.yandex.practicum.telemetry.collector.service.handler.TelemetryProducer;
import ru.yandex.practicum.telemetry.collector.service.handler.hub.maper.HubMapper;

@Component
public class DeviceAddedEventHandler extends BaseHubHandler<DeviceAddedEventAvro> {

    public DeviceAddedEventHandler(@Value("${kafka.topic.telemetry.hubs-topic}") String topic,
                                   TelemetryProducer telemetryProducer) {
        super(topic, telemetryProducer);
    }

    @Override
    public void handle(HubEvent hubEvent) {
        DeviceAddedEventAvro deviceAddedEventAvro = mapToAvro(hubEvent);

        HubEventAvro hubEventAvro = HubEventAvro.newBuilder()
                .setHubId(hubEvent.getHubId())
                .setTimestamp(hubEvent.getTimestamp())
                .setPayload(deviceAddedEventAvro)
                .build();

        producer.send(topic, hubEventAvro);
    }

    @Override
    protected DeviceAddedEventAvro mapToAvro(HubEvent hubEvent) {
        DeviceAddedEvent deviceAddedEvent = (DeviceAddedEvent) hubEvent;

        return DeviceAddedEventAvro.newBuilder()
                .setId(deviceAddedEvent.getId())
                .setType(HubMapper.mapToDeviceTypeAvro(deviceAddedEvent.getDeviceType()))
                .build();
    }

    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_ADDED;
    }
}