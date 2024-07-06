package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompilationInputDTO {

    private Set<Long> events;
    private boolean pinned;

    @NotBlank(message = "The title must not be empty.")
    @Size(min = 1, max = 50, message = "The title must be between 1 and 50 characters long.")
    private String title;
}
