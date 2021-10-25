package org.simple.chat.model.dto.request;

import lombok.Data;

@Data
public class NewUser {

    private String username;
    private String email;
    private String password;
}
