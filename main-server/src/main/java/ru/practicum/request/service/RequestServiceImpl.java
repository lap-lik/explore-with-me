package ru.practicum.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.dao.EventDAO;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.exception.DataConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.request.dao.RequestDAO;
import ru.practicum.request.dto.ParticipationDtoOut;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.user.dao.UserDAO;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {

    private final RequestDAO requestDAO;
    private final UserDAO userDAO;
    private final EventDAO eventDAO;
    private final RequestMapper requestMapper;

    @Override
    @Transactional
    public ParticipationDtoOut create(long userId, long eventId) {

        User user = getUser(userId);
        Event event = getEvent(eventId);

        if (!Objects.equals(event.getState(), EventState.PUBLISHED)) {
            throw DataConflictException.builder()
                    .message("Cannot apply for participation, the event has not been published.")
                    .build();
        }
        if (Objects.equals(event.getInitiator().getId(), userId)) {
            throw DataConflictException.builder()
                    .message("Cannot create a request for your own event.")
                    .build();
        }
        boolean checkExistsRequestByEventIdAndUserId = requestDAO.existsByEvent_IdAndRequester_Id(eventId, userId);
        if (checkExistsRequestByEventIdAndUserId) {
            throw DataConflictException.builder()
                    .message("Cannot add a repeat request to participate in the event.")
                    .build();
        }
        Integer participantLimit = event.getParticipantLimit();
        if (participantLimit > 0) {
            Integer countConfirmedRequests = requestDAO.countByEvent_IdAndStatus(eventId, RequestStatus.CONFIRMED);
            if (Objects.equals(participantLimit, countConfirmedRequests)) {
                throw DataConflictException.builder()
                        .message("The limit of the event participants has been reached.")
                        .build();
            }
        }

        Request newRequest = Request.builder()
                .created(LocalDateTime.now().withNano(0))
                .event(event)
                .requester(user)
                .status((event.isRequestModeration() && participantLimit > 0) ? RequestStatus.PENDING : RequestStatus.CONFIRMED)
                .build();

        return requestMapper.entityToDto(requestDAO.save(newRequest));
    }

    @Override
    public List<ParticipationDtoOut> getAll(long userId) {

        checkExistsUserById(userId);
        List<Request> requests = requestDAO.findAllByRequester_IdOrderByCreatedDesc(userId);

        if (requests.isEmpty()) {
            return Collections.emptyList();
        }

        return requestMapper.entitiesToDtos(requests);
    }

    @Override
    @Transactional
    public ParticipationDtoOut canceled(long userId, long requestId) {

        checkExistsUserById(userId);
        Request request = requestDAO.findById(requestId)
                .orElseThrow(() -> NotFoundException.builder()
                        .message(String.format("The request with the ID - `%d` was not found.", requestId))
                        .build());
        if (!Objects.equals(request.getRequester().getId(), userId)) {
            throw DataConflictException.builder()
                    .message(String.format("The request with ID - `%d` does not belong to the user with ID - `%d`", requestId, userId))
                    .build();
        }
        request.setStatus(RequestStatus.CANCELED);
        Request savedRequest = requestDAO.save(request);

        return requestMapper.entityToDto(savedRequest);
    }

    private void checkExistsUserById(long userId) {

        boolean isExist = userDAO.existsById(userId);
        if (!isExist) {
            throw NotFoundException.builder()
                    .message(String.format("The user with the ID=`%d` was not found.", userId))
                    .build();
        }
    }

    private Event getEvent(long eventId) {

        return eventDAO.findById(eventId).orElseThrow(() ->
                NotFoundException.builder()
                        .message(String.format("The event with the ID=`%d` was not found.", eventId))
                        .build());
    }

    private User getUser(long userId) {

        return userDAO.findById(userId).orElseThrow(() ->
                NotFoundException.builder()
                        .message(String.format("The user with the ID=`%d` was not found.", userId))
                        .build());
    }
}
