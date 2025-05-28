package com.greenguard.green_guard_application.model.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "sensors")
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "username")
    private String username;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "mac_address")
    private String macAddress;

    @Column(name = "is_active")
    private Boolean active;

    @OneToMany(
               mappedBy = "sensor",
               cascade = CascadeType.REMOVE,
               orphanRemoval = true
    )
    List<Reading> readings  = new ArrayList<>();

    public Sensor() {}

    public Sensor(String name, String username, String ipAddress, String macAddress, Boolean active) {
        this.name = name;
        this.username = username;
        this.ipAddress = ipAddress;
        this.macAddress = macAddress;
        this.active = active;
    }

    public Sensor(UUID id, String name, String username, String ipAddress, String macAddress, Boolean active) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.ipAddress = ipAddress;
        this.macAddress = macAddress;
        this.active = active;
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<Reading> getReadings() {
        return readings;
    }

    public void setReadings(List<Reading> readings) {
        this.readings = readings;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Sensor sensor = (Sensor) o;
        return Objects.equals(id, sensor.id) && Objects.equals(name, sensor.name) && Objects.equals(macAddress, sensor.macAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, macAddress);
    }
}
