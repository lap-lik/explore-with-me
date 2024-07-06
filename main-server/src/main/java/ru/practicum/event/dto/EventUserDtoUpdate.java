package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.practicum.event.model.StateAction;

import javax.validation.constraints.PositiveOrZero;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventUserDtoUpdate {

    @Length(min = 20, max = 2000, message = "The minimum length of the annotation is 20, the maximum is 2000.")
    private String annotation;

    private Long categoryId;

    @Length(min = 20, max = 7000, message = "The minimum length of the description is 20, the maximum is 7000.")
    private String description;

    private String eventDate;

    private LocationDto location;

    private Boolean paid;

    @PositiveOrZero(message = "The participantLimit field cannot be less than 0.")
    private Integer participantLimit;

    private Boolean requestModeration;

    @Length(min = 3, max = 120, message = "The minimum length of the title is 3, the maximum is 120.")
    private String title;

    private StateAction stateAction;
}
