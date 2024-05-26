package ru.practicum.ewmmain.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UnsupportedException extends RuntimeException {

    private final String message;
}
