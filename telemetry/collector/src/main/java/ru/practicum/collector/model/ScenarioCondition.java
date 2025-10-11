package ru.practicum.collector.model;

public record ScenarioCondition(String sensorId, ScenarioType type, OperationType operation, Integer value) {
}
