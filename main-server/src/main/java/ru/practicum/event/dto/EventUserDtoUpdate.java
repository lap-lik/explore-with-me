package ru.practicum.event.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.practicum.event.model.StateAction;

@Data
public class EventUserDtoUpdate {

    @Length(min = 20, max = 2000, message = "The minimum length of the annotation is 20, the maximum is 2000.")
    private String annotation;

    private Long categoryId;

    @Length(min = 20, max = 7000, message = "The minimum length of the description is 20, the maximum is 7000.")
    private String description;

    private String eventDate;

    private LocationDto location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    @Length(min = 3, max = 120, message = "The minimum length of the title is 3, the maximum is 120.")
    private String title;

    private StateAction stateAction;
}
