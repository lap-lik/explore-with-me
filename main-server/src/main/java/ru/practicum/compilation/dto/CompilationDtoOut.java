package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.event.dto.EventShortDtoOut;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompilationDtoOut {

    private Long id;
    private List<EventShortDtoOut> events;
    private boolean pinned;
    private String title;
}
