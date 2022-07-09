package com.example.application.data.service;

import com.example.application.data.entity.BrakeCondition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BrakeConditionRepository extends JpaRepository<BrakeCondition, UUID> {
}
