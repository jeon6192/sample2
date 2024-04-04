package com.taekwang.tcast.exception;

import com.taekwang.tcast.model.dto.ErrorResponse;
import com.taekwang.tcast.model.enums.UserError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(UserException.class)
    protected ResponseEntity<ErrorResponse> handleUserException(UserException e) {
        log.error(e.getMessage());

        final UserError userError = e.getUserError();

        final ErrorResponse errorResponse = ErrorResponse.of(userError, e);

        return new ResponseEntity<>(errorResponse, userError.getHttpStatus());
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
        log.error(e.getMessage());

        final UserError userError = UserError.FORBIDDEN;

        final ErrorResponse errorResponse = ErrorResponse.of(userError);

        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({ HttpMessageNotReadableException.class, HttpRequestMethodNotSupportedException.class })
    protected ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(Exception e) {
        log.error(e.getMessage());

        final UserError userError = UserError.BAD_REQUEST;

        final ErrorResponse errorResponse = ErrorResponse.of(userError);

        return new ResponseEntity<>(errorResponse, userError.getHttpStatus());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    protected ModelAndView handleNoHandlerFoundException(NoHandlerFoundException e) {
        log.error(e.getMessage());

        return new ModelAndView("error");
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error(e.getMessage());

        final UserError userError = UserError.NOT_DEFINED;

        final ErrorResponse errorResponse = ErrorResponse.of(userError);

        return new ResponseEntity<>(errorResponse, userError.getHttpStatus());
    }
}
