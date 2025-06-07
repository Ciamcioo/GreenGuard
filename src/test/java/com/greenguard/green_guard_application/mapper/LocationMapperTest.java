package com.greenguard.green_guard_application.mapper;

import com.greenguard.green_guard_application.model.dto.LocationDTO;
import com.greenguard.green_guard_application.model.entity.Location;
import com.greenguard.green_guard_application.service.mapper.LocationMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(properties = "spring.profiles.active=test")
public class LocationMapperTest {
    private static final String      LOCATION_NAME = "test location";
    private static final Location    LOCATION      = new Location(LOCATION_NAME);
    private static final LocationDTO LOCATION_DTO  = new LocationDTO(LOCATION_NAME);

    @Autowired
    LocationMapper locationMapper;

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
