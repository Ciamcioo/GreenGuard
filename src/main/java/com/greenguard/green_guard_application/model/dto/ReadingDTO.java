package com.greenguard.green_guard_application.model.dto;


import java.time.Instant;

public record ReadingDTO(
    String sensorName,
    Double temperature,
    Double humidity,
    String locationName,
    Instant timestamp
) {

}
