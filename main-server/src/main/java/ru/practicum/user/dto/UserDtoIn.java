package ru.practicum.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UserDtoIn {

    private Long id;

    @NotBlank(message = "The name must not be empty.")
    @Size(min = 2, max = 250, message = "The name must be between 2 and 250 characters long.")
    private String name;

    @NotBlank(message = "The email must not be empty.")
    @Email(message = "The email is incorrect.")
    @Size(min = 6, max = 254, message = "The email must be between 6 and 254 characters long.")
    private String email;
}
