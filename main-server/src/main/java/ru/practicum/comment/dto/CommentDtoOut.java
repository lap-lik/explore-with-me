package ru.practicum.comment.dto;

import lombok.Data;
import ru.practicum.comment.model.CommentState;
import ru.practicum.user.dto.UserShortDtoOut;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class CommentDtoOut {

    private Long id;

    private String text;

    private UserShortDtoOut author;

    private Long eventId;

    private Set<CommentDtoOut> comments;

    private CommentState state;

    private LocalDateTime createdOn;
}
