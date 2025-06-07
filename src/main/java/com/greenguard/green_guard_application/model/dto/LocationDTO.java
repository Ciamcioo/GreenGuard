package com.greenguard.green_guard_application.model.dto;

import java.util.Objects;

public record LocationDTO(String name) {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LocationDTO that = (LocationDTO) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public String toString() {
        return "LocationDTO{" +
                "name='" + name + '\'' +
                '}';
    }
}
