package ru.practicum.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comment.dao.CommentDAO;
import ru.practicum.comment.dto.CommentAdminDtoUpdate;
import ru.practicum.comment.dto.CommentDtoIn;
import ru.practicum.comment.dto.CommentDtoOut;
import ru.practicum.comment.dto.CommentPrivateDtoUpdate;
import ru.practicum.comment.mapper.CommentMapper;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.model.CommentState;
import ru.practicum.event.dao.EventDAO;
import ru.practicum.event.model.Event;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.dao.UserDAO;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {
    private final CommentDAO commentDAO;
    private final EventDAO eventDAO;
    private final UserDAO userDAO;
    private final CommentMapper commentMapper;

    @Override
    @Transactional
    public CommentDtoOut updateByAdmin(Long commentId, CommentAdminDtoUpdate inputDTO) {

        Comment comment = getCommentById(commentId);
        comment.setState(inputDTO.getState());
        Comment updatedComment = commentDAO.save(comment);

        return commentMapper.entityToDto(updatedComment);
    }

    @Override
    @Transactional
    public CommentDtoOut createByPrivate(Long userId, Long eventId, CommentDtoIn inputDTO) {
        User user = getUserById(userId);
        Event event = getEventById(eventId);

        Comment newComment = Comment.builder()
                .text(inputDTO.getText())
                .author(user)
                .event(event)
                .state(CommentState.PUBLISHED)
                .createdOn(LocalDateTime.now().withNano(0))
                .build();

        Comment savedComment = commentDAO.save(newComment);

        return commentMapper.entityToDto(savedComment);
    }

    @Override
    @Transactional
    public CommentDtoOut updateByPrivate(Long userId, Long commentId, CommentPrivateDtoUpdate inputDTO) {
        getUserById(userId);
        Comment comment = getCommentById(commentId);

        String text = inputDTO.getText();
        if (Objects.nonNull(text)) {
            comment.setText(text);
        }
        comment.setState(CommentState.MODIFIED);
        Comment updatedComment = commentDAO.save(comment);

        return commentMapper.entityToDto(updatedComment);
    }

    @Override
    @Transactional
    public void deleteByPrivate(Long userId, Long commentId) {

        getUserById(userId);
        Comment comment = getCommentById(commentId);
        if (!Objects.equals(comment.getAuthor().getId(), userId)) {
            throw NotFoundException.builder()
                    .message(String.format("The user with ID=`%d is not the author of the comment with ID=`%d.", userId, commentId))
                    .build();
        }

        commentDAO.delete(comment);
    }

    private Comment getCommentById(Long commentId) {

        return commentDAO.findById(commentId).orElseThrow(() ->
                NotFoundException.builder()
                        .message(String.format("The comment with the ID=`%d` was not found.", commentId))
                        .build());
    }

    private Event getEventById(long eventId) {

        return eventDAO.findById(eventId).orElseThrow(() ->
                NotFoundException.builder()
                        .message(String.format("The event with the ID=`%d` was not found.", eventId))
                        .build());
    }

    private User getUserById(long userId) {

        return userDAO.findById(userId).orElseThrow(() ->
                NotFoundException.builder()
                        .message(String.format("The user with the ID=`%d` was not found.", userId))
                        .build());
    }
}
