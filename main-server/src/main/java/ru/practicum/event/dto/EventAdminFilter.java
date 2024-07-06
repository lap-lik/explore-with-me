package ru.practicum.event.dto;

import lombok.Builder;
import lombok.Getter;
import ru.practicum.event.model.EventState;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class EventAdminFilter extends EventFilter {

    private List<Long> users;
    private List<EventState> states;
    private List<Long> categories;

    @Builder
    public EventAdminFilter(LocalDateTime rangeStart, LocalDateTime rangeEnd, List<Long> users, List<EventState> states, List<Long> categories) {
        super(rangeStart, rangeEnd);
        this.users = users;
        this.states = states;
        this.categories = categories;
    }
}
