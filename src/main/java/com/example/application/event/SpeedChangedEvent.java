package com.example.application.event;

import com.example.application.data.entity.Car;
import com.example.application.data.entity.Speed;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SpeedChangedEvent extends ApplicationEvent {
    private final Speed carSpeed;
    private Car car;
    private int oldSpeed;
    private int newSpeed;
    public SpeedChangedEvent(Object source, Car car, int newSpeed, int oldSpeed, Speed carSpeed) {
        super(source);
        this.car = car;
        this.newSpeed = newSpeed;
        this.oldSpeed = oldSpeed;
        this.carSpeed = carSpeed;
    }
}
