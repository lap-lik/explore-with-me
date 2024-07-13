package ru.practicum.comment.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class CommentDtoIn {

    @NotBlank(message = "The name must not be empty.")
    @Length(min = 3, max = 1500, message = "The minimum length of the text is 3, the maximum is 1500.")
    private String text;

    private Long parentCommentId;
}
