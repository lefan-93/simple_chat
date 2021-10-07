package org.simple.chat.controller;

import lombok.AllArgsConstructor;
import org.keycloak.authorization.client.util.HttpResponseException;
import org.keycloak.representations.AccessTokenResponse;
import org.simple.chat.model.dto.request.AuthenticationRequest;
import org.simple.chat.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin
@RestController
@AllArgsConstructor
public class AuthenticationController {

    private final UserService userService;

    @PostMapping("login")
    public ResponseEntity<AccessTokenResponse> login(@RequestBody AuthenticationRequest loginInfo) {
        try {
            AccessTokenResponse response = userService.auth(loginInfo);
            if (response == null) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error");
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (HttpResponseException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "wrong username or password");
        }
    }
}

