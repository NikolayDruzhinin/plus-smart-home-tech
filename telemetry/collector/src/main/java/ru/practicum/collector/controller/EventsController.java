package ru.practicum.collector.controller;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.practicum.collector.handler.HubEventHandler;
import ru.practicum.collector.handler.SensorEventHandler;
import ru.yandex.practicum.grpc.telemetry.collector.CollectorControllerGrpc;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@GrpcService
@Slf4j
public class EventsController extends CollectorControllerGrpc.CollectorControllerImplBase {
    private final Map<SensorEventProto.PayloadCase, SensorEventHandler> sensorEventHandlers;
    private final Map<HubEventProto.PayloadCase, HubEventHandler> hubEventHandlers;

    public EventsController(Set<SensorEventHandler> sensorEventHandlers,
                            Set<HubEventHandler> hubEventHandlers) {
        this.sensorEventHandlers = sensorEventHandlers.stream()
                .collect(Collectors.toMap(SensorEventHandler::getMessageType, Function.identity()));
        this.hubEventHandlers = hubEventHandlers.stream()
                .collect(Collectors.toMap(HubEventHandler::getMessageType, Function.identity()));
    }


    @Override
    public void collectSensorEvent(SensorEventProto sensorEvent, StreamObserver<Empty> responseObserver) {
        try {
            if (!sensorEventHandlers.containsKey(sensorEvent.getPayloadCase())) {
                throw new IllegalArgumentException("Unknown event type " + sensorEvent.getPayloadCase().name());
            }
            sensorEventHandlers.get(sensorEvent.getPayloadCase()).handle(sensorEvent);
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (IllegalArgumentException e) {
            responseObserver.onError(
                    Status.INVALID_ARGUMENT.withDescription(e.getMessage()).asRuntimeException());
        } catch (Exception e) {
            responseObserver.onError(new StatusRuntimeException(Status.fromThrowable(e)));
        }
    }

    @Override
    public void collectHubEvent(HubEventProto hubEvent, StreamObserver<Empty> responseObserver) {
        try {
            if (!hubEventHandlers.containsKey(hubEvent.getPayloadCase())) {
                throw new IllegalArgumentException("Unknown event type " + hubEvent.getPayloadCase().name());
            }
            hubEventHandlers.get(hubEvent.getPayloadCase()).handle(hubEvent);
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (IllegalArgumentException e) {
            responseObserver.onError(
                    Status.INVALID_ARGUMENT.withDescription(e.getMessage()).asRuntimeException());
        } catch (Exception e) {
            responseObserver.onError(new StatusRuntimeException(Status.fromThrowable(e)));
        }
    }
}
