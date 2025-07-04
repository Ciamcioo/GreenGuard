package com.greenguard.green_guard_application.service.mapper;

import com.greenguard.green_guard_application.model.dto.ReadingDTO;
import com.greenguard.green_guard_application.model.entity.Reading;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ReadingMapper {


    @Mapping(target = "sensorName",  source = "entity.sensor.name")
    @Mapping(target = "temperature", source = "entity.temperature")
    @Mapping(target = "humidity",    source = "entity.humidity")
    @Mapping(target = "locationName", source = "entity.sensor.location.name")
    @Mapping(target = "timestamp",   source = "entity.timestamp")
    ReadingDTO toDto(Reading entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sensor", ignore = true)
    @Mapping(target = "temperature", source = "dto.temperature")
    @Mapping(target = "humidity", source = "dto.humidity")
    @Mapping(target = "timestamp", source = "dto.timestamp")
    Reading toEntity(ReadingDTO dto);
}
