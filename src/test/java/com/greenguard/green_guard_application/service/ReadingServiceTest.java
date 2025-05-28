package com.greenguard.green_guard_application.service;

import com.greenguard.green_guard_application.model.dto.ReadingDTO;
import com.greenguard.green_guard_application.model.dto.ReadingFilterDTO;
import com.greenguard.green_guard_application.model.entity.Reading;
import com.greenguard.green_guard_application.repository.ReadingRepository;
import com.greenguard.green_guard_application.service.exception.ReadingNotFoundException;
import com.greenguard.green_guard_application.service.mapper.ReadingMapper;
import com.greenguard.green_guard_application.service.specification.ReadingSpecification;
import com.greenguard.green_guard_application.util.ReadingBuilder;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReadingServiceTest {

    static MockedStatic<ReadingSpecification> readingSpecification;

    static ReadingFilterDTO validReadingDTOFilter;
    static ReadingFilterDTO defaultReadingFilterDTO;

    static Specification<Reading> validSpecification;
    static Specification<Reading> defaultSpecification;

    static ReadingBuilder readingBuilder = ReadingBuilder.getInstance();
    static Reading validReading;
    static Reading notValidReading;
    static ReadingDTO validReadingDTO;

    ReadingService readingService;

    ReadingRepository readingRepository;
    ReadingMapper readingMapper;


    @BeforeAll
    static void setupStaticMock() {
        notValidReading = readingBuilder.buildReading();

        readingBuilder = readingBuilder.withTemperature(30.0);

        validReading = readingBuilder.buildReading();
        validReadingDTO = readingBuilder.buildReadingDTO();


        validReadingDTOFilter = new ReadingFilterDTO();
        validReadingDTOFilter.setTemperatureFrom(30.0);
        validSpecification = mock(Specification.class);

        readingSpecification = mockStatic(ReadingSpecification.class);

        readingSpecification.when(() -> ReadingSpecification.withFilter(validReadingDTOFilter)).thenReturn(validSpecification);
        readingSpecification.when(() -> ReadingSpecification.withFilter(defaultReadingFilterDTO)).thenReturn(defaultSpecification);
    }

    @BeforeEach
    void setup() {
        readingRepository = mock(ReadingRepository.class);
        readingMapper = mock(ReadingMapper.class);

        when(readingMapper.toDto(validReading)).thenReturn(validReadingDTO);

        readingService = new ReadingServiceImpl(readingRepository, readingMapper);
    }

    @Test
    @DisplayName("Method findAll() should not return null value")
    void findAllDoNotReturnNullValue() {
        assertNotNull(readingService.findAll(null));
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

    @AfterAll
    static void tearDownStaticMock() {
        readingSpecification.close();
    }




}
