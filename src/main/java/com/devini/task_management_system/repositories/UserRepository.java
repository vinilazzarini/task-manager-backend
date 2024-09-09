package com.devini.task_management_system.repositories;

import com.devini.task_management_system.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {
    Optional<User> findByUsername(String name);
}
