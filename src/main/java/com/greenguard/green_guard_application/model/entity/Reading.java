package com.greenguard.green_guard_application.model.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "readings")
public class Reading {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    private Sensor sensor;

    @Column(name = "temperature")
    private Double temperature;

    @Column(name = "humidity")
    private Double humidity;

    @Column(name = "capture_time")
    private LocalDate readingTimestamp;

    public Reading() {
    }

    public Reading(Sensor sensor, Double temperature, Double humidity, LocalDate readingTimestamp) {
        this.sensor = sensor;
        this.temperature = temperature;
        this.humidity = humidity;
        this.readingTimestamp = readingTimestamp;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public LocalDate getReadingTimestamp() {
        return readingTimestamp;
    }

    public void setReadingTimestamp(LocalDate readingTimestamp) {
        this.readingTimestamp = readingTimestamp;
    }

    @Override
    public String toString() {
        return "Reading{" +
                "sensor=" + sensor +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", readingTimestamp=" + readingTimestamp +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Reading reading = (Reading) o;
        return Objects.equals(id, reading.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
