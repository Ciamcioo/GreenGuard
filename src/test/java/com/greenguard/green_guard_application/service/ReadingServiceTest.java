package com.greenguard.green_guard_application.service;

import com.greenguard.green_guard_application.model.dto.ReadingDTO;
import com.greenguard.green_guard_application.model.dto.ReadingFilterDTO;
import com.greenguard.green_guard_application.model.entity.*;
import com.greenguard.green_guard_application.repository.ReadingRepository;
import com.greenguard.green_guard_application.repository.UserRepository;
import com.greenguard.green_guard_application.service.exception.*;
import com.greenguard.green_guard_application.service.mapper.ReadingMapper;
import com.greenguard.green_guard_application.service.specification.ReadingSpecification;
import com.greenguard.green_guard_application.util.ReadingBuilder;
import com.greenguard.green_guard_application.util.SensorBuilder;

import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.springframework.data.jpa.domain.Specification;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReadingServiceTest {

    // constant helper fields
    private static final String SENSOR_OWNER_USERNAME = "test_username";
    private static final String LOCATION_NAME_RZESZOW = "Rzeszow";
    private static final String LOCATION_NAME_WROCLAW = "Wroclaw";
    private static final Location LOCATION_RZESZOW = new Location(LOCATION_NAME_RZESZOW);
    private static final Location LOCATION_WROCLAW = new Location(LOCATION_NAME_WROCLAW);

    // helper fields
    private static ReadingBuilder readingBuilder = ReadingBuilder.getInstance();
    private static ReadingFilterDTO validReadingDTOFilter;
    private static ReadingFilterDTO defaultReadingFilterDTO;
    private static Reading validReading;
    private static ReadingDTO validReadingDTO;
    private final SensorBuilder sensorBuilder = SensorBuilder.getInstance();
    private Sensor sensorInFavLocation;
    private Set<String> favLocationNames;
    private List<Reading> readings;
    private List<ReadingDTO> readingDTOs ;

    // mocked fields
    private static MockedStatic<ReadingSpecification> readingSpecification;
    private static Specification<Reading> validSpecification;
    private static Specification<Reading> defaultSpecification;
    private ReadingRepository readingRepository;
    private UserRepository userRepository;
    private EntityManager entityManager;
    private ReadingMapper readingMapper;

    // test service
    private ReadingService readingService;

    @BeforeAll
    static void setupStaticMock() {
        readingBuilder = readingBuilder.withDefaultValues()
                                       .withTemperature(30.0);
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
        readingBuilder = readingBuilder.withDefaultValues()
                                        .withTemperature(30.0);

        readings = new ArrayList<>(List.of(validReading, validReading, validReading));
        readingDTOs = List.of(validReadingDTO, validReadingDTO, validReadingDTO);

        defaultReadingFilterDTO.setSensorOwnerUsername(SENSOR_OWNER_USERNAME);
        User sensorOwner = new User(SENSOR_OWNER_USERNAME,
                                    "password",
                                    List.of(LOCATION_RZESZOW, LOCATION_WROCLAW));

        sensorInFavLocation = sensorBuilder.withId(UUID.randomUUID())
                .withName("sensor in favorite location")
                .withUser(sensorOwner)
                .withIpAddress("10.0.0.1")
                .withMac("AB:CD:EF:GH:10")
                .withLocation(LOCATION_RZESZOW)
                .withActive(true)
                .buildSensor();
        favLocationNames = sensorOwner.getFavoriteLocations().stream().map(Location::getName).collect(Collectors.toSet());

        mockSetup(sensorOwner);

        readingService = new ReadingServiceImpl(readingRepository, userRepository, readingMapper,entityManager);
    }

    private void mockSetup(User sensorOwner) {
        readingRepository = mock(ReadingRepository.class);
        when(readingRepository.findAll(defaultSpecification)).thenReturn(readings);

        entityManager = mock(EntityManager.class);
        when(entityManager.find(eq(Sensor.class), any(UUID.class)))
                .thenReturn(new Sensor());

        readingMapper = mock(ReadingMapper.class);
        when(readingMapper.toDto(validReading)).thenReturn(validReadingDTO);

        userRepository = mock(UserRepository.class);
        when(userRepository.findById
                (SENSOR_OWNER_USERNAME)).thenReturn(Optional.of(sensorOwner));
    }

    @Nested
    class FindAllReadingsTest {

        @Test
        @DisplayName("Should not return null value.")
        void findAllDoNotReturnNullValue() {
            assertNotNull(readingService.findAll(defaultReadingFilterDTO));
        }

        @Test
        @DisplayName("Should return all the possible readings without filtering.")
        void findAllWithoutFilteringShouldReturnAllTheReadings() {
            assertEquals(readingDTOs, readingService.findAll(defaultReadingFilterDTO));
        }

        @Test
        @DisplayName("Should return all of the readings which match the filter.")
        void findAllWithFilteringShouldReturnAllTheValidReadings() {
            when(readingRepository.findAll(validSpecification)).thenReturn(readings);

            assertEquals(readingDTOs, readingService.findAll(validReadingDTOFilter));
        }

        @Test
        @DisplayName("If no matching reading for the filter, list should be empty.")
        void findAllWithFilteringShouldReturnEmptyList() {
            List<Reading> emptyList = new ArrayList<Reading>();

            when(readingRepository.findAll(defaultSpecification)).thenReturn(emptyList);

            assertTrue(readingService.findAll(defaultReadingFilterDTO).isEmpty());
        }

        @Test
        @DisplayName("UserNotFoundException should be thrown if the user from the filter hasn't been found.")
        void findAllShouldThrowUserNotFoundException() {
            String userNotPresentInUserTable = SENSOR_OWNER_USERNAME;

            when(userRepository.findById(userNotPresentInUserTable)).thenReturn(Optional.empty());

            assertThrows(UserNotFoundException.class, () -> readingService.findAll(defaultReadingFilterDTO));
        }
    }


    @Nested
    class FindLastReadingTest {

        @Test
        @DisplayName("Should not return null value.")
        void findLastReadingShouldNotReturnNullValue() {
            assertNotNull(readingService.findLastReading(defaultReadingFilterDTO));
        }

        @Test
        @DisplayName("Should return the reading with the latest timestamp.")
        void findLastReadingShouldReturnReadingWithTheLatestTimeStamp() {
            Reading latestReading = readingBuilder.withTimestamp(
                                        Instant.now()).buildReading();
            ReadingDTO lastReadingDTO = readingBuilder.buildReadingDTO();
            readings.add(latestReading);

            when(readingMapper.toDto(latestReading)).thenReturn(lastReadingDTO);

            assertEquals(lastReadingDTO, readingService.findLastReading(defaultReadingFilterDTO));
        }

        @Test
        @DisplayName("Throw an exception ReadingNotFound, if no reading matches the filter.")
        void throwReadingNotFoundIfThereWasNotMatchingCandidates() {
            readings = List.of();

            when(readingRepository.findAll(defaultSpecification)).thenReturn(readings);

            assertThrows(ReadingNotFoundException.class,
                    () -> readingService.findLastReading(defaultReadingFilterDTO));
        }


    }

   @Nested
   class GetFavoriteLocationReadings {

       @Test
       @DisplayName("Should not return null value.")
       void getFavLocationReadingsShouldNotReturnNullValue() {
           assertNotNull(readingService.getFavoriteLocationReadings(defaultReadingFilterDTO));
       }

       @Test
       @DisplayName("UserNotFoundException should be thrown if a username is not present.")
       void getFavLocationReadingsShouldThrowUserNotFoundException() {
           String unknownUser = SENSOR_OWNER_USERNAME;

           when(userRepository.findById(unknownUser)).thenReturn(Optional.empty());

           assertThrows(UserNotFoundException.class,
                   () -> readingService.getFavoriteLocationReadings(defaultReadingFilterDTO));
       }

       @Test
       @DisplayName("Should return only the readings from favorite locations, return list should not be empty")
       void getFavLocReadingsShouldReturnOnlyReadingFromFavoriteLocations() {
           Reading readingFavLocation = readingBuilder.withDefaultValues()
                   .withSensor(sensorInFavLocation)
                   .buildReading();
           ReadingDTO readingFavLocationDTO = readingBuilder.buildReadingDTO();
           List<Reading> allReadings = List.of(validReading, validReading, readingFavLocation);

           when(readingRepository.findAll
                   (defaultSpecification)).thenReturn(allReadings);
           when(readingMapper.toDto
                   (readingFavLocation)).thenReturn(readingFavLocationDTO);

           List<ReadingDTO> favLocationsReadings = readingService.getFavoriteLocationReadings(defaultReadingFilterDTO);

           assertFalse(favLocationsReadings.isEmpty());
           favLocationsReadings.forEach(
                   favLocationReading -> assertTrue(favLocationNames.contains(favLocationReading.locationName()))
           );
       }
   }

    @Nested
    class AddReadingTest {
        private UUID sensorID;
        private ReadingDTO readingDTO;
        private Reading reading;

        @BeforeEach
        void setupAddReadingTest() {
            sensorID = UUID.randomUUID();
            readingDTO = validReadingDTO;
            reading = readingBuilder.withSensor(null).buildReading();

            when(readingMapper.toEntity(readingDTO)).thenReturn(reading);

        }

        @Test
        @DisplayName("Should not return null value")
        void addReadingShouldNotReturnNull() {
            assertNotNull(readingService.addReading(sensorID, readingDTO));
        }

        @Test
        @DisplayName("Should return the readingDTO timestamp")
        void addReadingShouldReturnReadingDTOTimestamp() {
            Instant correctDate = LocalDateTime.of(2000, 1, 1, 12, 0)
                    .toInstant(ZoneOffset.UTC);

            Instant resultInstant = readingService.addReading(sensorID, readingDTO);
            assertEquals(0, correctDate.compareTo(resultInstant));
        }

        @Test
        @DisplayName("Call readingRepository with entity reading matching arg readingDTO")
        void addReadingShouldCallReadingRepositoryWithTheReading() {
            readingService.addReading(sensorID, readingDTO);

            verify(readingRepository).save(reading);
        }

        @Test
        @DisplayName("Set the Sensor field in a reading to persist before performing a save")
        void addReadingShouldSetSensorFiledOfReadingToPersistBeforeSaving() {
            readingService.addReading(sensorID, readingDTO);

            ArgumentCaptor<Reading> readingCaptor = ArgumentCaptor.forClass(Reading.class);
            verify(readingRepository).save(readingCaptor.capture());
            Reading readingToPersist = readingCaptor.getValue();
            assertNotNull(readingToPersist.getSensor());
        }

        @Test
        @DisplayName("If the sensor of reading is null, method add reading should throw SensorNotFound exception")
        void addReadingShouldThrowSensorNotFoundException() {
            when(entityManager.find(eq(Sensor.class), any(UUID.class))).thenReturn(null);

            assertThrows(SensorNotFoundException.class, () -> readingService.addReading(sensorID, readingDTO));
        }
    }

    @AfterAll
    static void tearDownStaticMock() {
        readingSpecification.close();
    }




}
