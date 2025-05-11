package com.greenguard.green_guard_application.sensor;

import com.greenguard.green_guard_application.sensor.CommonPacket;

public class NackPacket {
    public static final byte NACK_TYPE = 0x01;
    public static final int NACK_PACKET_LENGTH = CommonPacket.COMMON_PACKET_LENGTH;
}

