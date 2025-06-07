package com.greenguard.green_guard_application.restController;

import com.greenguard.green_guard_application.model.dto.ReadingDTO;
import com.greenguard.green_guard_application.model.dto.ReadingFilterDTO;
import com.greenguard.green_guard_application.service.ReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
@CrossOrigin(origins = "http://localhost:3000")
public class ReadingRestController {
    private final ReadingService readingService;

    @Autowired
    public ReadingRestController(ReadingService readingService) {
        this.readingService = readingService;
    }


    @GetMapping("/reading")
    public ResponseEntity<List<ReadingDTO>> getReadings(@ModelAttribute ReadingFilterDTO readingFilterDTO) {
       readingFilterDTO.setSensorOwnerUsername(SecurityContextHolder.getContext().getAuthentication().getName());
       return ResponseEntity.ok(readingService.findAll(readingFilterDTO));
    }

    @GetMapping("/reading/last")
    public ResponseEntity<ReadingDTO> getLastReading(@ModelAttribute ReadingFilterDTO readingFilterDTO) {
        readingFilterDTO.setSensorOwnerUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        return ResponseEntity.ok(readingService.findLastReading(readingFilterDTO));
    }

    @GetMapping("/reading/favorite-locations")
    public ResponseEntity<List<ReadingDTO>> getFavoriteLocationReading(@ModelAttribute ReadingFilterDTO readingFilterDTO) {
        readingFilterDTO.setSensorOwnerUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        return ResponseEntity.ok(readingService.getFavoriteLocationReadings(readingFilterDTO));
    }
}
