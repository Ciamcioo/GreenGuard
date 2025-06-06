package com.greenguard.green_guard_application.restController;

import com.greenguard.green_guard_application.aspect.annotation.EnableMethodLog;
import com.greenguard.green_guard_application.model.dto.SensorDTO;
import com.greenguard.green_guard_application.model.dto.SingleFieldRequestDTO;
import com.greenguard.green_guard_application.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
@CrossOrigin(origins = "http://localhost:3000") // as soon as I will configure the spring security filter I will disable it
public class SensorRestController {
    private final SensorService sensorService;

    @Autowired
    public SensorRestController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @GetMapping("/sensor")
    public ResponseEntity<List<SensorDTO>> getSensors() {
        return ResponseEntity.ok(
            sensorService.getSensors(SecurityContextHolder.getContext().getAuthentication().getName())
        );
    }
    @GetMapping("/sensor/{name}")
    @EnableMethodLog
    public ResponseEntity<SensorDTO> getSensor(@PathVariable("name") String name) {
        return new ResponseEntity<>(
            sensorService.getSensor(SecurityContextHolder.getContext().getAuthentication().getName(), name),
            HttpStatus.OK
        );
    }

    @DeleteMapping("/sensor/delete/{name}")
    @EnableMethodLog
    public ResponseEntity<Void> deleteSensor(@PathVariable("name") String name) {
        sensorService.deleteSensor(SecurityContextHolder.getContext().getAuthentication().getName(), name);

        return new ResponseEntity<>(
            HttpStatus.OK
        );
    }

    @PutMapping("/sensor/update/{name}")
    @EnableMethodLog
    public ResponseEntity<Void> updateSensorName(@PathVariable("name") String name,
                                                 @RequestBody SingleFieldRequestDTO<String> newName) {
        sensorService.updateSensorName(SecurityContextHolder.getContext().getAuthentication().getName(), name, newName.getValue());

        return new ResponseEntity<>(
            HttpStatus.OK
        );
    }

    @PostMapping("/sensor")
    public ResponseEntity<String> addSensor(@RequestBody SensorDTO sensorDTO) {
        return new ResponseEntity<>(
                sensorService.addSensor(SecurityContextHolder.getContext().getAuthentication().getName(), sensorDTO),
                HttpStatus.CREATED
        );
    }
}
