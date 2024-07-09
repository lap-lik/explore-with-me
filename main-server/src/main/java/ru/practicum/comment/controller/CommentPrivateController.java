package ru.practicum.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentDtoIn;
import ru.practicum.comment.dto.CommentDtoOut;
import ru.practicum.comment.dto.CommentPrivateDtoUpdate;
import ru.practicum.comment.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/comments")
public class CommentPrivateController {

    private final CommentService service;

    @PostMapping("/{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDtoOut createComment(@PathVariable @Positive final long userId,
                                       @PathVariable @Positive final long eventId,
                                       @Valid @RequestBody CommentDtoIn inputDTO) {
        log.info("START endpoint `method:POST /users/{userId}/comments/{eventId}` (create comment), comments text: {}.", inputDTO.getText());

        return service.createByPrivate(userId, eventId, inputDTO);
    }

    @PatchMapping("/{commentId}")
    public CommentDtoOut updateComment(@PathVariable @Positive final long userId,
                                       @PathVariable @Positive final long commentId,
                                       @Valid @RequestBody CommentPrivateDtoUpdate inputDTO) {

        log.info("START endpoint `method:PATCH /users/{userId}/comments/{commentId}` (update comment by user), comment id: {}.", commentId);

        return service.updateByPrivate(userId, commentId, inputDTO);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable @Positive final long userId,
                              @PathVariable @Positive final long commentId) {

        log.info("START endpoint `method:DELETE /users/{userId}/comments/{commentId}` (delete comment by user), comment id: {}.", commentId);

        service.deleteByPrivate(userId, commentId);
    }
}
