package ru.practicum.service.exception.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class ErrorResponse {
    private String error;
    private String message;
}
