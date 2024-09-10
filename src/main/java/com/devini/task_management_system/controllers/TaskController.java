package com.devini.task_management_system.controllers;

import com.devini.task_management_system.domain.Task;
import com.devini.task_management_system.domain.User;
import com.devini.task_management_system.dto.TaskDTO;
import com.devini.task_management_system.exceptions.NotFoundException;
import com.devini.task_management_system.exceptions.WrongLoginException;
import com.devini.task_management_system.repositories.TaskRepository;
import com.devini.task_management_system.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/task")
@RequiredArgsConstructor
public class TaskController {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    @GetMapping("/user")
    public ResponseEntity<List<Task>> createTask(Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        List<Task> tasks = taskRepository.findByUsername(userDetails.getUsername());
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<Task> createTask(@PathVariable String taskId,Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Task task = taskRepository.findById(userDetails.getUsername()).orElseThrow(() -> new NotFoundException("Task not found!"));
        return ResponseEntity.ok(task);
    }

    @PostMapping("/create")
    public ResponseEntity<Task> createTask(@RequestBody TaskDTO taskDTO,Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Task task = new Task();
        task.setTitle(taskDTO.title());
        task.setDescription(taskDTO.description());
        task.setIsCompleted(false);
        task.setUsername(userDetails.getUsername());

        Task createdTask = taskRepository.save(task);
        return ResponseEntity.ok(createdTask);
    }

    @PutMapping("/update/{taskId}")
    public ResponseEntity<Task> updateTask(@PathVariable String taskId, @RequestBody TaskDTO taskDTO, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new NotFoundException("Task not found!"));

        if (!userDetails.getUsername().equals(task.getUsername())){
            throw new WrongLoginException("User does not have a task with this id!");
        }

        task.setTitle(taskDTO.title());
        task.setDescription(taskDTO.description());

        Task updatedTask = taskRepository.save(task);
        return ResponseEntity.ok(updatedTask);
    }

    @PatchMapping("/update/complete/{taskId}")
    public ResponseEntity<Task> updateIsCompletedTask(@PathVariable String taskId, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new NotFoundException("Task not found!"));

        if (!userDetails.getUsername().equals(task.getUsername())){
            throw new WrongLoginException("User does not have a task with this id!");
        }

        task.setIsCompleted(!task.getIsCompleted());

        Task updatedTask = taskRepository.save(task);
        return ResponseEntity.ok(updatedTask);
    }
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Task> deleteTask(@PathVariable String taskId, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new NotFoundException("Task not found!"));

        if (!userDetails.getUsername().equals(task.getUsername())){
            throw new WrongLoginException("User does not have a task with this id!");
        }

        taskRepository.delete(task);

        return ResponseEntity.ok().build();
    }

}
