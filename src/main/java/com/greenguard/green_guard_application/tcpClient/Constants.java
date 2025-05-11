public class Constants {
    // Common packet constants
    public static final int COMMON_PACKET_LENGTH_POS = 0x0;
    public static final int COMMON_PACKET_TYPE_POS = 0x2;

    public static final int COMMON_PACKET_LENGTH_SIZE = 0x2;
    public static final int COMMON_PACKET_TYPE_SIZE = 0x1;

    public static final int COMMON_PACKET_LENGTH = COMMON_PACKET_LENGTH_SIZE
                                                 + COMMON_PACKET_TYPE_SIZE;

    // NACK packet type
    public static final byte NACK_TYPE = 0x01;
    public static final int NACK_PACKET_LENGTH = COMMON_PACKET_LENGTH;

    // GET_DHT22 packet type
    public static final byte GET_DHT22_TYPE = 0x60;
    public static final int GET_DHT22_PACKET_LENGTH = COMMON_PACKET_LENGTH;

    // REPLY_DHT22 packet type
    public static final byte REPLY_DHT22_TYPE = 0x61;
    public static final int REPLY_DHT22_SUCCESS_SIZE = 0x1;
    public static final int REPLY_DHT22_TEMPERATURE_SIZE = 0x4;
    public static final int REPLY_DHT22_HUMIDITY_SIZE = 0x4;

    public static final int REPLY_DHT22_SUCCESS_POS = COMMON_PACKET_TYPE_POS + 1;
    public static final int REPLY_DHT22_TEMPERATURE_POS = REPLY_DHT22_SUCCESS_POS + REPLY_DHT22_SUCCESS_SIZE;
    public static final int REPLY_DHT22_HUMIDITY_POS = REPLY_DHT22_TEMPERATURE_POS + REPLY_DHT22_TEMPERATURE_SIZE;

    public static final int REPLY_DHT22_PACKET_LENGTH = COMMON_PACKET_LENGTH
                                                      + REPLY_DHT22_SUCCESS_SIZE
                                                      + REPLY_DHT22_TEMPERATURE_SIZE
                                                      + REPLY_DHT22_HUMIDITY_SIZE;

    public static final int REPLY_DHT22_PACKET_LENGTH_NO_SUCCESS = COMMON_PACKET_LENGTH + REPLY_DHT22_SUCCESS_SIZE;
    public static final byte REPLY_DHT22_NO_SUCCESS = 0x0;
    public static final byte REPLY_DHT22_SUCCESS = 0x1;

    public static final byte DHT22_READ_SUCCESS = 0x1;
    public static final byte DHT22_READ_FAILURE = 0x0;
}

