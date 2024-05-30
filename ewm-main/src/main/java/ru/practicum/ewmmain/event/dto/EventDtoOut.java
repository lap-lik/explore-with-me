package ru.practicum.ewmmain.event.dto;

import lombok.Data;
import ru.practicum.ewmmain.category.dto.CategoryDtoOut;
import ru.practicum.ewmmain.event.model.EventState;
import ru.practicum.ewmmain.user.dto.UserShortDtoOut;

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
