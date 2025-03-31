package com.greenguard.green_guard_application.service;

import com.greenguard.green_guard_application.model.dto.SensorDTO;
import com.greenguard.green_guard_application.model.entity.Sensor;
import com.greenguard.green_guard_application.repository.SensorRepository;

import com.greenguard.green_guard_application.service.exception.SensorAlreadyExistsException;
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

    private SensorRepository sensorRepository;
    private SensorMapper sensorMapper;
    private SensorService sensorService;

    private Sensor testSensor;
    private SensorDTO testSensorDTO;


    @BeforeEach
    void setup() {
        testSensor = new Sensor(TEST_SENSOR_ID,
                                TEST_SENSOR_NAME,
                                TEST_SENSOR_IP,
                                TEST_SENSOR_MAC,
                                TEST_SENSOR_ACTIVE);

        testSensorDTO = new SensorDTO(TEST_SENSOR_NAME,
                                      TEST_SENSOR_IP,
                                      TEST_SENSOR_ACTIVE);

        sensorRepository = mock(SensorRepository.class);
        when(sensorRepository.findSensorByName(TEST_SENSOR_NAME)).thenReturn(Optional.of(testSensor));

        sensorMapper = mock(SensorMapper.class);
        when(sensorMapper.toDTO(testSensor)).thenReturn(testSensorDTO);
        when(sensorMapper.toEntity(testSensorDTO)).thenReturn(testSensor);

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

    @Test
    @DisplayName("Method addSensor() should not return null value")
    void  addSensorShouldNotReturnNullValue() {
        assertNotNull(sensorService.addSensor(testSensorDTO));
    }

    @Test
    @DisplayName("Return value of addSensor() should be an IP address of passed sensorDTO")
    void addSensorShouldReturnIPAddressOfPassedSensorDTO() {
        assertEquals(testSensorDTO.ipAddress(), sensorService.addSensor(testSensorDTO));
    }

    @Test
    @DisplayName("Test sensor should throw SensorAlreadyExists() if sensorDTO with provided ip address already exists in database")
    void addSensorShouldThrowSensorAlreadyExists() {
        String ipAddressDuplicate = TEST_SENSOR_IP;

        when(sensorRepository.findSensorByIpAddress(ipAddressDuplicate)).thenReturn(Optional.of(testSensor));

        assertThrows(SensorAlreadyExistsException.class, () -> sensorService.addSensor(testSensorDTO));
        verify(sensorRepository).findSensorByIpAddress(ipAddressDuplicate);
    }

    @Test
    @DisplayName("Method addSensor() should make a call to repository to save the sensor to database")
    void addSensorShouldSaveProvidedData() {
        when(sensorRepository.findSensorByIpAddress(testSensorDTO.name())).thenReturn(Optional.of(testSensor));

        sensorService.addSensor(testSensorDTO);

        verify(sensorRepository).save(testSensor);
    }






}
