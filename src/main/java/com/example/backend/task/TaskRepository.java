package com.example.backend.task;

import com.example.backend.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    List<Task> findAllByUserAndIfCompletedAndDueTo(User user, Boolean ifCompleted, LocalDateTime dueTo);
    List<Task> findAllByUserAndDueTo(User user, LocalDateTime dueTo);
}
