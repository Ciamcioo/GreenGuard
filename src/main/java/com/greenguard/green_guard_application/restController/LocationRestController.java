package com.greenguard.green_guard_application.restController;

import com.greenguard.green_guard_application.model.dto.LocationDTO;
import com.greenguard.green_guard_application.service.LocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("api")
public class LocationRestController {
    private final LocationService locationService;

    public LocationRestController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/locations")
    public ResponseEntity<Set<LocationDTO>> getLocations() {
        return ResponseEntity.ok(locationService.getLocations());

    }
}
