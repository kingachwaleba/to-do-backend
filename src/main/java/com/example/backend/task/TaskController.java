package com.example.backend.task;

import com.example.backend.user.User;
import com.example.backend.user.UserNotFoundException;
import com.example.backend.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @Transactional
    @PostMapping("/task/add")
    public ResponseEntity<?> addTask(@Valid @RequestBody Task task) {

        return ResponseEntity.ok(taskService.save(task));
    }

    @GetMapping("/task/get/{order}")
    public ResponseEntity<?> get(@PathVariable String order) {
        int orderValue = Integer.parseInt(order);
        if (orderValue != 0 && orderValue != 1)
            return new ResponseEntity<>("Bad input!", HttpStatus.BAD_REQUEST);

        return ResponseEntity.ok(taskService.findAllByUser(
                userService.findCurrentLoggedInUser().orElseThrow(UserNotFoundException::new), orderValue));
    }

//    @GetMapping("/task/get/{flag}")
//    public ResponseEntity<?> getAllByUserAndIfCompleted(@PathVariable String flag) {
//        int flagValue = Integer.parseInt(flag);
//        if (flagValue != 0 && flagValue != 1)
//            return new ResponseEntity<>("Bad input!", HttpStatus.BAD_REQUEST);
//
//        return ResponseEntity.ok(taskService.findAllByUserAndIfCompleted(flagValue));
//    }

    @GetMapping("/task/get/{ifCompleted}/{order}")
    public ResponseEntity<?> findAllByUserAndIfCompletedAndDueToOrderByDueTo(@PathVariable String ifCompleted, @PathVariable String order) {
        int ifCompletedValue = Integer.parseInt(ifCompleted);
        if (ifCompletedValue != 0 && ifCompletedValue != 1)
            return new ResponseEntity<>("Bad input!", HttpStatus.BAD_REQUEST);

        int orderValue = Integer.parseInt(order);
        if (orderValue != 0 && orderValue != 1)
            return new ResponseEntity<>("Bad input!", HttpStatus.BAD_REQUEST);

        User currentLoggedInUser = userService.findCurrentLoggedInUser().orElseThrow(UserNotFoundException::new);

        return ResponseEntity.ok(taskService.findAllByUserAndIfCompletedOrderByDueTo(
                currentLoggedInUser, ifCompletedValue == 1, orderValue));
    }

    @GetMapping("/task/get/flag/{date}")
    public ResponseEntity<?> getAllByUserOrderByDueTo(@PathVariable String date) {
        int flagValue = Integer.parseInt(date);
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
