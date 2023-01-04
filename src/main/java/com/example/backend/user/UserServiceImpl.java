package com.example.backend.user;

import com.example.backend.config.ErrorMessage;
import com.example.backend.task.Task;
import com.example.backend.task.TaskService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ErrorMessage errorMessage;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TaskService taskService;

    public UserServiceImpl(UserRepository userRepository, ErrorMessage errorMessage,
                           BCryptPasswordEncoder bCryptPasswordEncoder, @Lazy TaskService taskService) {
        this.userRepository = userRepository;
        this.errorMessage = errorMessage;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.taskService = taskService;
    }

    @Override
    public User save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public void changeUserPassword(User user, String password) {
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findCurrentLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserLogin = authentication.getName();

        return userRepository.findByLogin(currentUserLogin);
    }

    @Override
    public List<String> getErrorList(BindingResult bindingResult) {
        List<String> messages = new ArrayList<>();

        if (bindingResult.hasErrors())
            bindingResult.getFieldErrors().forEach(fieldError -> messages.add(fieldError.getDefaultMessage()));

        return messages;
    }

    @Override
    public List<String> passwordValidation(String password) {
        List<String> messages = new ArrayList<>();

        if (!password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$"))
            messages.add(errorMessage.get("user.password.regexp"));

        if (password.length() < 5 || password.length() > 50)
            messages.add(errorMessage.get("user.password.size"));

        return messages;
    }

    @Override
    public List<String> validation(BindingResult bindingResult, String password) {
        List<String> messages = getErrorList(bindingResult);
        messages.addAll(passwordValidation(password));

        return messages;
    }

    @Override
    public Boolean existsByLogin(String login) {
        return userRepository.existsByLogin(login);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Boolean checkIfValidOldPassword(User user, String oldPassword) {
        return bCryptPasswordEncoder.matches(oldPassword, user.getPassword());
    }

    @Override
    public void delete(User user) {
        List<Task> taskList = taskService.findAllByUser(user, 0);
        for (Task task : taskList) {
            taskService.delete(task);
        }

        userRepository.delete(user);
    }

}
