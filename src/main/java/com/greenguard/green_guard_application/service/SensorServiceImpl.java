package com.greenguard.green_guard_application.service;

import com.greenguard.green_guard_application.aspect.annotation.EnableMethodCallLog;
import com.greenguard.green_guard_application.aspect.annotation.EnableMethodLog;
import com.greenguard.green_guard_application.model.dto.SensorDTO;
import com.greenguard.green_guard_application.model.entity.Sensor;
import com.greenguard.green_guard_application.repository.SensorRepository;
import com.greenguard.green_guard_application.service.exception.SensorAlreadyExistsException;
import com.greenguard.green_guard_application.service.exception.SensorNotFoundException;
import com.greenguard.green_guard_application.service.mapper.SensorMapper;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Service
@Validated
public class SensorServiceImpl implements SensorService {
    private final SensorRepository sensorRepository;
    private final SensorMapper sensorMapper;


    @Autowired
    public SensorServiceImpl(SensorRepository sensorRepository, SensorMapper sensorMapper) {
        this.sensorRepository = sensorRepository;
        this.sensorMapper = sensorMapper;
    }

    @Override
    @EnableMethodLog
    public SensorDTO getSensor(String name) {
        Sensor sensor = sensorRepository.findSensorByName(name)
                                        .orElseThrow(() -> new SensorNotFoundException(name));

        return sensorMapper.toDTO(sensor);
    }

    @Override
    @EnableMethodLog
    public String addSensor(@NotNull SensorDTO sensorDTO) {
        Optional<Sensor> searchSensorResult = sensorRepository.findSensorByIpAddress(sensorDTO.ipAddress());

        if (searchSensorResult.isPresent()) {
            throw new SensorAlreadyExistsException();
        }

        Sensor sensorToPersist = sensorMapper.toEntity(sensorDTO);
        sensorRepository.save(sensorToPersist);

        return sensorDTO.ipAddress();
    }

    @Override
    @EnableMethodCallLog
    public void deleteSensor(String name) {
        Optional<Sensor> sensor  = sensorRepository.findSensorByName(name);

        if (!sensor.isPresent()) {
            throw new SensorNotFoundException(name);
        }

        sensorRepository.delete(sensor.get());
    }

    @Override
    @EnableMethodCallLog
    public void updateSensorName(String name, String newName) {
        Optional<Sensor> sensor  = sensorRepository.findSensorByName(name);

        if (!sensor.isPresent()) {
            throw new SensorNotFoundException(name);
        }

        sensorRepository.updateSensorName(name, newName);
    }

    private boolean validateIsNotNull(Object object) {
        return object != null;
    }
}
