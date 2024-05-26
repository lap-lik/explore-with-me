package ru.practicum.ewmmain.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotFoundException extends RuntimeException {

    private String message;
}
