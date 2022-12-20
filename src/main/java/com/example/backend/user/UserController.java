package com.example.backend.user;

import com.example.backend.config.ErrorMessage;
import com.example.backend.config.JwtProvider;
import com.example.backend.config.JwtResponse;
import com.example.backend.helpers.LoginForm;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.Optional;

@RestController
public class UserController {

    private final UserService userService;
    private final ErrorMessage errorMessage;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    public UserController(UserService userService, ErrorMessage errorMessage, AuthenticationManager authenticationManager,
                          JwtProvider jwtProvider) {
        this.userService = userService;
        this.errorMessage = errorMessage;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<?> createAccount(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (userService.validation(bindingResult, user.getPassword()).size() != 0)
            return new ResponseEntity<>(errorMessage.get("data.error"), HttpStatus.BAD_REQUEST);

        if (userService.existsByEmail(user.getEmail()) || userService.existsByLogin(user.getLogin()))
            return new ResponseEntity<>(errorMessage.get("register.takenCredentials"), HttpStatus.CONFLICT);

        return ResponseEntity.ok(userService.save(user));
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginRequest, BindingResult bindingResult) {
        if (userService.getErrorList(bindingResult).size() != 0)
            return new ResponseEntity<>(errorMessage.get("data.error"), HttpStatus.BAD_REQUEST);

        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        Optional<User> optionalUser = userService.findByEmail(email);

        if (optionalUser.isEmpty())
            return new ResponseEntity<>(errorMessage.get("login.error"), HttpStatus.UNAUTHORIZED);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwtToken = jwtProvider.generateJwtToken(authentication);
        Date expiryDate = jwtProvider.getExpiryDateFromJwtToken(jwtToken);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(jwtToken, expiryDate, userDetails.getUsername()));
    }
}
