package com.greenguard.green_guard_application.service;

import com.greenguard.green_guard_application.model.dto.SensorDTO;
import com.greenguard.green_guard_application.model.entity.Sensor;
import com.greenguard.green_guard_application.repository.SensorRepository;

import com.greenguard.green_guard_application.service.exception.SensorNotFoundException;
import com.greenguard.green_guard_application.service.mapper.SensorMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class SensorServiceTest {

    private static final UUID    TEST_SENSOR_ID           = UUID.randomUUID();
    private static final String  TEST_SENSOR_NAME         = "Foo";
    private static final String  TEST_SENSOR_IP           = "192.168.1.10";
    private static final String  TEST_SENSOR_MAC          = "DE:AD:BE:EF:12:34";
    private static final Boolean TEST_SENSOR_ACTIVE       = false;

    private static final String SENSOR_NOT_FOUND_MESSAGE = String.format("Sensor with name: %s not found!", TEST_SENSOR_NAME);


    private SensorService sensorService;
    private SensorRepository sensorRepository;
    private SensorMapper sensorMapper;
    private Sensor test_sensor;
    private SensorDTO test_sensor_dto;


    @BeforeEach
    void setup() {
        test_sensor = new Sensor(TEST_SENSOR_ID,
                                 TEST_SENSOR_NAME,
                                 TEST_SENSOR_IP,
                                 TEST_SENSOR_MAC,
                                 TEST_SENSOR_ACTIVE);

        test_sensor_dto = new SensorDTO(TEST_SENSOR_NAME,
                                        TEST_SENSOR_IP,
                                        TEST_SENSOR_MAC,
                                        TEST_SENSOR_ACTIVE);

        sensorRepository = mock(SensorRepository.class);
        when(sensorRepository.findSensorByName(TEST_SENSOR_NAME)).thenReturn(Optional.of(test_sensor));

        sensorMapper = mock(SensorMapper.class);
        when(sensorMapper.toDTO(test_sensor)).thenReturn(test_sensor_dto);
        when(sensorMapper.toEntity(test_sensor_dto)).thenReturn(test_sensor);

        sensorService = new SensorServiceImpl(sensorRepository, sensorMapper);

    }

    @Test
    @DisplayName("Null should not be returned by getSensor() method.")
    void getSensorShouldNotReturnNull() {
        assertNotNull(sensorService.getSensor(TEST_SENSOR_NAME));
    }

    @Test
    @DisplayName("Returned SensorDTO name should be equal to argument.")
    void getSensorShouldReturnSensorDTOWithNameEqualToArgument() {
        assertEquals(TEST_SENSOR_NAME, sensorService.getSensor(TEST_SENSOR_NAME).name());
    }

    @Test
    @DisplayName("Method getSensor() should make a call to sensor repository passing the argument name.")
    void getSensorShouldMakeCallToSensorRepository() {
        sensorService.getSensor(TEST_SENSOR_NAME);

        verify(sensorRepository).findSensorByName(TEST_SENSOR_NAME);
    }

    @Test
    @DisplayName("Throw SensorNotFoundException() when sensorRepository returns empty Optional in getSensor() method with appropriate message.")
    void getSensorThrowSensorNotFoundException() {
        when(sensorRepository.findSensorByName(TEST_SENSOR_NAME)).thenReturn(Optional.empty());

        Exception exception = assertThrows(SensorNotFoundException.class, () -> sensorService.getSensor(TEST_SENSOR_NAME));
        assertEquals(SENSOR_NOT_FOUND_MESSAGE, exception.getMessage());
    }






}
