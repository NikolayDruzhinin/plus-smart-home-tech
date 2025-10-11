package ru.practicum.collector.model.hub;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.practicum.collector.model.DeviceAction;
import ru.practicum.collector.model.ScenarioCondition;

import java.util.List;

@Getter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class ScenarioAddedEvent extends HubEvent {
    @NotBlank
    private String name;

    @NotNull
    private List<ScenarioCondition> conditions;

    @NotNull
    private List<DeviceAction> actions;

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_ADDED;
    }
}
