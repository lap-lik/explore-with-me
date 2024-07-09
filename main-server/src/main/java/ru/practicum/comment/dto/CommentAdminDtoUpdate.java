package ru.practicum.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.comment.model.CommentState;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentAdminDtoUpdate {

    private CommentState state;
}
