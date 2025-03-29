package com.greenguard.green_guard_application.service.exception;

public class SensorAlreadyExistsException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Sensor with given ip address already exists!";

    public SensorAlreadyExistsException() {
        super(DEFAULT_MESSAGE);
    }

    public SensorAlreadyExistsException(String message) {
        super(message);
    }
}
