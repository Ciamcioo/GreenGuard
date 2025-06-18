package com.greenguard.green_guard_application.service;

import com.greenguard.green_guard_application.model.dto.SensorDTO;
import com.greenguard.green_guard_application.model.entity.Location;
import com.greenguard.green_guard_application.model.entity.Sensor;
import com.greenguard.green_guard_application.repository.*;
import com.greenguard.green_guard_application.service.exception.*;
import com.greenguard.green_guard_application.service.mapper.SensorMapper;
import com.greenguard.green_guard_application.sensor.SensorRunner;
import com.greenguard.green_guard_application.util.SensorBuilder;

import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SensorServiceTest {

    // constant helper fields
    private static final String NOT_SPECIFIED_LOCATION_NAME = "not specified";
    private static final Location NOT_SPECIFIED_LOCATION = new Location(NOT_SPECIFIED_LOCATION_NAME);

    // helper fields
    private final SensorBuilder sensorBuilder = SensorBuilder.getInstance();
    private final String sensorOwnerUsername = SensorBuilder.DEFAULT_USER.getUsername();
    private Sensor sensorEntity;
    private SensorDTO sensorDTO;

    // mocked fields
    private SensorRepository sensorRepository;
    private LocationRepository locationRepository;
    private SensorMapper sensorMapper;
    private UserRepository userRepository;
    private SensorRunner sensorRunner;

    // tested field
    private SensorService sensorService;

    @BeforeEach
    void setup() {

        sensorEntity = sensorBuilder.withDefaultValues().buildSensor();
        sensorDTO = sensorBuilder.withDefaultValues().buildSensorDTO();

        mockSetup();

        sensorService = new SensorServiceImpl(sensorRepository, locationRepository, userRepository, sensorMapper, sensorRunner);
    }

    void mockSetup() {

        sensorRepository = mock(SensorRepository.class);
        when(sensorRepository.findSensorByUsernameAndName
                (sensorOwnerUsername, SensorBuilder.DEFAULT_NAME)).thenReturn(Optional.empty());
        when(sensorRepository.findSensorByUsernameAndIpAddress
                (sensorOwnerUsername, sensorDTO.ipAddress())).thenReturn(Optional.empty());

        locationRepository = mock(LocationRepository.class);
        when(locationRepository.findById
                (SensorBuilder.DEFAULT_LOCATION_NAME)).thenReturn(Optional.of(SensorBuilder.DEFAULT_LOCATION));
        when(locationRepository.findById
                (NOT_SPECIFIED_LOCATION_NAME)).thenReturn(Optional.of(NOT_SPECIFIED_LOCATION));

        userRepository = mock(UserRepository.class);
        when(userRepository.findById
                (sensorOwnerUsername)).thenReturn(Optional.of(SensorBuilder.DEFAULT_USER));

        sensorMapper = mock(SensorMapper.class);
        when(sensorMapper.toDTO(sensorEntity)).thenReturn(sensorDTO);
        when(sensorMapper.toEntity(sensorDTO)).thenReturn(sensorEntity);

        sensorRunner = mock(SensorRunner.class);
    }

    @Nested
    @DisplayName("Method getSensor() tests")
    class GetSensorTest {
        private final String sensorName = SensorBuilder.DEFAULT_NAME;

        @Test
        @DisplayName("Should not return null value")
        void getSensorsShouldNotReturnNullValue() {
            assertNotNull(sensorService.getSensors(sensorOwnerUsername));
        }

        @Test
        @DisplayName("Throw UserNotFoundException if user is not known")
        void getSensorsShouldThrowUserNotFoundException() {
            when(userRepository.findById(sensorOwnerUsername)).thenReturn(Optional.empty());

            assertThrows(UserNotFoundException.class,
                    () -> sensorService.getSensors(sensorOwnerUsername)
            );
        }

        @Test
        @DisplayName("Return all the sensor for specified user")
        void getSensorsShouldReturnAllSensorsForTheUser() {
            List<Sensor> userSensors = List.of(sensorEntity, sensorEntity);
            List<SensorDTO> expectedSensors = List.of(sensorDTO, sensorDTO);

            when(sensorRepository.findSensorByUser
                    (SensorBuilder.DEFAULT_USER)).thenReturn(userSensors);

            assertEquals(expectedSensors, sensorService.getSensors(sensorOwnerUsername));
        }

        @Test
        @DisplayName("Returned SensorDTO name should be equal to argument.")
        void getSensorShouldReturnSensorDTOWithNameEqualToArgument() {
            when(sensorRepository.findSensorByUsernameAndName
                    (sensorOwnerUsername, sensorName)).thenReturn(Optional.of(sensorEntity));

            assertEquals(sensorName, sensorService.getSensor(sensorOwnerUsername, sensorName).name());
        }

        @Test
        @DisplayName("Make a call to sensor repository passing the argument name.")
        void getSensorShouldMakeCallToSensorRepository() {
            when(sensorRepository.findSensorByUsernameAndName
                    (sensorOwnerUsername, sensorName)).thenReturn(Optional.of(sensorEntity));

            sensorService.getSensor(sensorOwnerUsername, sensorName);

            verify(sensorRepository).findSensorByUsernameAndName(sensorOwnerUsername, sensorName);
        }

        @Test
        @DisplayName("Throw SensorNotFoundException() when sensorRepository returns empty Optional.")
        void getSensorThrowSensorNotFoundException() {
            assertThrows(SensorNotFoundException.class, () -> sensorService.getSensor(sensorOwnerUsername, sensorName));
        }
    }

    @Nested
    class AddSensorTests {

        @Test
        @DisplayName("Should not return null value")
        void  addSensorShouldNotReturnNullValue() {
            assertNotNull(sensorService.addSensor(sensorOwnerUsername, sensorDTO));
        }

        @Test
        @DisplayName("Return value is an IP address of passed sensorDTO.")
        void addSensorShouldReturnIPAddressOfPassedSensorDTO() {
            assertEquals(sensorDTO.ipAddress(), sensorService.addSensor(sensorOwnerUsername, sensorDTO));
        }

        @Test
        @DisplayName("Throw SensorAlreadyExists() if sensorDTO with provided ip address already exists.")
        void addSensorShouldThrowSensorAlreadyExistsForIpAddress() {
            String ipAddressDuplicate = SensorBuilder.DEFAULT_IP_ADDRESS;

            when(sensorRepository.findSensorByUsernameAndIpAddress
                    (sensorOwnerUsername, ipAddressDuplicate)).thenReturn(Optional.of(sensorEntity));

            assertThrows(SensorAlreadyExistsException.class,
                    () -> sensorService.addSensor(SensorBuilder.DEFAULT_USER.getUsername(), sensorDTO));
        }
        
        @Test
        @DisplayName("Throw SensorAlreadyExists() if sensorDTO with provided sensor name already exists for this user.")
        void addSensorShouldThrowSensorAlreadyExistsForSensorName() {
            String sensorNameDuplicated = SensorBuilder.DEFAULT_NAME;

            when(sensorRepository.findSensorByUsernameAndName
                    (sensorOwnerUsername, sensorNameDuplicated)).thenReturn(Optional.of(sensorEntity));

            assertThrows(SensorAlreadyExistsException.class,
                    () -> sensorService.addSensor(sensorOwnerUsername, sensorDTO));
        }

        @Test
        @DisplayName("Should call repository interface to save the sensor.")
        void addSensorShouldSaveProvidedData() {
            sensorService.addSensor(sensorOwnerUsername, sensorDTO);

            verify(sensorRepository).save(sensorEntity);
        }

        @Test
        @DisplayName("Try to save a sensor with the location objet set to null is forbidden.")
        void addSensorShouldNotTryToSaveLocationWithNullValue() {
            sensorEntity.setLocation(null);

            when(locationRepository.findById(SensorBuilder.DEFAULT_LOCATION_NAME)).thenReturn(Optional.empty());

            sensorService.addSensor(sensorOwnerUsername, sensorDTO);

            ArgumentCaptor<Sensor> sensorCaptor = ArgumentCaptor.forClass(Sensor.class);
            verify(sensorRepository).save(sensorCaptor.capture());
            Sensor sensorToSave = sensorCaptor.getValue();
            assertNotNull(sensorToSave.getLocation());
        }

        @Test
        @DisplayName("Throw a DefaultLocationException if the default location was not found.")
        void addSensorShouldThrowDefaultLocationException() {
            sensorEntity.setLocation(null);

            when(locationRepository.findById(SensorBuilder.DEFAULT_LOCATION_NAME)).thenReturn(Optional.empty());
            when(locationRepository.findById(NOT_SPECIFIED_LOCATION_NAME)).thenReturn(Optional.empty());

            assertThrows(DefaultLocationException.class, () -> sensorService.addSensor(sensorOwnerUsername, sensorDTO));
        }

        @Test
        @DisplayName("Try to save a sensor with the user set to null is not allowed.")
        void addSensorShouldNotSaveSensorToDatabaseIfUserIsNull() {
            sensorEntity.setUser(null);
            sensorService.addSensor(sensorOwnerUsername, sensorDTO);

            ArgumentCaptor<Sensor> sensorCaptor = ArgumentCaptor.forClass(Sensor.class);
            verify(sensorRepository).save(sensorCaptor.capture());
            Sensor sensorToSave = sensorCaptor.getValue();
            assertNotNull(sensorToSave.getUser());

        }

        @Test
        @DisplayName("Saved sensor should contain user with the username matching the one from SensorDTO.")
        void addSensorShouldSaveSensorToDatabaseWithUseWhichUsernameIsMatchingUsernameFromSensorDTO() {
            sensorEntity.setUser(null);
            sensorService.addSensor(sensorOwnerUsername, sensorDTO);

            ArgumentCaptor<Sensor> sensorCaptor = ArgumentCaptor.forClass(Sensor.class);
            verify(sensorRepository).save(sensorCaptor.capture());
            Sensor sensorToSave = sensorCaptor.getValue();
            assertEquals(sensorEntity.getUser().getUsername(), sensorToSave.getUser().getUsername());
        }

        @Test
        @DisplayName("Throw an UserNotFoundException if the user with username passed in the SensorDTO does not exists.")
        void addSensorShouldThrowUserNotFoundException() {
            sensorEntity.setUser(null);

            when(userRepository.findById(sensorOwnerUsername)).thenReturn(Optional.empty());

            assertThrows(UserNotFoundException.class,
                    () -> sensorService.addSensor(sensorOwnerUsername, sensorDTO));

            verify(userRepository).findById(sensorOwnerUsername);
        }

        @Test
        @DisplayName("Saved sensor should contain user which was loaded from the user database")
        void addSensorShouldSaveSensorWithAppropriateUser() {
            sensorEntity.setUser(null);
            sensorService.addSensor(sensorOwnerUsername, sensorDTO);

            ArgumentCaptor<Sensor> sensorCaptor = ArgumentCaptor.forClass(Sensor.class);
            verify(sensorRepository).save(sensorCaptor.capture());
            Sensor sensorToSave = sensorCaptor.getValue();

            assertAll(
                    () -> assertNotNull(sensorToSave.getUser()),
                    () -> assertEquals(sensorEntity.getUser().getUsername(), sensorToSave.getUser().getUsername()),
                    () -> assertEquals(SensorBuilder.DEFAULT_USER, sensorToSave.getUser())
            );
        }
    }
}
