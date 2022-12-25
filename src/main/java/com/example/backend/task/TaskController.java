package com.example.backend.task;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Transactional
    @PostMapping("/task/add")
    public ResponseEntity<?> addTask(@Valid @RequestBody Task task) {
        if (task.getDueTo().isBefore(LocalDateTime.now()))
            return new ResponseEntity<>("Incorrect date chosen!", HttpStatus.BAD_REQUEST);

        return ResponseEntity.ok(taskService.save(task));
    }

    @DeleteMapping("/task/delete/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable int id) {
        Task task = taskService.findById(id).orElseThrow(TaskNotFoundException::new);
        taskService.delete(task);

        return ResponseEntity.ok("Task successfully deleted!");
    }
}
