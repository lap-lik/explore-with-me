package ru.practicum.event.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.event.model.Event;

import java.util.List;

public interface EventDAO extends JpaRepository<Event, Long> {



    @Query(value = "SELECT e.* " +
            "FROM events AS e " +
            "WHERE e.initiator_id = :userId " +
            "LIMIT :size OFFSET :from", nativeQuery = true)
    List<Event> findAllByUserId(@Param("userId") Long userId, @Param("from") int from, @Param("size") int size);
}
