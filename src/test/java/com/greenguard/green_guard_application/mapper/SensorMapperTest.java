package com.greenguard.green_guard_application.mapper;

import com.greenguard.green_guard_application.model.dto.SensorDTO;
import com.greenguard.green_guard_application.model.entity.Sensor;
import com.greenguard.green_guard_application.model.entity.Location;
import com.greenguard.green_guard_application.model.entity.User;
import com.greenguard.green_guard_application.service.mapper.LocationMapper;
import com.greenguard.green_guard_application.service.mapper.SensorMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class SensorMapperTest {

    private static final UUID     TEST_SENSOR_ID            = UUID.randomUUID();
    private static final String   TEST_SENSOR_NAME          = "Foo";
    private static final String   TEST_SENSOR_USERNAME      = "John";
    private static final User     TEST_SENSOR_USER          = new User(TEST_SENSOR_USERNAME, "password");
    private static final String   TEST_SENSOR_IP            = "192.168.1.10";
    private static final String   TEST_SENSOR_MAC           = "DE:AD:BE:EF:12:34";
    private static final String   TEST_SENSOR_LOCATION_NAME = "Location";
    private static final Boolean  TEST_SENSOR_ACTIVE        = false;

    private Sensor test_sensor_entity;
    private SensorDTO test_sensor_dto;

    private final SensorMapper sensorMapper = Mappers.getMapper(SensorMapper.class);

    @BeforeEach
    void setup() {
        test_sensor_entity = new Sensor(TEST_SENSOR_ID,
                                        TEST_SENSOR_NAME,
                                        TEST_SENSOR_USER,
                                        TEST_SENSOR_IP,
                                        TEST_SENSOR_MAC,
                                        new Location(TEST_SENSOR_LOCATION_NAME),
                                        TEST_SENSOR_ACTIVE);

        test_sensor_dto = new SensorDTO(TEST_SENSOR_NAME,
                                        TEST_SENSOR_IP,
                                        TEST_SENSOR_LOCATION_NAME,
                                        TEST_SENSOR_ACTIVE);
    }

    @Test
    @DisplayName("Result sensorDTO after mapping should have fields with the same values as sensor entity")
    void mapSensorEntityToSensorDTO() {
        SensorDTO resultSensorDTO = sensorMapper.toDTO(test_sensor_entity);

        assertAll(
                () -> assertEquals(TEST_SENSOR_NAME, resultSensorDTO.name()),
                () -> assertEquals(TEST_SENSOR_IP, resultSensorDTO.ipAddress()),
                () -> assertEquals(TEST_SENSOR_LOCATION_NAME, resultSensorDTO.locationName()),
                () -> assertEquals(TEST_SENSOR_ACTIVE, resultSensorDTO.active())
        );
    }

    @Test
    @DisplayName("Result sensor entity after mapping should have fields with the same value as sensorDTO")
    void mapSensorDTOToSensorEntity() {
        Sensor resultSensor = sensorMapper.toEntity(test_sensor_dto);

        assertAll(
                () -> assertNull(resultSensor.getId()),
                () -> assertEquals(TEST_SENSOR_NAME, resultSensor.getName()),
                () -> assertNull(resultSensor.getUser()),
                () -> assertEquals(TEST_SENSOR_IP, resultSensor.getIpAddress()),
                () -> assertNull(resultSensor.getMacAddress()),
                () -> assertEquals(TEST_SENSOR_ACTIVE, resultSensor.getActive()),
                () -> assertNull(resultSensor.getLocation()),
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
























