package com.greenguard.green_guard_application.model.dto;

import java.time.LocalDateTime;

public class ReadingFilterDTO {
    private String sensorName;
    private String sensorOwnerUsername;
    private Double temperatureFrom;
    private Double temperatureTo;
    private Double humidityFrom;
    private Double humidityTo;
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;
    private String locationName;


    public ReadingFilterDTO() {
    }

    public ReadingFilterDTO(String sensorName, String sensorOwnerUsername, Double temperatureFrom, Double temperatureTo, Double humidityFrom,
                            Double humidityTo, LocalDateTime dateFrom, LocalDateTime dateTo, String locationName) {
        this.sensorName = sensorName;
        this.sensorOwnerUsername = sensorOwnerUsername;
        this.temperatureFrom = temperatureFrom;
        this.temperatureTo = temperatureTo;
        this.humidityFrom = humidityFrom;
        this.humidityTo = humidityTo;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.locationName = locationName;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public String getSensorOwnerUsername() {
        return sensorOwnerUsername;
    }

    public void setSensorOwnerUsername(String sensorOwnerUsername) {
        this.sensorOwnerUsername = sensorOwnerUsername;
    }

    public Double getTemperatureFrom() {
        return temperatureFrom;
    }

    public void setTemperatureFrom(Double temperatureFrom) {
        this.temperatureFrom = temperatureFrom;
    }

    public Double getTemperatureTo() {
        return temperatureTo;
    }

    public void setTemperatureTo(Double temperatureTo) {
        this.temperatureTo = temperatureTo;
    }

    public Double getHumidityFrom() {
        return humidityFrom;
    }

    public void setHumidityFrom(Double humidityFrom) {
        this.humidityFrom = humidityFrom;
    }

    public Double getHumidityTo() {
        return humidityTo;
    }

    public void setHumidityTo(Double humidityTo) {
        this.humidityTo = humidityTo;
    }

    public LocalDateTime getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDateTime dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDateTime getDateTo() {
        return dateTo;
    }

    public void setDateTo(LocalDateTime dateTo) {
        this.dateTo = dateTo;
    }

    public String getLocationName() {
        return this.locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}
