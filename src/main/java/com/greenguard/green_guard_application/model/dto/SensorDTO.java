package com.greenguard.green_guard_application.model.dto;

import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public record SensorDTO(
        String name,
        String ipAddress,
        String macAddress,
        Boolean active
) {

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SensorDTO sensorDTO = (SensorDTO) o;
        return Objects.equals(name, sensorDTO.name) && Objects.equals(ipAddress, sensorDTO.ipAddress) && Objects.equals(active, sensorDTO.active) && Objects.equals(macAddress, sensorDTO.macAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, ipAddress, macAddress, active);
    }

    @Override
    public String toString() {
        return "SensorDTO{" +
                "name='" + name + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", macAddress='" + macAddress + '\'' +
                ", active=" + active +
                '}';
    }
}
