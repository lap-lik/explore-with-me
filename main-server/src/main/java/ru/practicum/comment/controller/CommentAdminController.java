package ru.practicum.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentAdminDtoUpdate;
import ru.practicum.comment.dto.CommentDtoOut;
import ru.practicum.comment.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/comments")
public class CommentAdminController {

    private final CommentService service;

    @PatchMapping("/{commentId}")
    public CommentDtoOut updateComment(@PathVariable @Positive final long commentId,
                                       @Valid @RequestBody CommentAdminDtoUpdate inputDTO) {

        log.info("START endpoint `method:PATCH /admin/comments/{commentId}` (update comment by admin), comment id: {}.", commentId);

        return service.updateByAdmin(commentId, inputDTO);
    }
}
