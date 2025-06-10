package com.greenguard.green_guard_application.sensor;

import java.net.Socket;
import java.io.IOException;
import java.time.Instant;
import java.io.OutputStream;
import java.util.UUID;

import com.greenguard.green_guard_application.sensor.ATHPacketBuilder;
import com.greenguard.green_guard_application.service.ReadingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class SensorContext implements AutoCloseable{
    private static final Logger log = LoggerFactory.getLogger(SensorContext.class);
    private final UUID id;
    private final String ip;
    private final int port = 5555;
    private Socket socket;
    private long timeout = 5000; // 5000ms = 5s
    private boolean connected = false;
    private Instant lastTimeOut = Instant.now();
    private ATHPacketBuilder.ATHData recentReading;
    private Boolean isDataNew = false;


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
        if(this.connected)
            return true;

        if(!hasTimedOut())
            return false;

        log.info("Connecting to sensor ID={} at {}:{}", this.id, this.ip, this.port);

      try {
          this.socket = new Socket(this.ip, this.port);
          this.connected = true;
          log.info("Sensor IP: {} connected!", this.ip);
      } catch (IOException e) {
          log.error("Failed to connect to sensor IP: {}", this.ip);
          this.connected = false;
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
      if(!this.connected || this.socket == null || this.socket.isClosed())
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
        if(!ATHPacketBuilder.sendATHRequest(this.socket.getOutputStream())) {
            log.error("Failed to send request to sensor IP: {}. Closing connection.", this.ip);

            this.connected = false;

            try {
                this.socket.close();
            } catch (IOException ex) {
                log.error("Failed to close socket: {}", ex.getMessage());
            }

            return false;
        }
      } catch (IOException e) {
          log.error("IOException when sending request to sensor IP: {}: {}", this.ip, e.getMessage());

        this.connected = false;

        try {
            this.socket.close();
        } catch (IOException ex) {
            log.error("Failed to close socket: {}", ex.getMessage());
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
                log.error("Failed to close socket: {}", ex.getMessage());
            }

            return false;
        }

        if(!data.isValid())
          return false;

        this.recentReading = data;
        this.isDataNew = true;
        log.info("Sensor at {} = {}", this.ip, data);

      } catch (IOException e) {
          log.error("IOException when sending request to sensor IP: {}: {}", this.ip, e.getMessage());
          this.connected = false;

          try {
              this.socket.close();
          } catch (IOException ex) {
              log.error("Failed to close socket: {}", ex.getMessage());
          }

          return false;
      }

      return true;
    }

    private boolean getATHParams() {
        return this.sendATHRequest() && this.getATHResponse();
    }

    public ATHPacketBuilder.ATHData getRecentReading() {
        return recentReading;
    }

    public Boolean getDataNew() {
        return isDataNew;
    }

    public void setDataNew(Boolean dataNew) {
        isDataNew = dataNew;
    }
}
