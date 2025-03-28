package com.greenguard.green_guard_application.service;

import com.greenguard.green_guard_application.aspect.annotation.EnableMethodLog;
import com.greenguard.green_guard_application.model.dto.SensorDTO;
import com.greenguard.green_guard_application.model.entity.Sensor;
import com.greenguard.green_guard_application.repository.SensorRepository;
import com.greenguard.green_guard_application.service.exception.SensorNotFoundException;
import com.greenguard.green_guard_application.service.mapper.SensorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
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

    private boolean validateIsNotNull(Object object) {
        return object != null;
    }
}
