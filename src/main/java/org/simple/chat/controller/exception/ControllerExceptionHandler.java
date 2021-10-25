package org.simple.chat.controller.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.simple.chat.model.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Arrays;
import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ControllerExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public @ResponseBody ErrorResponse unknownError(Exception e){
        log.error("Unknown error during rest request:" + Arrays.toString(e.getStackTrace()));
        return new ErrorResponse(List.of(e.getMessage()));
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConflictException.class)
    public @ResponseBody ErrorResponse conflictError(ConflictException e){
        log.error(e.getMessage());
        return new ErrorResponse(List.of(e.getMessage()));
    }
}
