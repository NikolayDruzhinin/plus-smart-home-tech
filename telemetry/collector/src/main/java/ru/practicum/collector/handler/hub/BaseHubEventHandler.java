package ru.practicum.collector.handler.hub;

import ru.practicum.collector.configuration.EventPublisher;
import ru.practicum.collector.handler.HubEventHandler;
import ru.practicum.collector.model.hub.HubEvent;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

public abstract class BaseHubEventHandler implements HubEventHandler {
    protected final EventPublisher publisher;

    public BaseHubEventHandler(EventPublisher publisher) {
        this.publisher = publisher;
    }

    protected abstract HubEventAvro mapToAvro(HubEvent event);
}
