package com.example.backend.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(unique = true, nullable = false, length = 45)
    @Size(min = 5, max = 45, message = "{user.login.size}")
    @NotBlank(message = "{user.login.notBlank}")
    @Pattern(regexp = "^(?=.*[A-Za-z0-9]$)[A-Za-z][A-Za-z\\d.-]{4,45}$",
            message = "{user.login.regexp}")
    private String login;

    @Column(unique = true, nullable = false, length = 50)
    @NotBlank
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false, length = 1000)
    @NotBlank(message = "{user.password.notBlank}")
    private String password;

    @Transient
    private String roles = "ROLE_USER";

}
