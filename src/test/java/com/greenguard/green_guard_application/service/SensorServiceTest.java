package com.greenguard.green_guard_application.service;

import com.greenguard.green_guard_application.model.dto.SensorDTO;
import com.greenguard.green_guard_application.model.entity.Location;
import com.greenguard.green_guard_application.model.entity.Sensor;
import com.greenguard.green_guard_application.model.entity.User;
import com.greenguard.green_guard_application.repository.LocationRepository;
import com.greenguard.green_guard_application.repository.SensorRepository;

import com.greenguard.green_guard_application.repository.UserRepository;
import com.greenguard.green_guard_application.service.exception.DefaultLocationException;
import com.greenguard.green_guard_application.service.exception.SensorAlreadyExistsException;
import com.greenguard.green_guard_application.service.exception.SensorNotFoundException;
import com.greenguard.green_guard_application.service.exception.UserNotFoundException;
import com.greenguard.green_guard_application.service.mapper.SensorMapper;
import com.greenguard.green_guard_application.sensor.SensorRunner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(properties = "spring.profiles.active=test")
public class SensorServiceTest {

    private static final UUID     TEST_SENSOR_ID            = UUID.randomUUID();
    private static final String   TEST_SENSOR_NAME          = "Foo";
    private static final String   TEST_SENSOR_USERNAME      = "John";
    private static final User     TEST_SENSOR_USER          = new User(TEST_SENSOR_USERNAME, "password");
    private static final String   TEST_SENSOR_IP            = "192.168.1.10";
    private static final String   TEST_SENSOR_MAC           = "DE:AD:BE:EF:12:34";
    private static final String   TEST_SENSOR_LOCATION_NAME = "Location";
    private static final Location TEST_SENSOR_LOCATION      = new Location(TEST_SENSOR_LOCATION_NAME);
    private static final Boolean  TEST_SENSOR_ACTIVE        = false;

    private static final String SENSOR_NOT_FOUND_MESSAGE = String.format("Sensor with name: %s not found!", TEST_SENSOR_NAME);

    @Value("${default.location.name}")
    private static String DEFAULT_LOCATION_NAME;
    private static final Location DEFAULT_LOCATION = new Location(DEFAULT_LOCATION_NAME);

    private SensorRepository sensorRepository;
    private LocationRepository locationRepository;
    private UserRepository userRepository;
    private SensorService sensorService;
    private SensorRunner sensorRunner;

    private Sensor testSensor;
    private SensorDTO testSensorDTO;


    @BeforeEach
    void setup() {
        testSensor = new Sensor(TEST_SENSOR_ID,
                                TEST_SENSOR_NAME,
                                TEST_SENSOR_USER,
                                TEST_SENSOR_IP,
                                TEST_SENSOR_MAC,
                                TEST_SENSOR_LOCATION,
                                TEST_SENSOR_ACTIVE);

        testSensorDTO = new SensorDTO(TEST_SENSOR_NAME,
                                      TEST_SENSOR_IP,
                                      TEST_SENSOR_LOCATION_NAME,
                                      TEST_SENSOR_ACTIVE);

        sensorRepository = mock(SensorRepository.class);
        when(sensorRepository.findSensorByUsernameAndName(TEST_SENSOR_USERNAME, TEST_SENSOR_NAME)).thenReturn(Optional.of(testSensor));

        locationRepository = mock(LocationRepository.class);
        when(locationRepository.findById(TEST_SENSOR_LOCATION_NAME)).thenReturn(Optional.of(TEST_SENSOR_LOCATION));
        when(locationRepository.findById(DEFAULT_LOCATION_NAME)).thenReturn(Optional.of(DEFAULT_LOCATION));

        userRepository = mock(UserRepository.class);
        when(userRepository.findById(TEST_SENSOR_USERNAME)).thenReturn(Optional.of(TEST_SENSOR_USER));

        SensorMapper sensorMapper = mock(SensorMapper.class);
        when(sensorMapper.toDTO(testSensor)).thenReturn(testSensorDTO);
        when(sensorMapper.toEntity(testSensorDTO)).thenReturn(testSensor);

        sensorRunner = mock(SensorRunner.class);

        sensorService = new SensorServiceImpl(sensorRepository, locationRepository, userRepository, sensorMapper, sensorRunner);
    }

    @Test
    @DisplayName("Method getSensors() should not return null value")
    void getSensorsShouldNotReturnNullValue() {
        assertNotNull(sensorService.getSensors(TEST_SENSOR_USERNAME));
    }

    @Test
    @DisplayName("Method getSensors() should throw UserNotFoundException if user is not known")
    void getSensorsShouldThrowUserNotFoundException() {
        when(userRepository.findById(TEST_SENSOR_USERNAME)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> sensorService.getSensors(TEST_SENSOR_USERNAME));
    }

    @Test
    @DisplayName("Method getSensors() should return all the sensor for specified user")
    void getSensorsShouldReturnAllSensorsForTheUser() {
        List<Sensor> userSensors = List.of(testSensor, testSensor);
        List<SensorDTO> expectedSensors = List.of(testSensorDTO, testSensorDTO);

        when(userRepository.findById(TEST_SENSOR_USERNAME)).thenReturn(Optional.of(TEST_SENSOR_USER));
        when(sensorRepository.findSensorByUser(TEST_SENSOR_USER)).thenReturn(userSensors);

        assertEquals(expectedSensors, sensorService.getSensors(TEST_SENSOR_USERNAME));
    }

    @DisplayName("Null should not be returned by getSensor() method.")
    void getSensorShouldNotReturnNull() {
        assertNotNull(sensorService.getSensor(TEST_SENSOR_USERNAME, TEST_SENSOR_NAME));
    }

    @Test
    @DisplayName("Returned SensorDTO name should be equal to argument.")
    void getSensorShouldReturnSensorDTOWithNameEqualToArgument() {
        assertEquals(TEST_SENSOR_NAME, sensorService.getSensor(TEST_SENSOR_USERNAME, TEST_SENSOR_NAME).name());
    }

    @Test
    @DisplayName("Method getSensor() should make a call to sensor repository passing the argument name.")
    void getSensorShouldMakeCallToSensorRepository() {
        sensorService.getSensor(TEST_SENSOR_USERNAME, TEST_SENSOR_NAME);

        verify(sensorRepository).findSensorByUsernameAndName(TEST_SENSOR_USERNAME, TEST_SENSOR_NAME);
    }

    @Test
    @DisplayName("Throw SensorNotFoundException() when sensorRepository returns empty Optional in getSensor() method with appropriate message.")
    void getSensorThrowSensorNotFoundException() {
        when(sensorRepository.findSensorByUsernameAndName(TEST_SENSOR_USERNAME, TEST_SENSOR_NAME)).thenReturn(Optional.empty());

        Exception exception = assertThrows(SensorNotFoundException.class, () -> sensorService.getSensor(TEST_SENSOR_USERNAME, TEST_SENSOR_NAME));
        assertEquals(SENSOR_NOT_FOUND_MESSAGE, exception.getMessage());
    }

    @Test
    @DisplayName("Method addSensor() should not return null value")
    void  addSensorShouldNotReturnNullValue() {
        assertNotNull(sensorService.addSensor(TEST_SENSOR_USERNAME, testSensorDTO));
    }

    @Test
    @DisplayName("Return value of addSensor() should be an IP address of passed sensorDTO")
    void addSensorShouldReturnIPAddressOfPassedSensorDTO() {
        assertEquals(testSensorDTO.ipAddress(), sensorService.addSensor(TEST_SENSOR_USERNAME, testSensorDTO));
    }

    @Test
    @DisplayName("Test sensor should throw SensorAlreadyExists() if sensorDTO with provided ip address already exists in database")
    void addSensorShouldThrowSensorAlreadyExists() {
        String ipAddressDuplicate = TEST_SENSOR_IP;

        when(sensorRepository.findSensorByIpAddress(ipAddressDuplicate)).thenReturn(Optional.of(testSensor));

        assertThrows(SensorAlreadyExistsException.class, () -> sensorService.addSensor(TEST_SENSOR_USERNAME, testSensorDTO));
        verify(sensorRepository).findSensorByIpAddress(ipAddressDuplicate);
    }

    @Test
    @DisplayName("Method addSensor() should make a call to repository to save the sensor to database")
    void addSensorShouldSaveProvidedData() {
        when(sensorRepository.findSensorByIpAddress(testSensorDTO.ipAddress())).thenReturn(Optional.empty());

        sensorService.addSensor(TEST_SENSOR_USERNAME, testSensorDTO);

        verify(sensorRepository).save(testSensor);
    }

    @Test
    @DisplayName("Method addSensor() should not try to save a sensor with the location objet set to null")
    void addSensorShouldNotTryToSaveLocationWithNullValue() {
        testSensor.setLocation(null);
        when(sensorRepository.findSensorByIpAddress(testSensorDTO.ipAddress())).thenReturn(Optional.empty());
        when(locationRepository.findById(TEST_SENSOR_LOCATION_NAME)).thenReturn(Optional.empty());


        sensorService.addSensor(TEST_SENSOR_USERNAME, testSensorDTO);

        ArgumentCaptor<Sensor> sensorCaptor = ArgumentCaptor.forClass(Sensor.class);
        verify(sensorRepository).save(sensorCaptor.capture());
        Sensor sensorToSave = sensorCaptor.getValue();
        assertNotNull(sensorToSave.getLocation());
    }

    @Test
    @DisplayName("Method addSensor() should throw a DefaultLocationException if the default location was not found")
    void addSensorShouldThrowDefaultLocationException() {
        testSensor.setLocation(null);

        when(sensorRepository.findSensorByIpAddress(testSensorDTO.ipAddress())).thenReturn(Optional.empty());
        when(locationRepository.findById(TEST_SENSOR_LOCATION_NAME)).thenReturn(Optional.empty());
        when(locationRepository.findById(DEFAULT_LOCATION_NAME)).thenReturn(Optional.empty());


        assertThrows(DefaultLocationException.class, () -> sensorService.addSensor(TEST_SENSOR_USERNAME, testSensorDTO));
    }

    @Test
    @DisplayName("Method addSensor() should not try to save a sensor with the user set to null")
    void addSensorShouldNotSaveSensorToDatabaseIfUserIsNull() {
        testSensor.setUser(null);

        when(sensorRepository.findSensorByIpAddress(testSensorDTO.ipAddress())).thenReturn(Optional.empty());
//        when(userRepository.findById(TEST_SENSOR_USERNAME)).thenReturn(Optional.empty());


        sensorService.addSensor(TEST_SENSOR_USERNAME, testSensorDTO);

        ArgumentCaptor<Sensor> sensorCaptor = ArgumentCaptor.forClass(Sensor.class);
        verify(sensorRepository).save(sensorCaptor.capture());
        Sensor sensorToSave = sensorCaptor.getValue();
        assertNotNull(sensorToSave.getUser());

    }

    @Test
    @DisplayName("Sensor saved to the database should contain user with the username matching the one from SensorDTO")
    void addSensorShouldSaveSensorToDatabaseWithUseWhichUsernameIsMatchingUsernameFromSensorDTO() {
        testSensor.setUser(null);

        when(sensorRepository.findSensorByIpAddress(testSensorDTO.ipAddress())).thenReturn(Optional.empty());
//        when(userRepository.findById(TEST_SENSOR_USERNAME)).thenReturn(Optional.empty());


        sensorService.addSensor(TEST_SENSOR_USERNAME, testSensorDTO);

        ArgumentCaptor<Sensor> sensorCaptor = ArgumentCaptor.forClass(Sensor.class);
        verify(sensorRepository).save(sensorCaptor.capture());
        Sensor sensorToSave = sensorCaptor.getValue();
        assertEquals(testSensor.getUser().getUsername(), sensorToSave.getUser().getUsername());
    }

    @Test
    @DisplayName("Sensor should throw an UserNotFound exception if the user with username passed in the SensorDTO does not exists in the user database")
    void addSensorShouldThrowUserNotFoundException() {
        testSensor.setUser(null);

        when(sensorRepository.findSensorByIpAddress(testSensorDTO.ipAddress())).thenReturn(Optional.empty());
        when(userRepository.findById(TEST_SENSOR_USERNAME)).thenReturn(Optional.empty());


        assertThrows(UserNotFoundException.class, () -> sensorService.addSensor(TEST_SENSOR_USERNAME, testSensorDTO));

        verify(userRepository).findById(TEST_SENSOR_USERNAME);
    }

    @Test
    @DisplayName("In method addSensor() saved sensor should use user object which was loaded from the user database")
    void addSensorShouldSaveSensorWithAppropriateUser() {
        testSensor.setUser(null);

        when(sensorRepository.findSensorByIpAddress(testSensorDTO.ipAddress())).thenReturn(Optional.empty());
        sensorService.addSensor(TEST_SENSOR_USERNAME, testSensorDTO);

        ArgumentCaptor<Sensor> sensorCaptor = ArgumentCaptor.forClass(Sensor.class);
        verify(sensorRepository).save(sensorCaptor.capture());
        Sensor sensorToSave = sensorCaptor.getValue();
        assertNotNull(sensorToSave.getUser());
        assertEquals(testSensor.getUser().getUsername(), sensorToSave.getUser().getUsername());
        assertEquals(TEST_SENSOR_USER, sensorToSave.getUser());
    }
}
