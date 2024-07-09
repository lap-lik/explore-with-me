package ru.practicum.comment.service;

import ru.practicum.comment.dto.CommentAdminDtoUpdate;
import ru.practicum.comment.dto.CommentDtoIn;
import ru.practicum.comment.dto.CommentDtoOut;
import ru.practicum.comment.dto.CommentPrivateDtoUpdate;

public interface CommentService {

    CommentDtoOut updateByAdmin(Long commentId, CommentAdminDtoUpdate inputDTO);

    CommentDtoOut createByPrivate(Long userId, Long eventId, CommentDtoIn inputDTO);

    CommentDtoOut updateByPrivate(Long userId, Long commentId, CommentPrivateDtoUpdate inputDTO);

    void deleteByPrivate(Long userId, Long commentId);
}
