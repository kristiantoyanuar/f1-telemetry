package com.example.application.data.service;

import com.example.application.data.entity.Car;
import com.example.application.data.entity.Speed;
import com.example.application.security.AuthenticatedUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CarService extends BaseService<Car, UUID>{
    private SpeedRepository speedRepository;
    public CarService(CarRepository repository, SpeedRepository speedRepository) {
        super(repository);
        this.speedRepository = speedRepository;
    }

    @Transactional
    public List<Speed> getSpeed(AuthenticatedUser authenticatedUser, Car car) {
        List<Speed> result = new ArrayList<>();
        LocalTime start = LocalTime.now().minus(5, ChronoUnit.MINUTES);
        LocalTime end = LocalTime.now();
        authenticatedUser.get().ifPresentOrElse(user -> {
        }, () -> {

        });
        return result;
    }
}
