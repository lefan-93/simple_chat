package org.simple.chat.controller;

import lombok.AllArgsConstructor;
import org.simple.chat.model.dto.NewUser;
import org.simple.chat.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("user")
    public ResponseEntity<String> createUser(@RequestBody NewUser newUser) {

        userService.createUser(newUser);

        return new ResponseEntity<>("created", HttpStatus.OK);
    }

    @GetMapping("user")
    public ResponseEntity<String> getUser() {

        //userService.auth();
        return new ResponseEntity<>("test", HttpStatus.OK);
    }
}
