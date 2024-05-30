package ru.practicum.ewmmain.event.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class EventDtoIn {

    @NotBlank(message = "The name must not be empty.")
    @Length(min = 20, max = 2000, message = "The minimum length of the annotation is 20, the maximum is 2000.")
    private String annotation;

    @NotNull(message = "The category field cannot be null.")
    private Long category;

    @NotBlank(message = "The name must not be empty.")
    @Length(min = 20, max = 7000, message = "The minimum length of the description is 20, the maximum is 7000.")
    private String description;

    @NotBlank(message = "The name must not be empty.")
    private String eventDate;

    @NotNull(message = "The location field cannot be null.")
    private LocationDto location;

    private boolean paid;

    private int participantLimit;

    private boolean requestModeration;

    @NotBlank(message = "The title must not be empty.")
    @Length(min = 3, max = 120, message = "The minimum length of the title is 3, the maximum is 120.")
    private String title;
}
