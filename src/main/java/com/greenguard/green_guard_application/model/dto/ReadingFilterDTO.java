package com.greenguard.green_guard_application.model.dto;

import java.time.LocalDateTime;

public class ReadingFilterDTO {
    private String sensorName;
    private Double temperatureFrom;
    private Double temperatureTo;
    private Double humidityFrom;
    private Double humidityTo;
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;


    public ReadingFilterDTO() {
    }

    public ReadingFilterDTO(String sensorName, Double temperatureFrom, Double temperatureTo, Double humidityFrom,
                            Double humidityTo, LocalDateTime dateFrom, LocalDateTime dateTo) {
        this.sensorName = sensorName;
        this.temperatureFrom = temperatureFrom;
        this.temperatureTo = temperatureTo;
        this.humidityFrom = humidityFrom;
        this.humidityTo = humidityTo;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
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
}
