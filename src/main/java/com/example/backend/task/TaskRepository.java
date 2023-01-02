package com.example.backend.task;

import com.example.backend.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    List<Task> findAllByUserOrderByDueToAsc(User user);
    List<Task> findAllByUserOrderByDueToDesc(User user);
    List<Task> findAllByUserAndIfCompletedOrderByDueToAsc(User user, Boolean ifCompleted);
    List<Task> findAllByUserAndIfCompletedOrderByDueToDesc(User user, Boolean ifCompleted);
    List<Task> findAllByUserAndDueTo(User user, LocalDateTime dueTo);
}
