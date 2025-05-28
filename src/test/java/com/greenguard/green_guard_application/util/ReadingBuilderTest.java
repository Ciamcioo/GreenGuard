package com.greenguard.green_guard_application.util;

import com.greenguard.green_guard_application.model.dto.ReadingDTO;
import com.greenguard.green_guard_application.model.entity.Reading;
import com.greenguard.green_guard_application.model.entity.Sensor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ReadingBuilderTest {
    private static final Sensor  DEF_SENSOR      = new Sensor(null,
                                                             "test sensor",
                                                             "test user",
                                                             "127.0.0.1",
                                                             "4A:9F:2C:75:B1:E3",
                                                             false);
    private static final Double  DEF_TEMPERATURE = 20.0;
    private static final Double  DEF_HUMIDITY    = 20.0;
    private static final Instant DEF_TIMESTAMP   = LocalDateTime
                                                        .of(2000, 1, 1, 12, 0)
                                                        .toInstant(ZoneOffset.UTC);

    private static final UUID    TEST_UUID        = UUID.randomUUID();
    private static final Sensor  TEST_SENSOR      = new Sensor(null,
                                                               "sensor name",
                                                               null,
                                                               null,
                                                               null,
                                                               true);
    private static final Double  TEST_TEMPERATURE = 100.0;
    private static final Double  TEST_HUMIDITY    = 10.0;
    private static final Instant TEST_TIMESTAMP   = LocalDateTime
                                                        .of(2010, 12, 1, 20, 0)
                                                        .toInstant(ZoneOffset.UTC);

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
                () -> assertEquals(DEF_SENSOR,      resultReading.getSensor()),
                () -> assertEquals(DEF_TEMPERATURE, resultReading.getTemperature()),
                () -> assertEquals(DEF_HUMIDITY,    resultReading.getHumidity()),
                () -> assertEquals(DEF_TIMESTAMP,   resultReading.getTimestamp())
        );
    }

    @Test
    @DisplayName("Reading Builder should return readingDTO with proper default values")
    void returnReadingDTOWithDefaultValues() {
        ReadingDTO resultReadingDTO = readingBuilder.buildReadingDTO();

        assertAll(
                () -> assertEquals(DEF_SENSOR.getName(), resultReadingDTO.sensorName()),
                () -> assertEquals(DEF_TEMPERATURE,      resultReadingDTO.temperature()),
                () -> assertEquals(DEF_HUMIDITY,         resultReadingDTO.humidity()),
                () -> assertEquals(DEF_TIMESTAMP,        resultReadingDTO.timestamp())
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
                () -> assertEquals(TEST_SENSOR.getName(), resultReadingDTO.sensorName()),
                () -> assertEquals(TEST_TEMPERATURE,      resultReadingDTO.temperature()),
                () -> assertEquals(TEST_HUMIDITY,         resultReadingDTO.humidity()),
                () -> assertEquals(TEST_TIMESTAMP,        resultReadingDTO.timestamp())
        );
    }
}
