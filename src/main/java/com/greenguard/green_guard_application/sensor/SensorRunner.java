package com.greenguard.green_guard_application.sensor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import com.greenguard.green_guard_application.repository.SensorRepository;
import com.greenguard.green_guard_application.sensor.SensorContext;
import com.greenguard.green_guard_application.model.entity.Sensor;

// TODO: Make this class thread safe
@Component
public class SensorRunner implements ApplicationRunner {

  private SensorRepository sensorRepository;

  // TODO: Add synchronization over it
  private Thread sensorManagerThread;

  private List<SensorContext> ctx;

  @Autowired
  public SensorRunner(SensorRepository sensorRepository)
  {
    this.sensorRepository = sensorRepository;
    this.ctx = new ArrayList<>();
  }

  @Override
  public void run(ApplicationArguments args) {
      synchronized(this.ctx) {
          this.ctx = sensorRepository.findByActiveTrue().stream()
                   .map(sensor -> new SensorContext(
                           sensor.getId(),
                           sensor.getIpAddress()
                   ))
                   .collect(Collectors.toCollection(ArrayList::new));

//    this.ctx = List.of(new SensorContext(123L, "192.168.105.4"));
      }

    sensorManagerThread = new Thread(this::runSensorManager);
    sensorManagerThread.start();
  }

  private void runSensorManager()
  {
      while(true) {
          synchronized(this.ctx) {
              for (SensorContext sensor : ctx) {
                  if(sensor.connect() == false)
                    continue;

                  if(sensor.hasTimedOut() == true) {
                    sensor.requestData();
                  }
              }
            }
      }
  }

  public void addActiveSensor(Sensor sensor) {
      SensorContext newSensor = new SensorContext(sensor.getId(), sensor.getIpAddress());

      synchronized(this.ctx) {
          ctx.add(newSensor);
      }
  }

  public void deleteActiveSensor(Sensor sensor) {
      synchronized (this.ctx) {
          ctx.removeIf(sensorContext -> sensorContext.getId().equals(sensor.getId()));
      }
  }

}
