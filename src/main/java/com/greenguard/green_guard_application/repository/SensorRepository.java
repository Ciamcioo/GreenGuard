package com.greenguard.green_guard_application.repository;

import com.greenguard.green_guard_application.model.entity.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, UUID> {

    Optional<Sensor> findSensorByName(String name);
}
