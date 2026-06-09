package ru.yandex.practicum.telemetry.collector.controller;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.yandex.practicum.grpc.telemetry.collector.CollectorControllerGrpc.CollectorControllerImplBase;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.telemetry.collector.service.EventService;

@GrpcService
@RequiredArgsConstructor
public class CollectorController extends CollectorControllerImplBase {

    private final EventService eventService;

    @Override
    public void collectSensorEvent(SensorEventProto sensorEventProto, StreamObserver<Empty> response) {
        try {
            eventService.handleSensor(sensorEventProto);
            response.onNext(Empty.getDefaultInstance());
            response.onCompleted();
        } catch (Exception exception) {
            response.onError(new StatusRuntimeException(Status.INTERNAL
                    .withDescription(exception.getLocalizedMessage())
                    .withCause(exception)));
        }
    }

    @Override
    public void collectHubEvent(HubEventProto hubEventProto, StreamObserver<Empty> response) {
        try {
            eventService.handleHub(hubEventProto);
            response.onNext(Empty.getDefaultInstance());
            response.onCompleted();
        } catch (Exception exception) {
            response.onError(new StatusRuntimeException(Status.INTERNAL
                    .withDescription(exception.getLocalizedMessage())
                    .withCause(exception)));
        }
    }
}
