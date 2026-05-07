package ru.yandex.practicum.telemetry.collector.handler.hub;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.DeviceRemovedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.collector.handler.CollectorProducer;

import java.time.Instant;

@Component
public class DeviceRemovedEventHandler extends BaseHubHandler<DeviceRemovedEventAvro> {

    protected DeviceRemovedEventHandler(@Value("${kafka.topic.telemetry.hubs-topic}") String topic,
                                        CollectorProducer collectorProducer) {
        super(topic, collectorProducer);
    }

    @Override
    public void handle(HubEventProto hubEventProto) {
        DeviceRemovedEventAvro deviceRemovedEventAvro = mapToAvro(hubEventProto);

        HubEventAvro hubEventAvro = HubEventAvro.newBuilder()
                .setHubId(hubEventProto.getHubId())
                .setTimestamp(Instant.ofEpochSecond(
                        hubEventProto.getTimestamp().getSeconds(),
                        hubEventProto.getTimestamp().getNanos()))
                .setPayload(deviceRemovedEventAvro)
                .build();

        producer.send(topic, hubEventAvro);
    }

    @Override
    protected DeviceRemovedEventAvro mapToAvro(HubEventProto hubEventProto) {
        DeviceRemovedEventProto deviceRemovedEventProto = hubEventProto.getDeviceRemoved();

        return DeviceRemovedEventAvro.newBuilder()
                .setId(deviceRemovedEventProto.getId())
                .build();
    }

    @Override
    public HubEventProto.PayloadCase getType() {
        return HubEventProto.PayloadCase.DEVICE_REMOVED;
    }

}
