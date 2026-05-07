package ru.yandex.practicum.telemetry.collector.handler.hub;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.DeviceAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.collector.handler.CollectorProducer;
import ru.yandex.practicum.telemetry.collector.handler.hub.maper.HubMapper;

import java.time.Instant;

@Component
public class DeviceAddedEventHandler extends BaseHubHandler<DeviceAddedEventAvro> {

    public DeviceAddedEventHandler(@Value("${kafka.topic.telemetry.hubs-topic}") String topic,
                                   CollectorProducer collectorProducer) {
        super(topic, collectorProducer);
    }

    @Override
    public void handle(HubEventProto hubEventProto) {
        DeviceAddedEventAvro deviceAddedEventAvro = mapToAvro(hubEventProto);

        HubEventAvro hubEventAvro = HubEventAvro.newBuilder()
                .setHubId(hubEventProto.getHubId())
                .setTimestamp(Instant.ofEpochSecond(
                        hubEventProto.getTimestamp().getSeconds(),
                        hubEventProto.getTimestamp().getNanos()))
                .setPayload(deviceAddedEventAvro)
                .build();

        producer.send(topic, hubEventAvro);
    }

    @Override
    protected DeviceAddedEventAvro mapToAvro(HubEventProto hubEventProto) {
        DeviceAddedEventProto deviceAddedEventProto = hubEventProto.getDeviceAdded();

        return DeviceAddedEventAvro.newBuilder()
                .setId(deviceAddedEventProto.getId())
                .setType(HubMapper.mapToDeviceTypeAvro(deviceAddedEventProto.getType()))
                .build();
    }

    @Override
    public HubEventProto.PayloadCase getType() {
        return HubEventProto.PayloadCase.DEVICE_ADDED;
    }

}