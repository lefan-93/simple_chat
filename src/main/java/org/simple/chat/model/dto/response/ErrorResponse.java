package org.simple.chat.model.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class ErrorResponse {

    private final List<String> errors;
}
