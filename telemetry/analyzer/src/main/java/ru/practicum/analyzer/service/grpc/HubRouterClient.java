package ru.practicum.analyzer.service.grpc;

import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;

@Component
@RequiredArgsConstructor
@Slf4j
public class HubRouterClient {

    private final HubRouterControllerGrpc.HubRouterControllerBlockingStub stub;

    public void sendAction(DeviceActionRequest request) {
        try {
            stub.handleDeviceAction(request);
            log.info("Action sent: {}", request);
        } catch (StatusRuntimeException e) {
            log.error("Error sending action: {}", e.getMessage(), e);
        }
    }
}