package com.greenguard.green_guard_application.service;

import com.greenguard.green_guard_application.model.dto.LocationDTO;
import com.greenguard.green_guard_application.model.entity.Location;
import com.greenguard.green_guard_application.repository.LocationRepository;
import com.greenguard.green_guard_application.service.mapper.LocationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;


    @Autowired
    public LocationServiceImpl(LocationRepository locationRepository, LocationMapper locationMapper) {
        this.locationRepository = locationRepository;
        this.locationMapper = locationMapper;
    }

    @Override
    public Set<LocationDTO> getLocations() {
        List<Location> persistedLocations = locationRepository.findAll();

        return persistedLocations.stream().map(locationMapper::toDto).collect(Collectors.toSet());
    }
}
