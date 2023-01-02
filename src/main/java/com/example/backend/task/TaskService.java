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

    List<Task> findAllByUser();
    List<Task> findAllByUserAndIfCompleted(int ifCompleted);
    List<Task> findAllByUserAndIfCompletedAndDueTo(Boolean ifCompleted, LocalDateTime dueTo);
    List<Task> findAllByUserAndDueTo(User user, LocalDateTime dueTo);
}
