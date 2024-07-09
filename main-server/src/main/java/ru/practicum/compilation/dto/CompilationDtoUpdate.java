package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompilationDtoUpdate {

    private Set<Long> events;
    private Boolean pinned;

    @Size(min = 1, max = 50, message = "The title must be between 1 and 50 characters long.")
    private String title;
}
