package com.example.application.data.service;

import com.example.application.data.entity.Car;
import com.example.application.data.entity.Speed;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Date;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class SpeedRepositoryTest {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private SpeedRepository speedRepository;

    @org.junit.jupiter.api.Test
    void findLast5MinutesWithoutLimit_under2HoursData() {
        Car mycar = new Car();
        mycar.setName("mycar");
        carRepository.save(mycar);

        Speed now = new Speed();
        now.setCar(mycar);
        now.setSpeed(90);
        now.setTimestamp(new Date());

        Speed nMinus4Mins = new Speed();
        nMinus4Mins.setCar(mycar);
        nMinus4Mins.setSpeed(50);
        nMinus4Mins.setTimestamp(DateUtils.addMinutes(now.getTimestamp(), -4));


        Speed nMinus8Mins = new Speed();
        nMinus8Mins.setCar(mycar);
        nMinus8Mins.setSpeed(50);
        nMinus8Mins.setTimestamp(DateUtils.addMinutes(now.getTimestamp(), -8));


        speedRepository.save(now);
        speedRepository.save(nMinus4Mins);
        speedRepository.save(nMinus8Mins);

        List<Speed> findLast5mins = speedRepository.findLast5MinutesWithoutLimit(mycar.getId().toString());

        Assertions.assertEquals(2, findLast5mins.size());
    }

    @org.junit.jupiter.api.Test
    void findLast5MinutesWithoutLimit_after2HoursData() {
        Car mycar = new Car();
        mycar.setName("mycar");
        carRepository.save(mycar);

        Date nowDate = new Date();

        Speed now = new Speed();
        now.setCar(mycar);
        now.setSpeed(90);
        now.setTimestamp(DateUtils.addMinutes(nowDate, -120));

        Speed nMinus4Mins = new Speed();
        nMinus4Mins.setCar(mycar);
        nMinus4Mins.setSpeed(50);
        nMinus4Mins.setTimestamp(DateUtils.addMinutes(nowDate, -124));


        Speed nMinus8Mins = new Speed();
        nMinus8Mins.setCar(mycar);
        nMinus8Mins.setSpeed(50);
        nMinus8Mins.setTimestamp(DateUtils.addMinutes(nowDate, -128));


        speedRepository.save(now);
        speedRepository.save(nMinus4Mins);
        speedRepository.save(nMinus8Mins);

        List<Speed> findLast5mins = speedRepository.findLast5MinutesWithoutLimit(mycar.getId().toString());

        Assertions.assertEquals(2, findLast5mins.size());
    }



    @org.junit.jupiter.api.Test
    void findLast5MinutesWithLimit_under2HoursData() {
        Car mycar = new Car();
        mycar.setName("mycar");
        carRepository.save(mycar);

        Speed now = new Speed();
        now.setCar(mycar);
        now.setSpeed(90);
        now.setTimestamp(new Date());

        Speed nMinus4Mins = new Speed();
        nMinus4Mins.setCar(mycar);
        nMinus4Mins.setSpeed(50);
        nMinus4Mins.setTimestamp(DateUtils.addMinutes(now.getTimestamp(), -4));


        Speed nMinus8Mins = new Speed();
        nMinus8Mins.setCar(mycar);
        nMinus8Mins.setSpeed(50);
        nMinus8Mins.setTimestamp(DateUtils.addMinutes(now.getTimestamp(), -8));


        speedRepository.save(now);
        speedRepository.save(nMinus4Mins);
        speedRepository.save(nMinus8Mins);

        List<Speed> findLast5mins = speedRepository.findLast5MinutesWithLimit(mycar.getId().toString());

        Assertions.assertEquals(2, findLast5mins.size());
    }

    @org.junit.jupiter.api.Test
    void findLast5MinutesWithLimit_after2HoursData() {
        Car mycar = new Car();
        mycar.setName("mycar");
        carRepository.save(mycar);

        Date nowDate = new Date();

        Speed now = new Speed();
        now.setCar(mycar);
        now.setSpeed(90);
        now.setTimestamp(DateUtils.addMinutes(nowDate, -120));

        Speed nMinus4Mins = new Speed();
        nMinus4Mins.setCar(mycar);
        nMinus4Mins.setSpeed(50);
        nMinus4Mins.setTimestamp(DateUtils.addMinutes(nowDate, -124));


        Speed nMinus8Mins = new Speed();
        nMinus8Mins.setCar(mycar);
        nMinus8Mins.setSpeed(50);
        nMinus8Mins.setTimestamp(DateUtils.addMinutes(nowDate, -128));


        speedRepository.save(now);
        speedRepository.save(nMinus4Mins);
        speedRepository.save(nMinus8Mins);

        List<Speed> findLast5mins = speedRepository.findLast5MinutesWithLimit(mycar.getId().toString());

        Assertions.assertEquals(0, findLast5mins.size());
    }
}