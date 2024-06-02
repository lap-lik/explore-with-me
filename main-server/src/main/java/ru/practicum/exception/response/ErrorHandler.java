package ru.practicum.exception.response;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.practicum.category.controller.CategoryAdminController;
import ru.practicum.category.controller.CategoryPublicController;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.NotImplementedException;
import ru.practicum.exception.UnsupportedException;
import ru.practicum.exception.ValidException;
import ru.practicum.user.controller.UserAdminController;

import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice(assignableTypes =
        {CategoryAdminController.class,
                CategoryPublicController.class,
                UserAdminController.class
        })
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidException.class)
    public ErrorResponse onValidException(final RuntimeException exception) {

        log.warn("Exception: {}, Validation error(s): \n{}", exception.getClass().getName(),
                getExceptionMessage(exception));

        return ErrorResponse.builder()
                .errors(convertStackTraceToStringList(exception))
                .status(HttpStatus.BAD_REQUEST.name())
                .reason("Incorrectly made request.")
                .message(exception.getMessage())
                .build();
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorResponse onConstraintViolationException(final RuntimeException exception) {

        log.warn("Exception: {}, ConstraintViolation error(s): \n{}", exception.getClass().getName(),
                getExceptionMessage(exception));

        return ErrorResponse.builder()
                .errors(convertStackTraceToStringList(exception))
                .status(HttpStatus.BAD_REQUEST.name())
                .reason("Incorrectly made request.")
                .message(exception.getMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UnsupportedException.class)
    public ErrorResponse onUnsupportedException(final RuntimeException exception) {

        log.warn("Exception: {}, Unsupported error(s): \n{}", exception.getClass().getName(),
                getExceptionMessage(exception));

        return ErrorResponse.builder()
                .errors(convertStackTraceToStringList(exception))
                .status(HttpStatus.BAD_REQUEST.name())
                .reason("Incorrectly made request.")
                .message(exception.getMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ErrorResponse onNotFoundException(final NotFoundException exception) {

        log.warn("Exception: {}, Not found: \n{}", exception.getClass().getName(), getExceptionMessage(exception));

        return ErrorResponse.builder()
                .errors(convertStackTraceToStringList(exception))
                .status(HttpStatus.NOT_FOUND.name())
                .reason("The required object was not found. ")
                .message(exception.getMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorResponse onDataIntegrityViolationException(final DataIntegrityViolationException exception) {

        log.warn("DataIntegrityViolationException: {}, message(s): \n{}", exception.getClass().getName(),
                getExceptionMessage(exception));

        return ErrorResponse.builder()
                .errors(convertStackTraceToStringList(exception))
                .status(HttpStatus.CONFLICT.name())
                .reason("Integrity constraint has been violated.")
                .message(exception.getMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({NotImplementedException.class, Exception.class})
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
