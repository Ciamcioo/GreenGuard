package com.greenguard.green_guard_application.service;

import com.greenguard.green_guard_application.model.dto.ReadingDTO;
import com.greenguard.green_guard_application.model.dto.ReadingFilterDTO;

import java.util.List;

public interface ReadingService {

   List<ReadingDTO> findAll(ReadingFilterDTO readingFilterDTO);

   ReadingDTO findLastReading(ReadingFilterDTO readingFilterDTO);

   List<ReadingDTO> getFavoriteLocationReadings(ReadingFilterDTO readingFilterDTO);
}
