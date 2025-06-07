package com.greenguard.green_guard_application.service.mapper;

import com.greenguard.green_guard_application.model.dto.LocationDTO;
import com.greenguard.green_guard_application.model.entity.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface LocationMapper {

    @Mapping(target = "name", source = "entity.name")
    LocationDTO toDto(Location entity);
}
