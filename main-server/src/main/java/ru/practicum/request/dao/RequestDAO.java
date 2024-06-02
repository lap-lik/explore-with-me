package ru.practicum.request.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.request.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestDAO extends JpaRepository<Request, Long> {

    List<Request> findAllByRequester_IdOrderByCreatedDesc(long requesterId);

    @Modifying
    @Query("UPDATE Request r " +
            "SET r.status = :status " +
            "WHERE r.id = :requestId AND r.requester.id = :requesterId")
    Optional<Request> updateStatusAtCanceled(@Param("requestId") Long requestId,
                                             @Param("requesterId") Long requesterId,
                                             @Param("status") String status);

    @Query("SELECT r " +
            "FROM Request r " +
            "WHERE r.event.id IN :eventIds AND r.status = :status")
    List<Request> findAllByEvent_IdInAndStatus(@Param("eventIds") List<Long> eventIds,
                                               @Param("status") String status);
}

