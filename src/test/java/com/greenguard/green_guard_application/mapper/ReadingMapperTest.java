package com.greenguard.green_guard_application.mapper;

import com.greenguard.green_guard_application.model.dto.ReadingDTO;
import com.greenguard.green_guard_application.model.entity.Reading;
import com.greenguard.green_guard_application.service.mapper.ReadingMapper;
import com.greenguard.green_guard_application.service.mapper.SensorMapper;
import com.greenguard.green_guard_application.util.ReadingBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

public class ReadingMapperTest {
    private static final String  TEST_SENSOR      = "test sensor";
    private static final Double  TEST_TEMPERATURE = 20.0;
    private static final Double  TEST_HUMIDITY    = 20.0;
    private static final String  TEST_LOCATION    = "test_location";
    private static final Instant TEST_TIMESTAMP   = LocalDateTime
                                                        .of(2000, 1, 1, 12, 0)
                                                        .toInstant(ZoneOffset.UTC);

    private final ReadingMapper readingMapper = Mappers.getMapper(ReadingMapper.class);


    @Test
    @DisplayName("Reading Mapper should return null if argument is null")
    void readingMapperReturnNullIfEntityIsNull() {
        assertNull(readingMapper.toDto(null));
    }

    @Test
    @DisplayName("Reading Mapper should convert fields of reading entity to dto readingDTO with matching fields")
    void mapperShouldConvertEntityToDtoWhichFieldsMatch() {
        ReadingBuilder builder = ReadingBuilder.getInstance();
        Reading testReading = builder.withDefaultValues()
                                     .buildReading();

        ReadingDTO mappedReadingDTO = readingMapper.toDto(testReading);


        assertAll(
                () -> assertEquals(TEST_SENSOR, mappedReadingDTO.sensorName()),
                () -> assertEquals(TEST_TEMPERATURE, mappedReadingDTO.temperature()),
                () -> assertEquals(TEST_HUMIDITY, mappedReadingDTO.humidity()),
                () -> assertEquals(TEST_LOCATION, mappedReadingDTO.locationName()),
                () -> assertEquals(TEST_TIMESTAMP, mappedReadingDTO.timestamp())
        );
    }
}
