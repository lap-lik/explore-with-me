package ru.practicum.comment.dto;

import lombok.Data;

@Data
public class CommentDtoIn {

    private String text;

    private Long parentCommentId;
}
