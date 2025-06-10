package com.greenguard.green_guard_application.service;

import com.greenguard.green_guard_application.model.dto.ReadingDTO;
import com.greenguard.green_guard_application.model.dto.ReadingFilterDTO;
import com.greenguard.green_guard_application.model.entity.Sensor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface ReadingService {

   List<ReadingDTO> findAll(ReadingFilterDTO readingFilterDTO);

   ReadingDTO findLastReading(ReadingFilterDTO readingFilterDTO);

   List<ReadingDTO> getFavoriteLocationReadings(ReadingFilterDTO readingFilterDTO);

   Instant addReading(UUID sensorID, ReadingDTO readingDTO);
}
