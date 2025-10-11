package ru.practicum.collector.model.sensor;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class LightSensorEvent extends SensorEvent {
    @PositiveOrZero
    private Integer linkQuality;

    @PositiveOrZero
    private Integer luminosity;

    @Override
    public SensorEventType getType() {
        return SensorEventType.LIGHT_SENSOR_EVENT;
    }
}
