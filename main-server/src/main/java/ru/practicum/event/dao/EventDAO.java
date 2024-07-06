package ru.practicum.event.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.event.model.Event;

import java.util.Optional;

public interface EventDAO extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {

    Optional<Event> findByIdAndInitiator_Id(long eventId, long userId);

    boolean existsByIdAndInitiator_Id(long eventId, long userId);
}
