package ru.practicum.ewmmain.category.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CategoryInputDTO {

    private Long id;

    @NotBlank(message = "The name must not be empty.")
    private String name;
}
