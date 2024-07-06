package ru.practicum.event.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;


@Getter
public class EventPublicFilter extends EventFilter {

    private String text;
    private List<Long> categories;
    private Boolean paid;

    @Builder
    public EventPublicFilter(LocalDateTime rangeStart, LocalDateTime rangeEnd, String text, List<Long> categories, Boolean paid) {
        super(rangeStart, rangeEnd);
        this.text = text;
        this.categories = categories;
        this.paid = paid;
    }
}
