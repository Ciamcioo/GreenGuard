package com.greenguard.green_guard_application.util;

import com.greenguard.green_guard_application.model.dto.ReadingDTO;
import com.greenguard.green_guard_application.model.entity.*;
import org.junit.jupiter.api.*;

import java.time.*;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ReadingBuilderTest {

    // constant fields
    private static final UUID     TEST_UUID        = UUID.randomUUID();
    private static final Location TEST_LOCATION    = new Location("location test");
    private static final Sensor   TEST_SENSOR      = new Sensor(null, "sensor name", null, null, null, TEST_LOCATION, true);
    private static final Double   TEST_TEMPERATURE = 100.0;
    private static final Double   TEST_HUMIDITY    = 10.0;
    private static final Instant  TEST_TIMESTAMP   = LocalDateTime.of(2010, 12, 1, 20, 0).toInstant(ZoneOffset.UTC);

    // tested field
    ReadingBuilder readingBuilder;

    @BeforeEach
    void setup() {
        readingBuilder = ReadingBuilder.getInstance();
    }

    @Test
    @DisplayName("Reading Builder should not return null value")
    void buildReadingShouldNotReturnNull() {
        assertNotNull(readingBuilder.buildReading());
        assertNotNull(readingBuilder.buildReadingDTO());
    }

    @Test
    @DisplayName("Reading Builder should return reading with properly set fields")
    void returnReadingWithSetValues() {
        readingBuilder = readingBuilder.withId(TEST_UUID)
                                       .withSensor(TEST_SENSOR)
                                       .withTemperature(TEST_TEMPERATURE)
                                       .withHumidity(TEST_HUMIDITY)
                                       .withTimestamp(TEST_TIMESTAMP);
        Reading resultReading = readingBuilder.buildReading();

        assertAll(
                () -> assertInstanceOf(UUID.class,   resultReading.getId()),
                () -> assertEquals(TEST_SENSOR,      resultReading.getSensor()),
                () -> assertEquals(TEST_TEMPERATURE, resultReading.getTemperature()),
                () -> assertEquals(TEST_HUMIDITY,    resultReading.getHumidity()),
                () -> assertEquals(TEST_TIMESTAMP,   resultReading.getTimestamp())
        );
    }

    @Test
    @DisplayName("Reading Builder should return reading with proper default values")
    void returnReadingWithDefaultValues() {
        Reading resultReading = readingBuilder.buildReading();

        assertAll(
                () -> assertInstanceOf(UUID.class,  resultReading.getId()),
                () -> assertEquals(ReadingBuilder.DEFAULT_SENSOR,      resultReading.getSensor()),
                () -> assertEquals(ReadingBuilder.DEFAULT_TEMPERATURE, resultReading.getTemperature()),
                () -> assertEquals(ReadingBuilder.DEFAULT_HUMIDITY,    resultReading.getHumidity()),
                () -> assertEquals(ReadingBuilder.DEFAULT_TIMESTAMP,   resultReading.getTimestamp())
        );
    }

    @Test
    @DisplayName("Reading Builder should return readingDTO with proper default values")
    void returnReadingDTOWithDefaultValues() {
        ReadingDTO resultReadingDTO = readingBuilder.buildReadingDTO();

        assertAll(
                () -> assertEquals(ReadingBuilder.DEFAULT_SENSOR.getName(), resultReadingDTO.sensorName()),
                () -> assertEquals(ReadingBuilder.DEFAULT_TEMPERATURE,      resultReadingDTO.temperature()),
                () -> assertEquals(ReadingBuilder.DEFAULT_HUMIDITY,         resultReadingDTO.humidity()),
                () -> assertEquals(ReadingBuilder.DEFAULT_TIMESTAMP,        resultReadingDTO.timestamp())
        );
    }

    @Test
    @DisplayName("Reading Builder should return readingDTO with properly set fields")
    void returnReadingDTOWithSetValues() {
        readingBuilder = readingBuilder.withId(TEST_UUID)
                                       .withSensor(TEST_SENSOR)
                                       .withTemperature(TEST_TEMPERATURE)
                                       .withHumidity(TEST_HUMIDITY)
                                       .withTimestamp(TEST_TIMESTAMP);
        ReadingDTO resultReadingDTO = readingBuilder.buildReadingDTO();

        assertAll(
                () -> assertEquals(TEST_SENSOR.getName(),   resultReadingDTO.sensorName()),
                () -> assertEquals(TEST_TEMPERATURE,        resultReadingDTO.temperature()),
                () -> assertEquals(TEST_HUMIDITY,           resultReadingDTO.humidity()),
                () -> assertEquals(TEST_LOCATION.getName(), resultReadingDTO.locationName()),
                () -> assertEquals(TEST_TIMESTAMP,          resultReadingDTO.timestamp())
        );
    }
}
