package org.simple.chat.controller.exception;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;

public class ConflictException extends ClientErrorException {
    public ConflictException(String message, Response.Status status) {
        super(message, status);
    }
}
