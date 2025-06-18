package com.greenguard.green_guard_application.service;

import com.greenguard.green_guard_application.model.dto.LocationDTO;
import com.greenguard.green_guard_application.model.entity.Location;
import com.greenguard.green_guard_application.repository.LocationRepository;
import com.greenguard.green_guard_application.service.mapper.LocationMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class LocationServiceTest {

    // mocked fields
    LocationRepository locationRepository;
    LocationMapper locationMapper;

    // tested field
    LocationService locationService;

    @BeforeEach
    void setup() {
        locationRepository = mock(LocationRepository.class);
        locationMapper = mock(LocationMapper.class);

        locationService = new LocationServiceImpl(locationRepository, locationMapper);
    }
    @Test
    @DisplayName("Null reference should not be returned by the getLocations() method")
    void getLocationsShouldNotReturnNull() {
        assertNotNull(locationService.getLocations());
    }

    @Test
    @DisplayName("Expected list of location's DTOs should be returned by the getLocations() method")
    void getLocationsShouldReturnExpectedLocationDTOs() {
        Location rzeszowLocation = new Location("Rzeszow");
        LocationDTO rzeszowLocationDTO = new LocationDTO("Rzeszow");

        Location wroclawLocation = new Location("Wroclaw");
        LocationDTO wroclawLocationDTO = new LocationDTO("Wroclaw");

        List<Location> persistedLocation = List.of(rzeszowLocation, wroclawLocation);
        Set<LocationDTO> expectedLocationDTOs = Set.of(rzeszowLocationDTO, wroclawLocationDTO);

        when(locationRepository.findAll()).thenReturn(persistedLocation);
        when(locationMapper.toDto(wroclawLocation)).thenReturn(wroclawLocationDTO);
        when(locationMapper.toDto(rzeszowLocation)).thenReturn(rzeszowLocationDTO);

        assertEquals(expectedLocationDTOs, locationService.getLocations());
    }

    @Test
    @DisplayName("List of locations from getLocations() should match the locations fetched from the locationRepository")
    void getLocationsShouldMatchTheLocationsFromLocationRepository() {
        Location persistedLocation = new Location("Katowice");
        Set<LocationDTO> expectedLocationDTOs = Set.of(new LocationDTO("Katowice"));

        when(locationRepository.findAll()).thenReturn(List.of(persistedLocation));
        when(locationMapper.toDto(persistedLocation)).thenReturn(new LocationDTO("Katowice"));

        assertEquals(expectedLocationDTOs, locationService.getLocations());
    }
}
