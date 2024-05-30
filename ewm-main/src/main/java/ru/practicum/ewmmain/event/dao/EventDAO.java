package ru.practicum.ewmmain.event.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmmain.event.model.Event;

public interface EventDAO extends JpaRepository<Event, Long> {
}
