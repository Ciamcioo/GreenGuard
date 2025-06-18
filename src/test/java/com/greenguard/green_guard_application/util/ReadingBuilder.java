package com.greenguard.green_guard_application.util;

import com.greenguard.green_guard_application.model.dto.ReadingDTO;
import com.greenguard.green_guard_application.model.entity.Location;
import com.greenguard.green_guard_application.model.entity.Reading;
import com.greenguard.green_guard_application.model.entity.Sensor;
import com.greenguard.green_guard_application.model.entity.User;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

public class ReadingBuilder {
    public static final Sensor   DEFAULT_SENSOR      = new Sensor(null, "test sensor", new User("test_username", "password"),
                                                    "127.0.0.1", "4A:9F:2C:75:B1:E3", new Location("test_location"), false);
    public static final Double   DEFAULT_TEMPERATURE = 20.0;
    public static final Double   DEFAULT_HUMIDITY    = 20.0;
    public static final Instant  DEFAULT_TIMESTAMP   = LocalDateTime.of(2000, 1, 1, 12, 0).toInstant(ZoneOffset.UTC);

    private UUID      id;
    private Sensor    sensor;
    private Double    temperature;
    private Double    humidity;
    private Instant   timestamp;

    private ReadingBuilder() { }

    public static ReadingBuilder getInstance() {
        ReadingBuilder readingBuilder = new ReadingBuilder();
        readingBuilder = readingBuilder.withDefaultValues();
        return readingBuilder;
    }

    public ReadingBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public ReadingBuilder withSensor(Sensor sensor) {
        this.sensor = sensor;
        return this;
    }

    public ReadingBuilder withTemperature(Double temperature) {
        this.temperature = temperature;
        return this;
    }

    public ReadingBuilder withHumidity(Double humidity) {
        this.humidity= humidity;
        return this;
    }

    public ReadingBuilder withTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public ReadingBuilder withDefaultValues() {
        this.id = UUID.randomUUID();
        this.sensor = DEFAULT_SENSOR;
        this.temperature = DEFAULT_TEMPERATURE;
        this.humidity = DEFAULT_HUMIDITY;
        this.timestamp = DEFAULT_TIMESTAMP;
        return this;
    }

    public Reading buildReading() {
        return new Reading(this.id,
                           this.sensor,
                           this.temperature,
                           this.humidity,
                           this.timestamp);

    }

    public ReadingDTO buildReadingDTO() {
        return new ReadingDTO(this.sensor.getName(),
                              this.temperature,
                              this.humidity,
                              this.sensor.getLocation().getName(),
                              this.timestamp);

    }
}
