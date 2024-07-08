package ru.practicum.event.dto;

import lombok.Data;
import ru.practicum.category.dto.CategoryDtoOut;
import ru.practicum.event.model.EventState;
import ru.practicum.user.dto.UserShortDtoOut;

@Data
public class EventDtoOut {

    private Long id;

    private String annotation;

    private CategoryDtoOut category;

    private Long confirmedRequests;

    private String createdOn;

    private String description;

    private String eventDate;

    private UserShortDtoOut initiator;

    private LocationDto location;

    private boolean paid;

    private Integer participantLimit;

    private String publishedOn;

    private boolean requestModeration;

    private EventState state;

    private String title;

    private Long views;
}
