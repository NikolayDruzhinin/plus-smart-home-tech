package ru.practicum.collector.handler.hub;

import ru.practicum.collector.configuration.EventPublisher;
import ru.practicum.collector.handler.HubEventHandler;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

public abstract class BaseHubEventHandler implements HubEventHandler {
    protected final EventPublisher publisher;

    public BaseHubEventHandler(EventPublisher publisher) {
        this.publisher = publisher;
    }

    protected abstract HubEventAvro mapToAvro(HubEventProto eventProto);
}
