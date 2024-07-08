package ru.practicum.request.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.RequestStatus;

import java.util.List;

public interface RequestDAO extends JpaRepository<Request, Long> {

    List<Request> findAllByRequester_IdOrderByCreatedDesc(long requesterId);

    List<Request> findAllByIdInAndStatus(List<Long> requestIds, RequestStatus status);

    List<Request> findAllByEvent_IdInAndStatus(List<Long> eventsIds, RequestStatus status);

    List<Request> findAllByEvent_IdAndStatus(long eventId, RequestStatus status);

    Integer countByEvent_IdAndStatus(long eventId, RequestStatus status);

    boolean existsByEvent_IdAndRequester_Id(long eventId, long userId);

    List<Request> findAllByEvent_Id(long eventId);
}

