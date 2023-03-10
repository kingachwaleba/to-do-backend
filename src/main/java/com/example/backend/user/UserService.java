package com.example.backend.user;

import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User save(User user);
    void changeUserPassword(User user, String password);
    Optional<User> findByEmail(String email);
    Optional<User> findCurrentLoggedInUser();
    List<String> getErrorList(BindingResult bindingResult);
    List<String> passwordValidation(String password);
    List<String> validation(BindingResult bindingResult, String password);
    Boolean existsByLogin(String login);
    Boolean existsByEmail(String email);
    Boolean checkIfValidOldPassword(User user, String oldPassword);
    void delete(User user);
}
