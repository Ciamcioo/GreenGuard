package com.greenguard.green_guard_application.service;

import com.greenguard.green_guard_application.model.dto.SensorDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.List;

public interface SensorService {

    List<SensorDTO> getSensors(@NotNull String username);

    SensorDTO getSensor(@NotNull String username, String name);

    String addSensor(@NotNull String ownerUsername, @NotNull SensorDTO sensorDTO);

    void deleteSensor(String ownerUsername, String name);

    void updateSensorName(String ownerUsername, String name, String newName);

}
