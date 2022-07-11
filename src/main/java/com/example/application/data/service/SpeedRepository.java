package com.example.application.data.service;

import com.example.application.data.entity.Speed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface SpeedRepository extends JpaRepository<Speed, UUID> {

    @Query(value = """
            SELECT s.* FROM SPEED s WHERE s.timestamp BETWEEN
            (SELECT (s1.timestamp - INTERVAL '5 MINUTES') FROM SPEED s1 ORDER BY s1.timestamp DESC LIMIT 1)
            AND
            (SELECT (s1.timestamp) FROM SPEED s1 ORDER BY s1.timestamp DESC LIMIT 1)
            AND s.car_id = :carId
            """,
            nativeQuery = true)
    List<Speed> findLast5MinutesWithoutLimit(@Param("carId") String carId);

    @Query(value = """
            SELECT s.* FROM SPEED s WHERE s.timestamp BETWEEN
            (SELECT (s1.timestamp - INTERVAL '5 MINUTES') FROM SPEED s1 ORDER BY s1.timestamp DESC LIMIT 1)
            AND
            (SELECT (s1.timestamp) FROM SPEED s1 ORDER BY s1.timestamp DESC LIMIT 1)
            AND s.car_id = :carId
            AND s.timestamp >= NOW() - INTERVAL '2 HOURS'
            """,
            nativeQuery = true)
    List<Speed> findLast5MinutesWithLimit(@Param("carId") String carId);

}
