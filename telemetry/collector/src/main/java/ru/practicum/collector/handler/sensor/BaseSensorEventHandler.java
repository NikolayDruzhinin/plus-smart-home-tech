package ru.practicum.collector.handler.sensor;

import ru.practicum.collector.configuration.EventPublisher;
import ru.practicum.collector.handler.SensorEventHandler;
import ru.practicum.collector.model.sensor.SensorEvent;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

public abstract class BaseSensorEventHandler implements SensorEventHandler {
    protected final EventPublisher publisher;

    protected BaseSensorEventHandler(EventPublisher publisher) {
        this.publisher = publisher;
    }

    protected abstract SensorEventAvro mapToAvro(SensorEvent event);
}
