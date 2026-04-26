package com.personalproject.tasklist.tasks;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.personalproject.tasklist.utils.NullValuesRequest;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/tasks")
@AllArgsConstructor
public class TaskController {

    private final TaskRepository taskRepository;

    @PostMapping("/")
    public ResponseEntity<String> create(@RequestBody Task task, HttpServletRequest request) {
        // Recuperando o ID do usuário 
        var userId = request.getAttribute("userId");
        task.setUserId((UUID) userId);
        
        // Validando a data de início e fim das tarefas
        LocalDateTime currentDate = LocalDateTime.now();
        if (currentDate.isAfter(task.getStartedAt()) || currentDate.isAfter(task.getEndedAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("A data de início da tarefa deve ser após a data atual");
        }
        
        // Verificando se a data do fim da tarefa é após a data de início
        if (task.getStartedAt().isAfter(task.getEndedAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("A data do fim da tarefa deve ser após a data de início");
        }

        this.taskRepository.save(task);
        return ResponseEntity.status(HttpStatus.CREATED).body("Tarefa criada");
    }

    @GetMapping("/")
    public ResponseEntity<List<Task>> findAll(HttpServletRequest request) {
        var userId = request.getAttribute("userId");
        var tasks = this.taskRepository.findByUserId((UUID) userId);
        return ResponseEntity.ok().body(tasks);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Integer id, @RequestBody Task task, HttpServletRequest request) {
        var newTask = this.taskRepository.findById(id).orElse(null);
        if (newTask.equals(null)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tarefa não encontrada");
        }

        var userId = request.getAttribute("userId");
        if (!newTask.getUserId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Vocẽ não tem permissão para alterar esta tarefa");
        }
        
        NullValuesRequest.copyNonNullProperties(task, newTask);
        var updatedTask = this.taskRepository.save(newTask);
        this.taskRepository.save(updatedTask);
        return ResponseEntity.status(HttpStatus.OK).body("Tarefa atualizada");
    }
}
