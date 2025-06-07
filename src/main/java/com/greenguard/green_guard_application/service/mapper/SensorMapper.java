package com.greenguard.green_guard_application.service.mapper;

import com.greenguard.green_guard_application.model.dto.SensorDTO;
import com.greenguard.green_guard_application.model.entity.Sensor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface SensorMapper {

    @Mapping(target = "name", source = "entity.name")
    @Mapping(target = "ipAddress", source = "entity.ipAddress")
    @Mapping(target = "locationName", source = "entity.location.name")
    @Mapping(target = "active", source = "entity.active")
    SensorDTO toDTO(Sensor entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "name", source = "dto.name")
    @Mapping(target = "ipAddress", source = "dto.ipAddress")
    @Mapping(target = "macAddress", ignore = true)
    @Mapping(target = "active", source = "dto.active")
    @Mapping(target = "location", ignore = true)
    @Mapping(target = "readings", ignore = true)
    Sensor toEntity(SensorDTO dto);




}
