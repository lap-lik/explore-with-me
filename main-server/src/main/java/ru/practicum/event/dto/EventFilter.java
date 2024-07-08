package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class EventFilter {
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
}
