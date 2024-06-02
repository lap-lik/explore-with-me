package ru.practicum.category.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CategoryDtoIn {

    private Long id;

    @NotBlank(message = "The name must not be empty.")
    private String name;
}
