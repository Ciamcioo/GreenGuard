import java.io.*;

import java.net.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class TcpClient {

    private String serverAddress;
    private int port;
    private Socket socket;

    // Constructor to initialize the server address and port
    public TcpClient(String serverAddress, int port) {
        this.serverAddress = serverAddress;
        this.port = port;
    }

    // Helper method to handle byte order conversion
    private static short htons(int value) {
        return (short) ((value >> 8) & 0xFF | (value << 8) & 0xFF00);
    }

    private static short ntohs(int value) {
        return (short) ((value & 0xFF) << 8 | (value >> 8) & 0xFF);
    }

    private static int ntohl(int value) {
        return Integer.reverseBytes(value);
    }

    // Send request
    public void sendRequest() throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(Constants.GET_DHT22_PACKET_LENGTH);
        buf.order(ByteOrder.BIG_ENDIAN);

        short length = Constants.GET_DHT22_PACKET_LENGTH;
        buf.putShort(length);
        buf.put(Constants.GET_DHT22_TYPE);

        hexdump(buf.array(), Constants.GET_DHT22_PACKET_LENGTH);

        // Send packet
        socket.getOutputStream().write(buf.array());
    }

    // Get results
    public void getResults(byte[] buffer) throws IOException {
        int n = socket.getInputStream().read(buffer);
        System.out.println("Received: " + n);
        hexdump(buffer, n);

        if (n < 2) {
            System.out.println("Invalid packet received");
            return;
        }

        ByteBuffer bufferByte = ByteBuffer.wrap(buffer);

        /* network byte order */
        bufferByte.order(ByteOrder.BIG_ENDIAN);

        short length = bufferByte.getShort(Constants.COMMON_PACKET_LENGTH_POS);

        if (length < Constants.COMMON_PACKET_LENGTH) {
            System.out.println("Invalid packet received, length < 3");
            System.out.println("Received: " + length);
            return;
        }

        if (buffer[Constants.COMMON_PACKET_TYPE_POS] == Constants.NACK_TYPE) {
            System.out.println("Got NACK!");
            return;
        }

        if (buffer[Constants.COMMON_PACKET_TYPE_POS] != Constants.REPLY_DHT22_TYPE) {
            System.out.printf("Invalid packet type received: %02X\n",
                buffer[Constants.COMMON_PACKET_TYPE_POS]);
            return;
        }

        if (length < Constants.REPLY_DHT22_PACKET_LENGTH_NO_SUCCESS) {
            System.out.println("Incomplete packet type received");
            return;
        }

        boolean success = 
          buffer[Constants.REPLY_DHT22_SUCCESS_POS] == Constants.REPLY_DHT22_SUCCESS;

        if (!success) {
            System.out.println("Firmware error! DHT22 transaction error!");
            return;
        }

        if (length != Constants.REPLY_DHT22_PACKET_LENGTH) {
            System.out.println("Incomplete packet type received");
            return;
        }

        bufferByte.position(Constants.REPLY_DHT22_TEMPERATURE_POS);

        // Parse temperature
        int rawTempNetwork = bufferByte.getInt();
        float temp = (float) (rawTempNetwork) / 100;

        // Parse humidity
        int rawHumNetwork = bufferByte.getInt();
        float hum = (float) (rawHumNetwork) / 100;

        System.out.printf("Read temperature: %.2f, humidity: %.2f\n", temp, hum);
    }

    // Hex dump utility
    private static void hexdump(byte[] buffer, int length) {
        for (int i = 0; i < length; i++) {
            System.out.printf("%02X ", buffer[i]);
            if ((i + 1) % 16 == 0) {
                System.out.println();
            }
        }
        System.out.println();
    }

    // Main loop
    public void run() throws IOException, InterruptedException {
        socket = new Socket(serverAddress, port);
        byte[] buffer = new byte[Constants.REPLY_DHT22_PACKET_LENGTH];
        while (true) {
            sendRequest();
            getResults(buffer);
            Thread.sleep(2000);
        }
    }

    // Main method to start the client
    public static void main(String[] args) throws IOException, InterruptedException {
        TcpClient client = new TcpClient("127.0.0.1", 3333);
        client.run();
    }
}

