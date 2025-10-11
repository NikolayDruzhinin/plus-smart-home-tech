package ru.practicum.collector.handler;

import ru.practicum.collector.model.hub.HubEvent;
import ru.practicum.collector.model.hub.HubEventType;

public interface HubEventHandler {
    HubEventType getMessageType();

    void handle(HubEvent event);
}
