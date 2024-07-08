package ru.practicum.category.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CategoryDtoIn {

    private Long id;

    @NotBlank(message = "The name must not be empty.")
    @Size(min = 1, max = 50, message = "The name must be between 1 and 50 characters long.")
    private String name;
}
