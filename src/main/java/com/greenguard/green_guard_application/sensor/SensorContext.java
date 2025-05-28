package com.greenguard.green_guard_application.sensor;

import java.net.Socket;
import java.io.IOException;
import java.time.Instant;
import java.io.OutputStream;
import java.util.UUID;

import com.greenguard.green_guard_application.sensor.ATHPacketBuilder;

public class SensorContext implements AutoCloseable{
    private final UUID id;
    private final String ip;
    private final int port = 5555;
    private Socket socket;
    private long timeout = 5000; // 5000ms = 5s
    private boolean connected = false;
    private Instant lastTimeOut = Instant.now();

    public SensorContext(UUID id, String ip) {
        this.id = id;
        this.ip = ip;
    }

    @Override
    public void close() throws IOException {
        if(this.socket != null && !this.socket.isClosed()) {
          socket.close();
        }
    }

    public UUID getId() {
      return this.id;
    }

    public String getIpAddress() {
      return this.ip;
    }

    public boolean connect()
    {
      if(this.connected == true)
        return this.connected;

      if(hasTimedOut() == false)
        return false;

      System.out.println("Connecting to sensor ID=" +
                         this.id +
                         " at " + this.ip +
                         ":" +
                         this.port);

      try {
          this.socket = new Socket(this.ip, this.port);
          this.connected = true;
          System.out.println("Sensor IP: " + this.ip + " connected!");
      } catch (IOException e) {
          this.connected = false;
          System.err.println("Failed to connect to sensor IP: " + this.ip);
      }

      this.lastTimeOut = Instant.now();

      return this.connected;
    }

    public boolean hasTimedOut()
    {
      final Instant now = Instant.now();
      final long elapsed = java.time.Duration.between(lastTimeOut, now).toMillis();

      return elapsed >= this.timeout;
    }

    public boolean requestData()
    {
      /* Sanity check */
      if(this.connected == false || this.socket == null || this.socket.isClosed())
      {
          this.connected = false;
          return false;
      }

      this.lastTimeOut = Instant.now();
      return this.getATHParams();
    }

    private boolean sendATHRequest()
    {
      try {
        if(ATHPacketBuilder.sendATHRequest(this.socket.getOutputStream()) == false) {
            System.err.println("Failed to send request to sensor IP: " +
                this.ip +
                ". Closing connection.");

            this.connected = false;

            try {
                this.socket.close();
            } catch (IOException ex) {
                System.err.println("Failed to close socket: " + ex.getMessage());
            }

            return false;
        }
      } catch (IOException e) {
        System.err.println("IOException when sending request to sensor IP: " +
                           this.ip + ": " + e.getMessage());

        this.connected = false;

        try {
            this.socket.close();
        } catch (IOException ex) {
            System.err.println("Failed to close socket: " + ex.getMessage());
        }

        return false;
      }

      return true;
    }

    private boolean getATHResponse()
    {
      try {
        ATHPacketBuilder.ATHData data =
          ATHPacketBuilder.readATHReply(this.socket.getInputStream());

        if(data == null)
        {
            this.connected = false;

            try {
                this.socket.close();
            } catch (IOException ex) {
                System.err.println("Failed to close socket: " + ex.getMessage());
            }

            return false;
        }

        if(data.isValid() == false)
          return false;

        System.out.println("Sensor at " + this.ip + " = " + data);

      } catch (IOException e) {
        System.err.println("IOException when reading from sensor IP: " +
                             this.ip + ": " + e.getMessage());
          this.connected = false;

          try {
              this.socket.close();
          } catch (IOException ex) {
              System.err.println("Failed to close socket: " + ex.getMessage());
          }

          return false;
      }

      return true;
    }

    private boolean getATHParams()
    {
      if(this.sendATHRequest() == false)
        return false;

      if(this.getATHResponse() == false)
        return false;

      return true;
    }
}
