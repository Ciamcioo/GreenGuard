package com.greenguard.green_guard_application.service;

import com.greenguard.green_guard_application.model.dto.SensorDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

public interface SensorService {

    SensorDTO getSensor(String name);

    String addSensor(@NotNull SensorDTO sensorDTO);

    void deleteSensor(String name);
}
