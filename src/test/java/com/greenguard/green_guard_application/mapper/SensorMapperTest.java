package com.greenguard.green_guard_application.mapper;

import com.greenguard.green_guard_application.model.dto.SensorDTO;
import com.greenguard.green_guard_application.model.entity.Sensor;
import com.greenguard.green_guard_application.model.entity.Location;
import com.greenguard.green_guard_application.model.entity.User;
import com.greenguard.green_guard_application.service.mapper.SensorMapper;
import com.greenguard.green_guard_application.util.SensorBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class SensorMapperTest {

    // helper fields
    private Sensor sensorEntity;
    private SensorDTO sensorDTO;

    // tested field
    private final SensorMapper sensorMapper = Mappers.getMapper(SensorMapper.class);

    @BeforeEach
    void setup() {
        sensorEntity = SensorBuilder.getInstance().withDefaultValues().buildSensor();
        sensorDTO = SensorBuilder.getInstance().withDefaultValues().buildSensorDTO();
    }

    @Test
    @DisplayName("Result sensorDTO after mapping should have fields with the same values as sensor entity")
    void mapSensorEntityToSensorDTO() {
        SensorDTO resultSensorDTO = sensorMapper.toDTO(sensorEntity);

        assertAll(
                () -> assertEquals(SensorBuilder.DEFAULT_NAME,             resultSensorDTO.name()),
                () -> assertEquals(SensorBuilder.DEFAULT_IP_ADDRESS,       resultSensorDTO.ipAddress()),
                () -> assertEquals(SensorBuilder.DEFAULT_LOCATION_NAME,    resultSensorDTO.locationName()),
                () -> assertEquals(SensorBuilder.DEFAULT_ACTIVATION_STATE, resultSensorDTO.active())
        );
    }

    @Test
    @DisplayName("Result sensor entity after mapping should have fields with the same value as sensorDTO")
    void mapSensorDTOToSensorEntity() {
        Sensor resultSensor = sensorMapper.toEntity(sensorDTO);

        assertAll(
                () -> assertNull(resultSensor.getId()),
                () -> assertNull(resultSensor.getUser()),
                () -> assertNull(resultSensor.getMacAddress()),
                () -> assertNull(resultSensor.getLocation()),
                () -> assertEquals(SensorBuilder.DEFAULT_NAME,             resultSensor.getName()),
                () -> assertEquals(SensorBuilder.DEFAULT_IP_ADDRESS,       resultSensor.getIpAddress()),
                () -> assertEquals(SensorBuilder.DEFAULT_ACTIVATION_STATE, resultSensor.getActive()),
                () -> assertTrue(resultSensor.getReadings().isEmpty())
        );
    }

    @Test
    @DisplayName("Mapping should return null for null input")
    void mappingShouldReturnNullValueForNullInput() {
        assertNull(sensorMapper.toDTO(null));
        assertNull(sensorMapper.toEntity(null));
    }
}
























