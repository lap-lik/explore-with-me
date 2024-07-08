package ru.practicum.event.dto;

import lombok.Data;
import ru.practicum.category.dto.CategoryDtoOut;
import ru.practicum.user.dto.UserShortDtoOut;

@Data
public class EventShortDtoOut {

    private Long id;

    private String annotation;

    private CategoryDtoOut category;

    private Long confirmedRequests;

    private String eventDate;

    private UserShortDtoOut initiator;

    private boolean paid;

    private String title;

    private Long views;
}
