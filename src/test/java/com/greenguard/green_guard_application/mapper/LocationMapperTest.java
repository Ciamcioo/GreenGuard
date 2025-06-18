package com.greenguard.green_guard_application.mapper;

import com.greenguard.green_guard_application.model.dto.LocationDTO;
import com.greenguard.green_guard_application.model.entity.Location;
import com.greenguard.green_guard_application.service.mapper.LocationMapper;
import com.greenguard.green_guard_application.service.mapper.SensorMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class LocationMapperTest {

    // constant helper fields
    private static final String      LOCATION_NAME = "test location";
    private static final Location    LOCATION      = new Location(LOCATION_NAME);
    private static final LocationDTO LOCATION_DTO  = new LocationDTO(LOCATION_NAME);

    // tested field
    private final LocationMapper locationMapper = Mappers.getMapper(LocationMapper.class);

    @Test
    @DisplayName("If the entity location is null the result of mapping is null")
    void toDtoShouldReturnNull() {
        assertNull(locationMapper.toDto(null));
    }

    @Test
    @DisplayName("Method toDto should return object LocationDto which fields match the entity fields")
    void toDtoShouldMapTheFieldsOfEntity() {
        LocationDTO resultLocationDTO = locationMapper.toDto(LOCATION);
        assertEquals(LOCATION_DTO, resultLocationDTO);
    }

}
