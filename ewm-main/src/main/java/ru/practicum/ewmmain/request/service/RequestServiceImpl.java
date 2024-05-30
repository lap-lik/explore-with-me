package ru.practicum.ewmmain.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmmain.event.dao.EventDAO;
import ru.practicum.ewmmain.exception.NotFoundException;
import ru.practicum.ewmmain.request.dao.RequestDAO;
import ru.practicum.ewmmain.request.dto.ParticipationDtoIn;
import ru.practicum.ewmmain.request.dto.ParticipationDtoOut;
import ru.practicum.ewmmain.request.mapper.RequestMapper;
import ru.practicum.ewmmain.request.model.RequestStatus;
import ru.practicum.ewmmain.user.dao.UserDAO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static ru.practicum.ewmmain.constant.Constant.DATE_TIME_PATTERN;

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

        checkExistsUserById(userId);
        checkExistsEventById(eventId);

        ParticipationDtoIn request = ParticipationDtoIn.builder()
                .created(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)))
                .requester(userId)
                .event(eventId)
                .status(RequestStatus.PENDING.toString())
                .build();

        return requestMapper.entityToDto(requestDAO.save(requestMapper.dtoToEntity(request)));
    }

    @Override
    public List<ParticipationDtoOut> getAll(long userId) {

        checkExistsUserById(userId);
        return requestMapper.entitiesToDtos(requestDAO.findAllByRequester_IdOrderByCreatedDesc(userId));
    }

    @Override
    @Transactional
    public ParticipationDtoOut update(long userId, long requestId) {

        checkExistsUserById(userId);
        checkExistsRequestById(requestId);

        return requestMapper.entityToDto(requestDAO.updateStatusAtCanceled(requestId, userId, RequestStatus.CANCELLED.toString())
                .orElseThrow(() -> NotFoundException.builder()
                        .message(String.format("The request with the ID - `%d` was not found.", requestId))
                        .build()));
    }


    private void checkExistsRequestById(long requestId) {

        boolean isExist = requestDAO.existsById(requestId);
        if (!isExist) {
            throw NotFoundException.builder()
                    .message(String.format("The request with the ID=`%d` was not found.", requestId))
                    .build();
        }
    }

    private void checkExistsUserById(long userId) {

        boolean isExist = userDAO.existsById(userId);
        if (!isExist) {
            throw NotFoundException.builder()
                    .message(String.format("The user with the ID=`%d` was not found.", userId))
                    .build();
        }
    }

    private void checkExistsEventById(long eventId) {

        boolean isExist = eventDAO.existsById(eventId);
        if (!isExist) {
            throw NotFoundException.builder()
                    .message(String.format("The event with the ID=`%d` was not found.", eventId))
                    .build();
        }
    }
}
