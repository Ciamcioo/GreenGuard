package com.greenguard.green_guard_application.model.dto;

import java.util.Objects;

public record SensorDTO(
        String name,
        String ipAddress,
        Boolean active
) {

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SensorDTO sensorDTO = (SensorDTO) o;

        return Objects.equals(name, sensorDTO.name) &&
               Objects.equals(ipAddress, sensorDTO.ipAddress) &&
               Objects.equals(active, sensorDTO.active);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, ipAddress, active);
    }

    @Override
    public String toString() {
        return "SensorDTO{" +
                "name='" + name + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", active=" + active +
                '}';
    }
}
