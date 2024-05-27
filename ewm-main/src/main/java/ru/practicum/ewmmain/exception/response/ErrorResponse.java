package ru.practicum.ewmmain.exception.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class ErrorResponse {
    private String status;
    private String reason;
    private String message;
    private String timestamp;
}
