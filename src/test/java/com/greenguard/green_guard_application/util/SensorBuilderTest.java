package com.greenguard.green_guard_application.util;

import com.greenguard.green_guard_application.model.dto.SensorDTO;
import com.greenguard.green_guard_application.model.entity.Location;
import com.greenguard.green_guard_application.model.entity.Sensor;
import com.greenguard.green_guard_application.model.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class SensorBuilderTest {

    // constant field
    private static final String   SENSOR_NAME   = "some sensor name";
    private static final String   USERNAME      = "some username";
    private static final String   PASSWORD      = "password";
    private static final String   IP            = "127.0.0.1";
    private static final String   MAC           = "AA:BB:CC:DD:EE";
    private static final String   LOCATION_NAME = "some location name";
    private static final Boolean  ACTIVE_STATE  = false ;

    //  tested field
    private SensorBuilder sensorBuilder;

    @BeforeEach
    void setup() {
        sensorBuilder = SensorBuilder.getInstance();
        sensorBuilder = sensorBuilder.withId(UUID.randomUUID())
                .withName(SENSOR_NAME)
                .withUser(new User(USERNAME, PASSWORD))
                .withIpAddress(IP)
                .withMac(MAC)
                .withLocation(new Location(LOCATION_NAME))
                .withActive(ACTIVE_STATE);
    }

    @Test
    @DisplayName
    ("Method buildSensor should return the sensor with default values even if on the object is not called withDefaultValues()")
    void buildSensorShouldNotContainNullValues() {
        Sensor resultSensor = SensorBuilder.getInstance().buildSensor();

        assertAll(
                () -> assertInstanceOf(UUID.class, resultSensor.getId()),
                () -> assertEquals(SensorBuilder.DEFAULT_NAME,             resultSensor.getName()),
                () -> assertEquals(SensorBuilder.DEFAULT_USER,             resultSensor.getUser()),
                () -> assertEquals(SensorBuilder.DEFAULT_IP_ADDRESS,       resultSensor.getIpAddress()),
                () -> assertEquals(SensorBuilder.DEFAULT_MAC_ADDRESS,      resultSensor.getMacAddress()),
                () -> assertEquals(SensorBuilder.DEFAULT_LOCATION,         resultSensor.getLocation()),
                () -> assertEquals(SensorBuilder.DEFAULT_ACTIVATION_STATE, resultSensor.getActive())
        );
    }

    @Test
    @DisplayName
    ("Method buildSensor should return the sensorDTO with default values even if on the object is not called withDefaultValues()")
    void buildSensorDTOShouldNotContainNullValues() {
        SensorDTO resultSensorDTO = SensorBuilder.getInstance().buildSensorDTO();

        assertAll(
                () -> assertEquals(SensorBuilder.DEFAULT_NAME,             resultSensorDTO.name()),
                () -> assertEquals(SensorBuilder.DEFAULT_IP_ADDRESS,       resultSensorDTO.ipAddress()),
                () -> assertEquals(SensorBuilder.DEFAULT_LOCATION_NAME,    resultSensorDTO.locationName()),
                () -> assertEquals(SensorBuilder.DEFAULT_ACTIVATION_STATE, resultSensorDTO.active())
        );
    }

    @Test
    @DisplayName("Method buildSensor should build the sensor object with default values")
    void buildSensorWithDefaultValues() {
        sensorBuilder = sensorBuilder.withDefaultValues();
        Sensor resultSensor = sensorBuilder.buildSensor();

        assertAll(
                () -> assertInstanceOf(UUID.class, resultSensor.getId()),
                () -> assertEquals(SensorBuilder.DEFAULT_NAME,             resultSensor.getName()),
                () -> assertEquals(SensorBuilder.DEFAULT_USER,             resultSensor.getUser()),
                () -> assertEquals(SensorBuilder.DEFAULT_IP_ADDRESS,       resultSensor.getIpAddress()),
                () -> assertEquals(SensorBuilder.DEFAULT_MAC_ADDRESS,      resultSensor.getMacAddress()),
                () -> assertEquals(SensorBuilder.DEFAULT_LOCATION,         resultSensor.getLocation()),
                () -> assertEquals(SensorBuilder.DEFAULT_ACTIVATION_STATE, resultSensor.getActive())
        );
    }

    @Test
    @DisplayName("Method buildSensor should build the sensorDto record with default values")
    void buildSensorDtoWithDefaultValues() {
        sensorBuilder = sensorBuilder.withDefaultValues();
        SensorDTO resultSensorDTO = sensorBuilder.buildSensorDTO();

        assertAll(
                () -> assertEquals(SensorBuilder.DEFAULT_NAME,             resultSensorDTO.name()),
                () -> assertEquals(SensorBuilder.DEFAULT_IP_ADDRESS,       resultSensorDTO.ipAddress()),
                () -> assertEquals(SensorBuilder.DEFAULT_LOCATION_NAME,    resultSensorDTO.locationName()),
                () -> assertEquals(SensorBuilder.DEFAULT_ACTIVATION_STATE, resultSensorDTO.active())
        );
    }

    @Test
    @DisplayName("Method buildSensor should build the sensor object with the set values")
    void buildSensorWithSetValues() {
        Sensor resultSensor = sensorBuilder.buildSensor();

        assertAll(
                () -> assertInstanceOf(UUID.class, resultSensor.getId()),
                () -> assertEquals(SENSOR_NAME,                  resultSensor.getName()),
                () -> assertEquals(new User(USERNAME, PASSWORD), resultSensor.getUser()),
                () -> assertEquals(IP,                           resultSensor.getIpAddress()),
                () -> assertEquals(MAC,                          resultSensor.getMacAddress()),
                () -> assertEquals(new Location(LOCATION_NAME),  resultSensor.getLocation()),
                () -> assertEquals(ACTIVE_STATE,                 resultSensor.getActive())
        );
    }

    @Test
    @DisplayName("Method buildSensor should build the sensorDTO record with the set value")
    void buildSensorDTOWithSetValues() {
        SensorDTO resultSensorDTO = sensorBuilder.buildSensorDTO();

        assertAll(
                () -> assertEquals(SENSOR_NAME,   resultSensorDTO.name()),
                () -> assertEquals(IP,            resultSensorDTO.ipAddress()),
                () -> assertEquals(LOCATION_NAME, resultSensorDTO.locationName()),
                () -> assertEquals(ACTIVE_STATE,  resultSensorDTO.active())
        );
    }


}
