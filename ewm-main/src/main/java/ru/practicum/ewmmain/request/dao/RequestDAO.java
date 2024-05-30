package ru.practicum.ewmmain.request.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewmmain.request.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestDAO extends JpaRepository<Request, Long> {

    List<Request> findAllByRequester_IdOrderByCreatedDesc(long requesterId);

//    @Query("INSERT Requests " +
//            "SET status = :status " +
//            "WHERE id = :requestId and requester_id = :requesterId")
//    Request updateStatusAtCanceled(@Param("requestId") long requestId,
//                                   @Param("requesterId")  long requesterId,
//                                   @Param("status") String status);

    @Modifying
    @Query("UPDATE Request r SET r.status = :newState WHERE r.id = :requestId AND r.requester.id = :requesterId")
    Optional<Request> updateStatusAtCanceled(Long requestId, Long requesterId, String newState);
}

