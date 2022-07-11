package com.example.application.event;

import com.example.application.data.entity.Car;
import com.example.application.data.entity.GearPosition;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class GearPositionChangedEvent extends ApplicationEvent {
    private Car car;
    GearPosition.Position oldGearPosition;
    GearPosition.Position newGearPosition;
    public GearPositionChangedEvent(Object source, Car car,
                                    GearPosition.Position oldGearPosition,
                                    GearPosition.Position newGearPosition) {
        super(source);
        this.car = car;
        this.oldGearPosition = oldGearPosition;
        this.newGearPosition = newGearPosition;
    }
}
