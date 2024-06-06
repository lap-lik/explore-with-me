package ru.practicum.event.dao;

import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface EventDAO extends JpaRepository<Event, Long> {


    @Query(value = "SELECT e.* " +
            "FROM events AS e " +
            "WHERE e.initiator_id = :userId " +
            "LIMIT :size OFFSET :from", nativeQuery = true)
    List<Event> findAllByUserId(@Param("userId") Long userId, @Param("from") int from, @Param("size") int size);

    Optional<Event> findByIdAndInitiator_Id(long eventId, long userId);

//    @Query(value = "SELECT * " +
//            "FROM events " +
//            "WHERE " +
//            "    (:text IS NULL OR LOWER(annotation) LIKE LOWER(CONCAT('%', :text, '%')) OR LOWER(description) LIKE LOWER(CONCAT('%', :text, '%'))) " +
//            "    AND (:categories IS NULL OR category_id IN (:categories)) " +
//            "    AND (:paid IS NULL OR paid = :paid) " +
//            "    AND (:rangeStart IS NULL OR event_date > to_timestamp(:rangeStart, 'YYYY-MM-DD HH24:MI:SS.MS')) " +
//            "    AND (:rangeEnd IS NULL OR event_date < to_timestamp(:rangeEnd, 'YYYY-MM-DD HH24:MI:SS.MS')) " +
//            "    AND (:state IS NULL OR state = :state) " +
//            "LIMIT :size OFFSET :from", nativeQuery = true)
//    List<Event> search(@Param("text") String text,
//                       @Param("categories") List<Long> categories,
//                       @Param("paid") Boolean paid,
//                       @Param("rangeStart") LocalDateTime rangeStart,
//                       @Param("rangeEnd") LocalDateTime rangeEnd,
//                       @Param("state") String state,
//                       @Param("from") int from,
//                       @Param("size") int size);

    @Query(value = "SELECT * " +
            "FROM events " +
            "WHERE " +
            "    (:text IS NULL OR LOWER(annotation) LIKE LOWER(CONCAT('%', :text, '%')) OR LOWER(description) LIKE LOWER(CONCAT('%', :text, '%'))) " +
            "    AND (:categories IS NULL OR category_id IN (:categories)) " +
            "    AND (:paid IS NULL OR paid = :paid) " +
            "    AND (event_date > to_timestamp(:rangeStart, 'YYYY-MM-DD HH24:MI:SS.MS')) " +
            "    AND (event_date < to_timestamp(:rangeEnd, 'YYYY-MM-DD HH24:MI:SS.MS')) " +
            "    AND (:state IS NULL OR state = :state) " +
            "LIMIT :size OFFSET :from", nativeQuery = true)
    List<Event> search(@Param("text") String text,
                       @Param("categories") List<Long> categories,
                       @Param("paid") Boolean paid,
                       @Param("rangeStart") LocalDateTime rangeStart,
                       @Param("rangeEnd") LocalDateTime rangeEnd,
                       @Param("state") String state,
                       @Param("from") int from,
                       @Param("size") int size);


    @Query(value = "SELECT * " +
            "FROM events " +
            "WHERE " +
            "    (:text IS NULL OR LOWER(annotation) LIKE LOWER(CONCAT('%', :text, '%')) OR LOWER(description) LIKE LOWER(CONCAT('%', :text, '%'))) " +
            "    AND (:categories IS NULL OR category_id IN (:categories)) " +
            "    AND (:paid IS NULL OR paid = :paid) " +
            "    AND (:state IS NULL OR state = :state) " +
            "LIMIT :size OFFSET :from", nativeQuery = true)
    List<Event> search(@Param("text") String text,
                       @Param("categories") List<Long> categories,
                       @Param("paid") Boolean paid,
                       @Param("state") String state,
                       @Param("from") int from,
                       @Param("size") int size);
}
