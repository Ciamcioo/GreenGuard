package com.greenguard.green_guard_application.repository;

import com.greenguard.green_guard_application.model.entity.Sensor;
import com.greenguard.green_guard_application.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, UUID> {

    List<Sensor> findSensorByUser(User user);

    Optional<Sensor> findSensorByName(String name);

    @Query("SELECT s FROM Sensor s WHERE s.user.username = :username AND s.name = :name")
    Optional<Sensor> findSensorByUsernameAndName(@Param("username") String username, @Param("name") String name);

    Optional<Sensor> findSensorByIpAddress(String ipAddress);

    void deleteSensorByName(String name);

    List<Sensor> findByActiveTrue();

    @Modifying
    @Transactional
    @Query("UPDATE Sensor s SET s.name = :newName WHERE s.name = :name")
    void updateSensorName(@Param("name") String name, @Param("newName") String newName);
}
