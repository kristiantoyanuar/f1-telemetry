package com.example.application.event;

import com.example.application.data.entity.BrakeCondition;
import com.example.application.data.entity.Car;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class BrakeConditionChangedEvent extends ApplicationEvent {
    private Car car;
    BrakeCondition.Condition oldCondition;
    BrakeCondition.Condition newCondition;
    int brakeAmountIn1Sec;
    public BrakeConditionChangedEvent(Object source, Car car,
                                      BrakeCondition.Condition oldCondition,
                                      BrakeCondition.Condition newCondition, int brakeAmountIn1Sec) {
        super(source);
        this.car = car;
        this.oldCondition = oldCondition;
        this.newCondition = newCondition;
        this.brakeAmountIn1Sec = brakeAmountIn1Sec;
    }
}
