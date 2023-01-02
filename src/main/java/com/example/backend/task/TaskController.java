package com.example.backend.task;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Transactional
    @PostMapping("/task/add")
    public ResponseEntity<?> addTask(@Valid @RequestBody Task task) {

        return ResponseEntity.ok(taskService.save(task));
    }

    @GetMapping("/task/get")
    public ResponseEntity<?> get() {

        return ResponseEntity.ok(taskService.findAllByUser());
    }

    @GetMapping("/task/get/{flag}")
    public ResponseEntity<?> getAllByUserAndIfCompleted(@PathVariable String flag) {
        int flagValue = Integer.parseInt(flag);
        if (flagValue != 0 && flagValue != 1)
            return new ResponseEntity<>("Bad input!", HttpStatus.BAD_REQUEST);

        return ResponseEntity.ok(taskService.findAllByUserAndIfCompleted(flagValue));
    }

    @PutMapping("/task/{id}")
    public ResponseEntity<?> editOne(@PathVariable int id, @RequestBody Task task) {

        return ResponseEntity.ok(
                taskService.edit(taskService.findById(id).orElseThrow(TaskNotFoundException::new), task));
    }

    @PutMapping("/task/status/{id}")
    public ResponseEntity<?> editStatus(@PathVariable int id) {

        return ResponseEntity.ok(
                taskService.editStatus(taskService.findById(id).orElseThrow(TaskNotFoundException::new)));
    }

    @DeleteMapping("/task/delete/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable int id) {
        Task task = taskService.findById(id).orElseThrow(TaskNotFoundException::new);
        taskService.delete(task);

        return ResponseEntity.ok("Task successfully deleted!");
    }
}
