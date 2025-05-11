package com.greenguard.green_guard_application.sensor;

import java.nio.ByteBuffer;
import java.io.InputStream;
import java.io.IOException;

public final class CommonPacket {

    public static final int READSOME_MS = 5000;

    // Common packet positions
    public static final int COMMON_PACKET_LENGTH_POS = 0x0;
    public static final int COMMON_PACKET_TYPE_POS = 0x2;

    // Common packet sizes
    public static final int COMMON_PACKET_LENGTH_SIZE = 0x2;
    public static final int COMMON_PACKET_TYPE_SIZE = 0x1;

    // Total common packet length (length field + type field)
    public static final int COMMON_PACKET_LENGTH = COMMON_PACKET_LENGTH_SIZE +
      COMMON_PACKET_TYPE_SIZE;

    private CommonPacket() {}

   public static int extractLength(byte[] buffer) {
        if (buffer.length < COMMON_PACKET_LENGTH)
            throw new IllegalArgumentException("Packet too short for length/type check");

        short len = ByteBuffer.wrap(buffer, COMMON_PACKET_LENGTH_POS, 2).getShort();
        return Short.toUnsignedInt(Short.reverseBytes(len)); // network to host order
    }

    public static byte extractType(byte[] buffer) {
        if (buffer.length < COMMON_PACKET_LENGTH)
            throw new IllegalArgumentException("Packet too short for length/type check");

        return buffer[COMMON_PACKET_TYPE_POS];
    }

    public static boolean readSome(InputStream inputStream,
                                   ByteBuffer buffer,
                                   int length,
                                   int timeoutMs) {
        if (buffer.remaining() < length) return false;

        int totalRead = 0;
        long deadline = System.currentTimeMillis() + timeoutMs;

        try {
            while (totalRead < length) {
                long now = System.currentTimeMillis();
                if (now >= deadline) {
                    return false;
                }

                if (inputStream.available() == 0) {
                    Thread.sleep(5); // brief wait
                    continue;
                }

                // Calculate safe size to read into temp buffer
                int maxChunk = Math.min(inputStream.available(), length - totalRead);
                byte[] tempBuffer = new byte[maxChunk];
                int bytesRead = inputStream.read(tempBuffer, 0, maxChunk);

                if (bytesRead == -1) {
                    return false; // end of stream
                }

                buffer.put(tempBuffer, 0, bytesRead);
                totalRead += bytesRead;
            }

            return true;
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }

}
