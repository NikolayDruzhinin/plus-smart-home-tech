package ru.practicum.collector.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.collector.handler.HubEventHandler;
import ru.practicum.collector.handler.SensorEventHandler;
import ru.practicum.collector.model.hub.HubEvent;
import ru.practicum.collector.model.hub.HubEventType;
import ru.practicum.collector.model.sensor.SensorEvent;
import ru.practicum.collector.model.sensor.SensorEventType;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/events")
@Slf4j
public class EventsController {
    private final Map<SensorEventType, SensorEventHandler> sensorEvents;
    private final Map<HubEventType, HubEventHandler> hubEvents;

    public EventsController(Set<SensorEventHandler> sensorEvents, Set<HubEventHandler> hubEvents) {
        this.sensorEvents = sensorEvents.stream()
                .collect(Collectors.toMap(SensorEventHandler::getMessageType, Function.identity()));
        this.hubEvents = hubEvents.stream()
                .collect(Collectors.toMap(HubEventHandler::getMessageType, Function.identity()));
    }

    @PostMapping("/sensors")

    public void collectSensorEvent(@Valid @RequestBody SensorEvent sensorEvent) {
        log.info("handle sensor event: {}", sensorEvent.toString());
        SensorEventHandler handler = sensorEvents.get(sensorEvent.getType());
        if (handler == null) {
            throw new IllegalArgumentException("Can't find handler for event " + sensorEvent.getType());
        }
        handler.handle(sensorEvent);
    }

    @PostMapping("/hubs")
    public void collectHubEvent(@Valid @RequestBody HubEvent hubEvent) {
        log.info("handle hub event: {}", hubEvent.toString());
        HubEventHandler handler = hubEvents.get(hubEvent.getType());
        if (handler == null) {
            throw new IllegalArgumentException("Can't find handler for event " + hubEvent.getType());
        }
        handler.handle(hubEvent);
    }
}
