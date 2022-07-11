package com.example.application.data.service;

import com.example.application.data.entity.BrakeCondition;
import com.example.application.data.entity.Car;
import com.example.application.data.entity.GearPosition;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class CarSimulator implements Runnable{
    private enum Change {
        INCREASE, DECREASE, NONE
    }
    private List<Integer> speedChange;
    private List<Change> change;
    private Car car;
    int currentSpeed = 0;
    BrakeCondition.Condition brakeCondition;
    GearPosition.Position currentGear = GearPosition.Position._1;
    private CarService carService;
    List<Integer> brakeAmount = new ArrayList<>();

    @Autowired
    Environment env;


    public CarSimulator(Car car, CarService carService) {
        this.car = car;
        this.carService = carService;
        speedChange = new ArrayList<>(List.of(0, 1, 1, 1, 2, 2, 2, 3, 3, 3, 5, 5, 5));
        currentSpeed = 0;
        change = new ArrayList<>(List.of(Change.INCREASE,Change.INCREASE, Change.DECREASE, Change.DECREASE, Change.NONE));
    }

    @Override
    public void run() {
        if (env != null && env.getActiveProfiles()[0] == "test")
            return;
        carService.saveSpeed(car, currentSpeed, currentSpeed);
        while (true) {
            simulateSpeedChange();
        }

    }

    private void simulateSpeedChange() {
        Collections.shuffle(speedChange);

        // SPEED CHANGE
        int speedChange = this.speedChange.get(0);
        Change change;
        if (currentSpeed == 0) {
            change = Change.INCREASE;
        } else if (currentSpeed >= 350) {
            change = Change.DECREASE;
        } else {
            Collections.shuffle(this.change);
            change = this.change.get(0);
        }

        if (change == Change.INCREASE) {
            int oldSpeed = currentSpeed;
            currentSpeed += speedChange;
            if (currentSpeed >= 350) currentSpeed = 350;
            carService.saveSpeed(car, currentSpeed, oldSpeed);
        } else if (change == Change.DECREASE) {
            int oldSpeed = currentSpeed;
            currentSpeed -= speedChange;
            if (currentSpeed <= -10) currentSpeed = -10;
            carService.saveSpeed(car, currentSpeed, oldSpeed);

            // lets assume decelerate means breaking
            brakeAmount.add(speedChange);
            while (brakeAmount.size() >= 1000) {
                brakeAmount.remove(0);
            }

            int brakeAmountIn1Sec = brakeAmount.stream()
                    .reduce(0, (integer, integer2) -> integer + integer2)
                    .intValue();

            BrakeCondition.Condition oldCondition = brakeCondition;

            if (brakeAmountIn1Sec < 2000) {
                brakeCondition = BrakeCondition.Condition.COLD;
            }
            if (brakeAmountIn1Sec >= 2000 && brakeAmountIn1Sec < 2300) {
                brakeCondition = BrakeCondition.Condition.WARM;
            }
            if (brakeAmountIn1Sec > 2300) {
                brakeCondition = BrakeCondition.Condition.HOT;
            }

            carService.saveBrakeCondition(car, brakeCondition, oldCondition, brakeAmountIn1Sec);
        }

        // GEAR POS
        GearPosition.Position oldPosition = currentGear;

        if (currentSpeed == 0) {
            currentGear = GearPosition.Position._0;
        }
        if (currentSpeed > 0 && currentSpeed <= 30) {
            currentGear = GearPosition.Position._1;
        }
        if (currentSpeed > 30 && currentSpeed <= 60) {
            currentGear = GearPosition.Position._2;
        }
        if (currentSpeed > 60 && currentSpeed <= 120) {
            currentGear = GearPosition.Position._3;
        }
        if (currentSpeed > 120 && currentSpeed <= 200) {
            currentGear = GearPosition.Position._4;
        }
        if (currentSpeed > 200) {
            currentGear = GearPosition.Position._5;
        }
        if (currentSpeed < 0) {
            currentGear = GearPosition.Position._6;
        }

        if (oldPosition != currentGear) {
            carService.saveGearPosition(car, currentGear, oldPosition);
        }

        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
