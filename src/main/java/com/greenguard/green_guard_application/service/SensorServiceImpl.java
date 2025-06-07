package com.greenguard.green_guard_application.service;

import com.greenguard.green_guard_application.aspect.annotation.EnableMethodCallLog;
import com.greenguard.green_guard_application.aspect.annotation.EnableMethodLog;
import com.greenguard.green_guard_application.model.dto.SensorDTO;
import com.greenguard.green_guard_application.model.entity.Location;
import com.greenguard.green_guard_application.model.entity.Sensor;
import com.greenguard.green_guard_application.model.entity.User;
import com.greenguard.green_guard_application.repository.LocationRepository;
import com.greenguard.green_guard_application.repository.SensorRepository;
import com.greenguard.green_guard_application.repository.UserRepository;
import com.greenguard.green_guard_application.service.exception.DefaultLocationException;
import com.greenguard.green_guard_application.service.exception.SensorAlreadyExistsException;
import com.greenguard.green_guard_application.sensor.SensorRunner;

import com.greenguard.green_guard_application.service.exception.SensorNotFoundException;
import com.greenguard.green_guard_application.service.exception.UserNotFoundException;
import com.greenguard.green_guard_application.service.mapper.SensorMapper;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@Validated
public class SensorServiceImpl implements SensorService {
    @Value("${default.location.name}")
    private static String DEFAULT_LOCATION_NAME;

    private final SensorRepository sensorRepository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
    private final SensorMapper sensorMapper;
    private final SensorRunner sensorRunner;

    @Autowired
    public SensorServiceImpl(SensorRepository sensorRepository, LocationRepository locationRepository, UserRepository userRepository, SensorMapper sensorMapper, SensorRunner sensorRunner) {
        this.sensorRepository = sensorRepository;
        this.locationRepository = locationRepository;
        this.userRepository = userRepository;
        this.sensorMapper = sensorMapper;
        this.sensorRunner = sensorRunner;
    }

    @Override
    public List<SensorDTO> getSensors(String username) {
        User targetUser = userRepository.findById(username)
                .orElseThrow(() -> new UserNotFoundException("User with provided username not found"));
        List<Sensor> userSensors = sensorRepository.findSensorByUser(targetUser);

        return userSensors.stream().map(sensorMapper::toDTO).toList();
    }

    @Override
    @EnableMethodLog
    public SensorDTO getSensor(@NotNull String username, String name) {
        Sensor sensor = sensorRepository.findSensorByUsernameAndName(username, name)
                                        .orElseThrow(() -> new SensorNotFoundException(name));

        return sensorMapper.toDTO(sensor);
    }

    @Override
    @EnableMethodLog
    public String addSensor(@NotNull String ownerUsername, @NotNull SensorDTO sensorDTO) throws DefaultLocationException {
        Optional<Sensor> searchSensorResult = sensorRepository.findSensorByIpAddress(sensorDTO.ipAddress());

        if (searchSensorResult.isPresent()) {
            throw new SensorAlreadyExistsException();
        }

        Sensor sensorToPersist = sensorMapper.toEntity(sensorDTO);

        User sensorOwner = userRepository.findById(ownerUsername)
                                         .orElseThrow(() -> new UserNotFoundException("User with provided username does not exists in the database of users."));
        sensorToPersist.setUser(sensorOwner);

        Optional<Location> sensorLocationOpt = locationRepository.findById(sensorDTO.locationName());
        if (sensorLocationOpt.isEmpty()) {
            sensorLocationOpt = locationRepository.findById(DEFAULT_LOCATION_NAME);
            if (sensorLocationOpt.isEmpty()) {
                throw new DefaultLocationException("Default Location object couldn't be loaded");
            }
        }

        Location locationForSensor = sensorLocationOpt.get();
        sensorToPersist.setLocation(locationForSensor);

        sensorRepository.save(sensorToPersist);

        if(sensorToPersist.getActive() == true) {
            sensorRunner.addActiveSensor(sensorToPersist);
        }

        return sensorDTO.ipAddress();
    }

    @Override
    @EnableMethodCallLog
    public void deleteSensor(String ownerUsername, String name) {
        Optional<Sensor> sensorOpt  = sensorRepository.findSensorByUsernameAndName(ownerUsername, name);

        if (sensorOpt.isEmpty()) {
            throw new SensorNotFoundException(name);
        }

        Sensor sensor = sensorOpt.get();

        if(sensor.getActive() == true) {
            sensorRunner.deleteActiveSensor(sensor);
        }


        sensorRepository.delete(sensor);
    }

    @Override
    @EnableMethodCallLog
    public void updateSensorName(String ownerUsername, String name, String newName) {
        Optional<Sensor> sensor  = sensorRepository.findSensorByUsernameAndName(ownerUsername, name);

        if (sensor.isEmpty()) {
            throw new SensorNotFoundException(name);
        }

        sensorRepository.updateSensorName(name, newName);
    }
}
