package com.example.backend.task;

import com.example.backend.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TaskService {

    Task save(Task task);
    Task edit(Task previousTask, Task editedTask);
    Task editStatus(Task task);
    void delete(Task task);

    Optional<Task> findById(int id);

    List<Task> findAllByUser(User user, int order);
    List<Task> findAllByUserAndIfCompletedOrderByDueTo(User user, Boolean ifCompleted, int order);
}
