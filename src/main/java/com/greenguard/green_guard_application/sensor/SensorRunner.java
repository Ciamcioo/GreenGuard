package com.greenguard.green_guard_application.sensor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

import com.greenguard.green_guard_application.repository.SensorRepository;
import com.greenguard.green_guard_application.sensor.SensorContext;

// TODO: Make this class thread safe
@Component
public class SensorRunner implements ApplicationRunner {

  private SensorRepository sensorRepository;

  // TODO: Add synchronization over it
  private Thread sensorManagerThread;

  @Autowired
  public SensorRunner(SensorRepository sensorRepository)
  {
    this.sensorRepository = sensorRepository;
  }

  // TODO: Add synchronization over it
  List<SensorContext> ctx;

  @Override
  public void run(ApplicationArguments args) {
       this.ctx = sensorRepository.findByActiveTrue().stream()
                .map(sensor -> new SensorContext(
                        sensor.getId(),
                        sensor.getIpAddress()
                ))
                .toList();

//    this.ctx = List.of(new SensorContext(123L, "192.168.105.4"));

    sensorManagerThread = new Thread(this::runSensorManager);
    sensorManagerThread.start();
  }

  private void runSensorManager()
  {
      while(true) {
        for (SensorContext sensor : ctx) {
            if(sensor.connect() == false)
              continue;

            if(sensor.hasTimedOut() == true) {
              sensor.requestData();
            }
            else
              continue;
        }
      }
  }

}
