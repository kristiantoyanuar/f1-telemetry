package com.example.application.data.service;

import com.example.application.data.entity.BrakeCondition;
import com.example.application.data.entity.Car;
import com.example.application.data.entity.GearPosition;
import com.example.application.data.entity.Speed;
import com.example.application.security.AuthenticatedUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CarService extends BaseService<Car, UUID> {
    private SpeedRepository speedRepository;
    private GearPositionRepository gearPositionRepository;
    private BrakeConditionRepository brakeConditionRepository;

    public CarService(CarRepository repository,
                      SpeedRepository speedRepository,
                      GearPositionRepository gearPositionRepository,
                      BrakeConditionRepository brakeConditionRepository) {
        super(repository);
        this.speedRepository = speedRepository;
        this.gearPositionRepository = gearPositionRepository;
        this.brakeConditionRepository = brakeConditionRepository;
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
    public void saveSpeed(Car car, int speedValue) {
        Speed carSpeed = new Speed();
        carSpeed.setSpeed(speedValue);
        carSpeed.setCar(car);
        carSpeed.setTimestamp(new Date());

        speedRepository.save(carSpeed);
    }

    @Transactional
    public void saveGearPosition(Car car, GearPosition.Position position) {
        GearPosition gearPosition = new GearPosition();
        gearPosition.setPosition(position);
        gearPosition.setCar(car);
        gearPosition.setTimestamp(new Date());

        gearPositionRepository.save(gearPosition);
    }

    @Transactional
    public void saveBrakeCondition(Car car, BrakeCondition.Condition brakeCondition) {
        BrakeCondition condition = new BrakeCondition();
        condition.setCondition(brakeCondition);
        condition.setCar(car);
        condition.setTimestamp(new Date());

        brakeConditionRepository.save(condition);
    }
}
