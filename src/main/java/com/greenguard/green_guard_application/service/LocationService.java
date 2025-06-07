package com.greenguard.green_guard_application.service;

import com.greenguard.green_guard_application.model.dto.LocationDTO;

import java.util.Set;

public interface LocationService {

    Set<LocationDTO> getLocations();
}
