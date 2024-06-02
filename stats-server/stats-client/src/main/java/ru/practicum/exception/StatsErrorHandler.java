package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.practicum.StatsClient;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice(assignableTypes = StatsClient.class)
public class StatsErrorHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ClientException.class)
    public ErrorResponse onClientException(final RuntimeException exception) {

        log.warn("Exception: {}, ClientException error(s): \n{}", exception.getClass().getName(),
                getExceptionMessage(exception));

        return ErrorResponse.builder()
                .errors(convertStackTraceToStringList(exception))
                .status(HttpStatus.BAD_REQUEST.name())
                .reason("Incorrectly made request.")
                .message(exception.getMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse onThrowableException(final Exception exception) {

        log.error("Exception: {}, message(s): \n{}", exception.getClass().getName(), getExceptionMessage(exception));

        return ErrorResponse.builder()
                .errors(convertStackTraceToStringList(exception))
                .status(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .reason("Incorrectly made request.")
                .message(exception.getMessage())
                .build();
    }

    private String getExceptionMessage(Throwable exception) {

        return Arrays.stream(exception.getMessage().split("&"))
                .map(message -> "- " + message.trim())
                .collect(Collectors.joining("\n"));
    }

    private List<String> convertStackTraceToStringList(Throwable exception) {

        return Arrays.stream(exception.getStackTrace())
                .map(StackTraceElement::toString)
                .collect(Collectors.toList());
    }
}
