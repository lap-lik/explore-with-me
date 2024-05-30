package ru.practicum.ewmmain.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import static ru.practicum.ewmmain.constant.Constant.EMAIL_REGEX;

@Data
public class UserDtoIn {

    private Long id;

    @NotBlank(message = "The name must not be empty.")
    private String name;

    @NotBlank(message = "The email must not be empty.")
    @Email(regexp = EMAIL_REGEX, message = "The email is incorrect.")
    private String email;
}
