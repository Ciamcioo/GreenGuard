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

    @ManyToOne
    @JoinColumn(name = "username")
    private User user;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "mac_address")
    private String macAddress;

    @ManyToOne
    @JoinColumn(name = "location")
    private Location location;

    @Column(name = "is_active")
    private Boolean active;

    @OneToMany(
               mappedBy = "sensor",
               cascade = CascadeType.REMOVE,
               orphanRemoval = true
    )
    List<Reading> readings  = new ArrayList<>();

    public Sensor() {}

    public Sensor(String name, User user, String ipAddress, String macAddress, Location location, Boolean active) {
        this.name = name;
        this.user = user;
        this.ipAddress = ipAddress;
        this.macAddress = macAddress;
        this.location = location;
        this.active = active;
    }

    public Sensor(UUID id, String name, User user, String ipAddress, String macAddress, Location location,   Boolean active) {
        this.id = id;
        this.name = name;
        this.user = user;
        this.ipAddress = ipAddress;
        this.macAddress = macAddress;
        this.location = location;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
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
