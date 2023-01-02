package com.example.backend.task;

import com.example.backend.user.User;
import com.example.backend.user.UserNotFoundException;
import com.example.backend.user.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;

    public TaskServiceImpl(TaskRepository taskRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    @Override
    public Task save(Task task) {
        User currentLoggedInUser = userService.findCurrentLoggedInUser().orElseThrow(UserNotFoundException::new);
        task.setUser(currentLoggedInUser);
        task.setIfCompleted(false);
        currentLoggedInUser.addTask(task);

        return taskRepository.save(task);
    }

    @Override
    public Task edit(Task previousTask, Task editedTask) {
        previousTask.setTitle(editedTask.getTitle());
        previousTask.setContent(editedTask.getContent());
        previousTask.setDueTo(editedTask.getDueTo());

        return taskRepository.save(previousTask);
    }

    @Override
    public Task editStatus(Task task) {
        task.setIfCompleted(!task.getIfCompleted());

        return taskRepository.save(task);
    }

    @Override
    public void delete(Task task) {
        taskRepository.delete(task);
    }

    @Override
    public Optional<Task> findById(int id) {
        return taskRepository.findById(id);
    }

    @Override
    public List<Task> findAllByUser() {
        User currentLoggedInUser = userService.findCurrentLoggedInUser().orElseThrow(UserNotFoundException::new);

        return taskRepository.findAllByUser(currentLoggedInUser);
    }

    @Override
    public List<Task> findAllByUserAndIfCompleted(int ifCompleted) {
        User currentLoggedInUser = userService.findCurrentLoggedInUser().orElseThrow(UserNotFoundException::new);

        return taskRepository.findAllByUser(currentLoggedInUser).stream().filter(
                task -> task.getIfCompleted().equals(ifCompleted >= 1)).collect(Collectors.toList());
    }

    @Override
    public List<Task> findAllByUserAndIfCompletedAndDueTo(Boolean ifCompleted, LocalDateTime dueTo) {
        User currentLoggedInUser = userService.findCurrentLoggedInUser().orElseThrow(UserNotFoundException::new);

        return taskRepository.findAllByUserAndIfCompletedAndDueTo(currentLoggedInUser, ifCompleted, dueTo);
    }

    @Override
    public List<Task> findAllByUserAndDueTo(User user, LocalDateTime dueTo) {
        return taskRepository.findAllByUserAndDueTo(user, dueTo);
    }
}
