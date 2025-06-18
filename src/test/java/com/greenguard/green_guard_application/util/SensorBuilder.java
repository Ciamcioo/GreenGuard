package com.greenguard.green_guard_application.util;

import com.greenguard.green_guard_application.model.dto.SensorDTO;
import com.greenguard.green_guard_application.model.entity.Location;
import com.greenguard.green_guard_application.model.entity.Sensor;
import com.greenguard.green_guard_application.model.entity.User;

import java.util.UUID;

public class SensorBuilder {
    public static final String   DEFAULT_NAME             = "default sensor name";
    public static final User     DEFAULT_USER             = new User("username", "password");
    public static final String   DEFAULT_IP_ADDRESS       = "127.0.0.1";
    public static final String   DEFAULT_MAC_ADDRESS      = "AA:BB:CC:DD:EE";
    public static final String   DEFAULT_LOCATION_NAME    = "default location name";
    public static final Location DEFAULT_LOCATION         = new Location(DEFAULT_LOCATION_NAME);
    public static final Boolean  DEFAULT_ACTIVATION_STATE = true;


    private UUID     id;
    private String   name;
    private User     user;
    private String   ipAddress;
    private String   macAddress;
    private Location location;
    private Boolean  active;

    public static SensorBuilder getInstance() {
        return new SensorBuilder().withDefaultValues();
    }

    public SensorBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public SensorBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public SensorBuilder withUser(User user) {
        this.user = user;
        return this;
    }

    public SensorBuilder withIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
        return this;
    }

    public SensorBuilder withMac(String macAddress) {
        this.macAddress = macAddress;
        return this;
    }

    public SensorBuilder withLocation(Location location) {
        this.location = location;
        return this;
    }

    public SensorBuilder withActive(Boolean active) {
        this.active = active;
        return this;
    }

    public SensorBuilder withDefaultValues() {
        this.id         = UUID.randomUUID();
        this.name       = DEFAULT_NAME;
        this.user       = DEFAULT_USER;
        this.ipAddress  = DEFAULT_IP_ADDRESS;
        this.macAddress = DEFAULT_MAC_ADDRESS;
        this.location   = DEFAULT_LOCATION;
        this.active     = DEFAULT_ACTIVATION_STATE;

        return this;
    }

//    public SensorBuilder withReadings(List<Reading> readings) {
//        this.readingList = readings;
//        return this;
//    }

    public Sensor buildSensor() {
        return new Sensor(id, name,  user, ipAddress, macAddress, location, active);
    }

    public SensorDTO buildSensorDTO() {
        return new SensorDTO(name, ipAddress, location.getName(), active);
    }


}
