package com.devini.task_management_system.repositories;

import com.devini.task_management_system.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task,String> {
    List<Task> findByUsername(String username);
}
