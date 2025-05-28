package com.greenguard.green_guard_application.repository;

import com.greenguard.green_guard_application.model.entity.Reading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReadingRepository extends JpaRepository<Reading, UUID>, JpaSpecificationExecutor<Reading> {
}
