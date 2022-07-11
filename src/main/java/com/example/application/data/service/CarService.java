package com.example.application.data.service;

import com.example.application.data.entity.BrakeCondition;
import com.example.application.data.entity.Car;
import com.example.application.data.entity.GearPosition;
import com.example.application.data.entity.Speed;
import com.example.application.event.BrakeConditionChangedEvent;
import com.example.application.event.GearPositionChangedEvent;
import com.example.application.event.SpeedChangedEvent;
import com.example.application.security.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CarService extends BaseService<Car, UUID> {
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private SpeedRepository speedRepository;
    @Autowired
    private GearPositionRepository gearPositionRepository;
    @Autowired
    private BrakeConditionRepository brakeConditionRepository;

    public CarService(CarRepository repository) {
        super(repository);
    }

    @Transactional
    public List<Speed> getSpeed(AuthenticatedUser authenticatedUser, Car car) {
        final List<Speed> result = new ArrayList<>();
        authenticatedUser.get().ifPresentOrElse(
                user -> result.addAll(speedRepository.findLast5MinutesWithoutLimit(car.getId().toString())),
                () -> result.addAll(speedRepository.findLast5MinutesWithLimit(car.getId().toString())));
        return result;
    }

    @Transactional
    public Speed saveSpeed(Car car, int newSpeed, int oldSpeed) {
        Speed carSpeed = new Speed();
        carSpeed.setSpeed(newSpeed);
        carSpeed.setCar(car);
        carSpeed.setTimestamp(new Date());

        speedRepository.save(carSpeed);

        applicationContext.publishEvent(new SpeedChangedEvent(this, car, newSpeed, oldSpeed, carSpeed));

        return carSpeed;
    }

    @Transactional
    public void saveGearPosition(Car car, GearPosition.Position newPosition, GearPosition.Position oldPosition) {
        GearPosition gearPosition = new GearPosition();
        gearPosition.setPosition(newPosition);
        gearPosition.setCar(car);
        gearPosition.setTimestamp(new Date());

        gearPositionRepository.save(gearPosition);

        applicationContext.publishEvent(new GearPositionChangedEvent(this, car, newPosition, oldPosition));
    }

    @Transactional
    public void saveBrakeCondition(Car car, BrakeCondition.Condition brakeCondition, BrakeCondition.Condition oldCondition, int brakeAmountIn1Sec) {
        BrakeCondition condition = new BrakeCondition();
        condition.setCondition(brakeCondition);
        condition.setCar(car);
        condition.setTimestamp(new Date());

        brakeConditionRepository.save(condition);

        applicationContext.publishEvent(new BrakeConditionChangedEvent(this, car, brakeCondition, oldCondition, brakeAmountIn1Sec));
    }
}
