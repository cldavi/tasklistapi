package com.personalproject.tasklist.tasks;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/tasks")
@AllArgsConstructor
public class TaskController {
    
    private final TaskRepository taskRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody Task task, HttpServletRequest request) {
        var newTask = this.taskRepository.save(task);
        var userId = request.getAttribute("userId");
        newTask.setUserId((UUID) userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTask);
    }
}
