package com.greenguard.green_guard_application.mapper;

import com.greenguard.green_guard_application.model.dto.ReadingDTO;
import com.greenguard.green_guard_application.model.entity.Reading;
import com.greenguard.green_guard_application.service.mapper.ReadingMapper;
import com.greenguard.green_guard_application.util.ReadingBuilder;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

public class ReadingMapperTest {
    private final ReadingMapper readingMapper = Mappers.getMapper(ReadingMapper.class);
    private final Reading       reading       = ReadingBuilder.getInstance().buildReading();
    private final ReadingDTO    readingDto    = ReadingBuilder.getInstance().buildReadingDTO();


    @Test
    @DisplayName("Reading Mapper should return null if argument is null")
    void readingMapperReturnNullIfEntityIsNull() {
        assertNull(readingMapper.toDto(null));
    }

    @Test
    @DisplayName("Reading Mapper should convert fields of reading entity to dto readingDTO with matching fields")
    void mapperShouldConvertEntityToDtoWhichFieldsMatch() {
        ReadingDTO mappedReadingDTO = readingMapper.toDto(reading);


        assertAll(
                () -> assertEquals(reading.getSensor().getName(),               mappedReadingDTO.sensorName()),
                () -> assertEquals(reading.getTemperature(),                    mappedReadingDTO.temperature()),
                () -> assertEquals(reading.getHumidity(),                       mappedReadingDTO.humidity()),
                () -> assertEquals(reading.getSensor().getLocation().getName(), mappedReadingDTO.locationName()),
                () -> assertEquals(reading.getTimestamp(),                      mappedReadingDTO.timestamp())
        );
    }

    @Test
    @DisplayName("Reading Mapper should convert fields of readingDTO to reading entity with matching fields")
    void mapperShouldConvertReadingDtoToReadingEntity() {
        Reading mappedReading = readingMapper.toEntity(readingDto);

        assertAll(
                () -> assertNull(mappedReading.getId()),
                () -> assertNull(mappedReading.getSensor()),
                () -> assertEquals(readingDto.temperature(), mappedReading.getTemperature()),
                () -> assertEquals(readingDto.humidity(),    mappedReading.getHumidity()),
                () -> assertEquals(readingDto.timestamp(),   mappedReading.getTimestamp())
        );
    }
}
