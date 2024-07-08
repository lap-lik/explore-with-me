package ru.practicum.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ClientException extends RuntimeException {

    private final String message;
}
