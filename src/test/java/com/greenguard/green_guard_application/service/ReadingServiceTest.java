package com.greenguard.green_guard_application.service;

import com.greenguard.green_guard_application.model.dto.ReadingDTO;
import com.greenguard.green_guard_application.model.dto.ReadingFilterDTO;
import com.greenguard.green_guard_application.model.entity.Location;
import com.greenguard.green_guard_application.model.entity.Reading;
import com.greenguard.green_guard_application.model.entity.Sensor;
import com.greenguard.green_guard_application.model.entity.User;
import com.greenguard.green_guard_application.repository.ReadingRepository;
import com.greenguard.green_guard_application.repository.UserRepository;
import com.greenguard.green_guard_application.service.exception.ReadingNotFoundException;
import com.greenguard.green_guard_application.service.exception.SensorNotFoundException;
import com.greenguard.green_guard_application.service.exception.UserNotFoundException;
import com.greenguard.green_guard_application.service.mapper.ReadingMapper;
import com.greenguard.green_guard_application.service.specification.ReadingSpecification;
import com.greenguard.green_guard_application.util.ReadingBuilder;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReadingServiceTest {
    private static final String SENSOR_OWNER_USERNAME = "test_username";
    private static final String LOCATION_NAME_RZESZOW = "Rzeszow";
    private static final String LOCATION_NAME_WROCLAW = "Wroclaw";
    private static final Location LOCATION_RZESZOW = new Location(LOCATION_NAME_RZESZOW);
    private static final Location LOCATION_WROCLAW = new Location(LOCATION_NAME_WROCLAW);

    private static MockedStatic<ReadingSpecification> readingSpecification;

    private static ReadingFilterDTO validReadingDTOFilter;
    private static ReadingFilterDTO defaultReadingFilterDTO;

    private static Specification<Reading> validSpecification;
    private static Specification<Reading> defaultSpecification;

    private static ReadingBuilder readingBuilder = ReadingBuilder.getInstance();
    private static Reading validReading;
    private static ReadingDTO validReadingDTO;

    private ReadingService readingService;

    private Sensor sensorInFavLocation;
    private Set<String> favLocationNames;
    private ReadingRepository readingRepository;
    private UserRepository userRepository;
    private EntityManager entityManager;
    private ReadingMapper readingMapper;


    @BeforeAll
    static void setupStaticMock() {
        Reading notValidReading = readingBuilder.buildReading();


        readingBuilder = readingBuilder.withTemperature(30.0);

        validReading = readingBuilder.buildReading();
        validReadingDTO = readingBuilder.buildReadingDTO();

        defaultReadingFilterDTO = new ReadingFilterDTO();
        defaultReadingFilterDTO.setSensorOwnerUsername(SENSOR_OWNER_USERNAME);

        validReadingDTOFilter = new ReadingFilterDTO();
        validReadingDTOFilter.setSensorOwnerUsername(SENSOR_OWNER_USERNAME);
        validReadingDTOFilter.setTemperatureFrom(30.0);
        validSpecification = mock(Specification.class);

        readingSpecification = mockStatic(ReadingSpecification.class);

        readingSpecification.when(() -> ReadingSpecification.withFilter(validReadingDTOFilter)).thenReturn(validSpecification);
        readingSpecification.when(() -> ReadingSpecification.withFilter(defaultReadingFilterDTO)).thenReturn(defaultSpecification);
    }

    @BeforeEach
    void setup() {
        userRepository = mock(UserRepository.class);
        readingRepository = mock(ReadingRepository.class);
        readingMapper = mock(ReadingMapper.class);
        entityManager = mock(EntityManager.class);

        defaultReadingFilterDTO.setSensorOwnerUsername(SENSOR_OWNER_USERNAME);

        User sensorOwner = new User(SENSOR_OWNER_USERNAME, "password", List.of(LOCATION_RZESZOW, LOCATION_WROCLAW));
        sensorInFavLocation = new Sensor("sensor in favorite location", sensorOwner,
                                         "10.0.0.1", "AB:CD:EF:GH:10",
                                          LOCATION_RZESZOW, true);
        favLocationNames = sensorOwner.getFavoriteLocations().stream().map(Location::getName).collect(Collectors.toSet());

        when(readingMapper.toDto(validReading)).thenReturn(validReadingDTO);
        when(userRepository.findById(SENSOR_OWNER_USERNAME)).thenReturn(Optional.of(sensorOwner));

        readingService = new ReadingServiceImpl(readingRepository, userRepository, readingMapper,entityManager);
    }

    @Test
    @DisplayName("Method findAll() should not return null value")
    void findAllDoNotReturnNullValue() {
        assertNotNull(readingService.findAll(defaultReadingFilterDTO));
    }

    @Test
    @DisplayName("Method findAll() with no filtering should return all the possible readings")
    void findAllWithoutFilteringShouldReturnAllTheReadings() {
        List<Reading> testReadings = List.of(validReading, validReading, validReading);
        List<ReadingDTO> testReadingDTOs = List.of(validReadingDTO, validReadingDTO, validReadingDTO);

        when(readingRepository.findAll(defaultSpecification)).thenReturn(testReadings);

        assertEquals(testReadingDTOs, readingService.findAll(defaultReadingFilterDTO));
    }

    @Test
    @DisplayName("Method findAll() with filtering should return all of the readings which match the predicate")
    void findAllWithFilteringShouldReturnAllTheValidReadings() {
            List<Reading> readings = List.of(validReading, validReading);
            List<ReadingDTO> resultReadingDTO = List.of(validReadingDTO, validReadingDTO);

           when(readingRepository.findAll(validSpecification)).thenReturn(readings);

           assertEquals(resultReadingDTO, readingService.findAll(validReadingDTOFilter));
    }

    @Test
    @DisplayName("If method findAll() haven't find reading matching the predicate, method should return empty list")
    void findAllWithFilteringShouldReturnEmptyList() {
        List<Reading> emptyList = new ArrayList<Reading>();

        when(readingRepository.findAll(defaultSpecification)).thenReturn(emptyList);

        assertTrue(readingService.findAll(defaultReadingFilterDTO).isEmpty());
    }

    @Test
    @DisplayName("UserNotFoundException should be thrown if the user hasn't been found in the table of user, which has been passed in reading filter")
    void findAllShouldThrowUserNotFoundException() {
        String userNotPresentInUserTable = SENSOR_OWNER_USERNAME;

        when(userRepository.findById(userNotPresentInUserTable)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> readingService.findAll(defaultReadingFilterDTO));
    }

    @Test
    @DisplayName("Method findLastReading should not return null value")
    void findLastReadingShouldNotReturnNullValue() {
        when(readingRepository.findAll(defaultSpecification)).thenReturn(List.of(validReading));
        assertNotNull(readingService.findLastReading(defaultReadingFilterDTO));
    }

    @Test
    @DisplayName("Method findLastReading should return the reading with the latest timestamp")
    void findLastReadingShouldReturnReadingWithTheLatestTimeStamp() {
        readingBuilder = readingBuilder.withDefaultValues();
        LocalDateTime referenceDate = LocalDateTime.of(2000, 10, 12, 13, 33, 40);

        Reading firstReading = readingBuilder.withTimestamp(
                                                 referenceDate.toInstant(ZoneOffset.UTC)
                                               ).buildReading();

        Reading secondReading = readingBuilder.withTimestamp(
                                                referenceDate.plusYears(10).toInstant(ZoneOffset.UTC)
                                               ).buildReading();

        Reading latestReading = readingBuilder.withTimestamp(
                                                 referenceDate.plusYears(20).toInstant(ZoneOffset.UTC)
                                                ).buildReading();
        ReadingDTO lastReadingDTO = readingBuilder.buildReadingDTO();

        List<Reading> readingList = List.of(firstReading, secondReading, latestReading);
        when(readingRepository.findAll(defaultSpecification)).thenReturn(readingList);
        when(readingMapper.toDto(latestReading)).thenReturn(lastReadingDTO);

        assertEquals(lastReadingDTO, readingService.findLastReading(defaultReadingFilterDTO));
    }

    @Test
    @DisplayName("Method findLastReading should throw a exception ReadingNotFound," +
                " if there was no candidates matching filtering expectation for latest reading")
    void throwReadingNotFoundIfThereWasNotMatchingCandidates() {
        when(readingRepository.findAll(defaultSpecification)).thenReturn(List.of());

        assertThrows(ReadingNotFoundException.class,
                      () -> readingService.findLastReading(defaultReadingFilterDTO));
    }

    @Test
    @DisplayName("Method findLastReading should throw an exception ReadingNotFound with a message")
    void throwReadingNotFoundWithAMessage() {
        when(readingRepository.findAll(defaultSpecification)).thenReturn(List.of());

        Exception exception = assertThrows(ReadingNotFoundException.class,
                () -> readingService.findLastReading(defaultReadingFilterDTO));

        assertNotNull(exception.getMessage());
    }

    @Test
    @DisplayName("Method getFavoriteLocationsReadings() should not return null value")
    void getFavLocationReadingsShouldNotReturnNullValue() {
        assertNotNull(readingService.getFavoriteLocationReadings(defaultReadingFilterDTO));
    }

    @Test
    @DisplayName("UserNotFoundException should be thrown if a username is not present in user table")
    void getFavLocationReadingsShouldThrowUserNotFoundException() {
        String unknownUser = SENSOR_OWNER_USERNAME;
        when(userRepository.findById(unknownUser)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> readingService.getFavoriteLocationReadings(defaultReadingFilterDTO));
    }

    @Test
    @DisplayName("Method getFavoriteLocationsReadings() should return only the readings from favorite locations and return list should not be empty")
    void getFavLocReadingsShouldReturnOnlyReadingFromFavoriteLocations() {
        Reading readingFavLocation = readingBuilder.withDefaultValues()
                                                    .withSensor(sensorInFavLocation)
                                                    .buildReading();
        ReadingDTO readingFavLocationDTO = readingBuilder.buildReadingDTO();
        List<Reading> allReadings = List.of(validReading, validReading, readingFavLocation);

        when(readingRepository.findAll(defaultSpecification)).thenReturn(allReadings);
        when(readingMapper.toDto(readingFavLocation)).thenReturn(readingFavLocationDTO);

        List<ReadingDTO> favLocationsReadings = readingService.getFavoriteLocationReadings(defaultReadingFilterDTO);

        assertFalse(favLocationsReadings.isEmpty());
        favLocationsReadings.forEach(
                favLocationReading -> assertTrue(favLocationNames.contains(favLocationReading.locationName()))
        );
    }

    @Test
    @DisplayName("Method addReading should not return null value")
    void addReadingShouldNotReturnNull() {
        UUID sensorID = UUID.randomUUID();
        ReadingDTO readingDTO = readingBuilder.withDefaultValues().buildReadingDTO();
        Reading reading = readingBuilder.buildReading();

        when(readingMapper.toEntity(readingDTO)).thenReturn(reading);
        when(entityManager.find(eq(Sensor.class), any(UUID.class))).thenReturn(new Sensor());

        assertNotNull(readingService.addReading(sensorID, readingDTO));
    }

    @Test
    @DisplayName("Method addReading should return the readingDTO timestamp")
    void addReadingShouldReturnReadingDTOTimestamp() {
        UUID sensorID = UUID.randomUUID();
        ReadingDTO readingDTO = readingBuilder.withDefaultValues().buildReadingDTO();
        Reading reading = readingBuilder.buildReading();
        Instant correctDate = LocalDateTime.of(2000, 1, 1, 12, 0)
                                            .toInstant(ZoneOffset.UTC);

        when(readingMapper.toEntity(readingDTO)).thenReturn(reading);
        when(entityManager.find(eq(Sensor.class), any(UUID.class))).thenReturn(new Sensor());

        Instant resultInstant = readingService.addReading(sensorID, readingDTO);
        assertEquals(0, correctDate.compareTo(resultInstant));
    }

    @Test
    @DisplayName("Method addReading should call the readingRepository with entity reading matching the readingDTO")
    void addReadingShouldCallReadingRepositoryWithTheReading() {
        UUID sensorID = UUID.randomUUID();
        ReadingDTO readingDTO = readingBuilder.withDefaultValues().buildReadingDTO();
        Reading reading = readingBuilder.buildReading();

        when(readingMapper.toEntity(readingDTO)).thenReturn(reading);
        when(entityManager.find(eq(Sensor.class), any(UUID.class))).thenReturn(new Sensor());

        readingService.addReading(sensorID, readingDTO);

        verify(readingRepository).save(reading);
    }

    @Test
    @DisplayName("Method add reading should set the Sensor field in the reading to persist before performing a save")
    void addReadingShouldSetSensorFiledOfReadingToPersistBeforeSaving() {
        UUID sensorID = UUID.randomUUID();
        ReadingDTO readingDTO = readingBuilder.withDefaultValues().buildReadingDTO();
        Reading reading = readingBuilder.withSensor(null).buildReading();

        when(readingMapper.toEntity(readingDTO)).thenReturn(reading);
        when(entityManager.find(eq(Sensor.class), any(UUID.class))).thenReturn(new Sensor());

        readingService.addReading(sensorID, readingDTO);

        ArgumentCaptor<Reading> readingCaptor = ArgumentCaptor.forClass(Reading.class);
        verify(readingRepository).save(readingCaptor.capture());

        Reading readingToPersist = readingCaptor.getValue();
        assertNotNull(readingToPersist.getSensor());
    }

    @Test
    @DisplayName("If the sensor of reading is null, method add reading should throw SensorNotFound exception")
    void addReadingShouldThrowSensorNotFoundException() {
        UUID sensorID = UUID.randomUUID();
        ReadingDTO readingDTO = readingBuilder.withDefaultValues().buildReadingDTO();
        Reading reading = readingBuilder.withSensor(null).buildReading();

        when(readingMapper.toEntity(readingDTO)).thenReturn(reading);
        when(entityManager.find(eq(Sensor.class), any(UUID.class))).thenReturn(null);

        assertThrows(SensorNotFoundException.class, () -> readingService.addReading(sensorID, readingDTO));
    }


    @AfterAll
    static void tearDownStaticMock() {
        readingSpecification.close();
    }




}
