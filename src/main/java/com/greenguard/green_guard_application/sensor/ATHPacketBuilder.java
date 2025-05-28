package com.greenguard.green_guard_application.sensor;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.IOException;

import java.util.Arrays;

import com.greenguard.green_guard_application.sensor.CommonPacket;

/* Air Temperature and Humidity packet builder and parser */
public class ATHPacketBuilder {

    public static final byte GET_DHT22_TYPE = 0x60;
    public static final int  GET_DHT22_PACKET_LENGTH = CommonPacket.COMMON_PACKET_LENGTH;

    public static final byte REPLY_DHT22_TYPE = 0x61;

    public static final int REPLY_DHT22_SUCCESS_SIZE     = 0x1;
    public static final int REPLY_DHT22_TEMPERATURE_SIZE = 0x4;
    public static final int REPLY_DHT22_HUMIDITY_SIZE    = 0x4;

    public static final int REPLY_DHT22_SUCCESS_POS =
        CommonPacket.COMMON_PACKET_TYPE_POS + 1;

    public static final int REPLY_DHT22_TEMPERATURE_POS = REPLY_DHT22_SUCCESS_POS
        + REPLY_DHT22_SUCCESS_SIZE;

    public static final int REPLY_DHT22_HUMIDITY_POS = REPLY_DHT22_TEMPERATURE_POS
        + REPLY_DHT22_TEMPERATURE_SIZE;

    public static final int REPLY_DHT22_PACKET_LENGTH = CommonPacket.COMMON_PACKET_LENGTH
        + REPLY_DHT22_SUCCESS_SIZE
        + REPLY_DHT22_TEMPERATURE_SIZE
        + REPLY_DHT22_HUMIDITY_SIZE;

    public static final int REPLY_DHT22_DATA_SIZE =  REPLY_DHT22_TEMPERATURE_SIZE
        + REPLY_DHT22_HUMIDITY_SIZE;

    public static final int REPLY_DHT22_PACKET_LENGTH_NO_SUCCESS =
        CommonPacket.COMMON_PACKET_LENGTH
        + REPLY_DHT22_SUCCESS_SIZE;

    public static final byte REPLY_DHT22_NO_SUCCESS = 0x0;
    public static final byte REPLY_DHT22_SUCCESS    = 0x1;

    public static final byte DHT22_READ_SUCCESS = 0x1;
    public static final byte DHT22_READ_FAILURE = 0x0;



    public static class ATHData {
        public final boolean valid;
        public final float   temperature;
        public final float   humidity;

        public ATHData(boolean valid, float temperature, float humidity) {
            this.valid       = valid;
            this.temperature = temperature;
            this.humidity    = humidity;
        }

        public static ATHData invalid()
        {
          return new ATHData(false, (float)0.0, (float)0.0);
        }

        @Override
        public String toString() {
            return " { valid=" + this.valid       +
              ", temperature=" + this.temperature +
              ", humidity="    + this.humidity    +
              " }";
        }

        public boolean isValid()
        {
            return this.valid;
        }
    }

    public static byte[] buildATHRequest() {
        ByteBuffer buffer = ByteBuffer.allocate(GET_DHT22_PACKET_LENGTH);
        buffer.order(ByteOrder.BIG_ENDIAN);

        buffer.putShort((short) GET_DHT22_PACKET_LENGTH);
        buffer.put((byte) GET_DHT22_TYPE);

        return buffer.array();
    }

    public static boolean sendATHRequest(OutputStream out) {
        byte[] req = buildATHRequest();
        try {
            out.write(req);
            out.flush();
            return true;
        } catch (IOException e) {
            System.err.println("Failed to send ATH request: " + e.getMessage());
            return false;
        }
    }

    public static ATHData readATHReply(InputStream in) {
        ByteBuffer buffer = ByteBuffer.allocate(REPLY_DHT22_PACKET_LENGTH);

        if(CommonPacket.readSome(in, buffer, CommonPacket.COMMON_PACKET_LENGTH,
              CommonPacket.READSOME_MS) == false)
            return null;

        int packetLength = CommonPacket.extractLength(buffer.array());
        byte type = CommonPacket.extractType(buffer.array());

        if (packetLength < CommonPacket.COMMON_PACKET_LENGTH)
            return null;

        // NACK?
        if (type == NackPacket.NACK_TYPE) {
            System.out.println("Received NACK");
            return ATHData.invalid();
        }

        if (type != REPLY_DHT22_TYPE) {
            System.err.printf("Invalid packet type: %02X%n", type);
            return ATHData.invalid();
        }


        if(CommonPacket.readSome(in, buffer, REPLY_DHT22_SUCCESS_SIZE,
              CommonPacket.READSOME_MS) == false)
            return null;

        buffer.position(REPLY_DHT22_SUCCESS_POS);

        byte success = buffer.get();
        if (success != REPLY_DHT22_SUCCESS) {
            System.err.println("DHT22 transaction error");
            return ATHData.invalid();
        }

        buffer.position(REPLY_DHT22_TEMPERATURE_POS);

        if(CommonPacket.readSome(in, buffer, REPLY_DHT22_DATA_SIZE,
              CommonPacket.READSOME_MS) == false)
            return null;

        buffer.position(REPLY_DHT22_TEMPERATURE_POS);

        int tempRaw = buffer.getInt();
        int humRaw  = buffer.getInt();

        float temp = tempRaw / 100.0f;
        float hum  = humRaw / 100.0f;

        return new ATHData(true, temp, hum);
    }
}

