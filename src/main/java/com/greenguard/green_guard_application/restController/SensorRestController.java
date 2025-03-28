package com.greenguard.green_guard_application.restController;

import com.greenguard.green_guard_application.aspect.annotation.EnableMethodLog;
import com.greenguard.green_guard_application.model.dto.SensorDTO;
import com.greenguard.green_guard_application.service.SensorService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
public class SensorRestController {
    private SensorService sensorService;

    @Autowired
    public SensorRestController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @GetMapping("/sensor/{name}")
    @EnableMethodLog
    public ResponseEntity<SensorDTO> getSensor(@PathVariable("name") String name) {
        return new ResponseEntity<>(
            sensorService.getSensor(name),
            HttpStatus.OK
        );
    }

    @PostMapping("/sensor")
    public ResponseEntity<String> addSensor(@RequestBody SensorDTO sensorDTO) {
        return new ResponseEntity<>(
                sensorService.addSensor(null),
                HttpStatus.CREATED
        );
    }
}
