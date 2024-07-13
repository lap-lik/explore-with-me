package ru.practicum.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentPrivateDtoUpdate {

    @NotBlank(message = "The name must not be empty.")
    @Length(min = 3, max = 1500, message = "The minimum length of the text is 3, the maximum is 1500.")
    private String text;
}
