package com.greenguard.green_guard_application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.greenguard.green_guard_application.model.entity.User;

public interface UserRepository extends JpaRepository<User, String> {
}
