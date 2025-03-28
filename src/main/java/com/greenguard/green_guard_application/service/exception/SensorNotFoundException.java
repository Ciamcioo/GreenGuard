package com.greenguard.green_guard_application.service.exception;

public class SensorNotFoundException extends RuntimeException {
    private static final String SENSOR_NOT_FOUND_MESSAGE_FORMAT = "Sensor with name: %s not found!";
    public SensorNotFoundException(String sensorName) {
        super(String.format(SENSOR_NOT_FOUND_MESSAGE_FORMAT, sensorName));
    }
}
