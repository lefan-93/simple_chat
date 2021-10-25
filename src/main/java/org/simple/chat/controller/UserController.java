package org.simple.chat.controller;

import lombok.AllArgsConstructor;
import org.simple.chat.model.dto.request.NewUser;
import org.simple.chat.model.dto.response.FriendDto;
import org.simple.chat.model.dto.response.ProfileDto;
import org.simple.chat.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.simple.chat.utils.PrincipalUtils.getUserId;

@CrossOrigin
@RestController
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("user")
    public ResponseEntity<String> createUser(@RequestBody NewUser newUser) {

        userService.createUser(newUser);

        return new ResponseEntity<>("created", HttpStatus.CREATED);
    }

    @GetMapping("user")
    public ResponseEntity<List<FriendDto>> getUser(@RequestParam String username) {
        return new ResponseEntity<>(userService.findUser(username), HttpStatus.OK);
    }

    @GetMapping("me")
    public ResponseEntity<ProfileDto> getMeProfile() {
        String userId = getUserId();
        return new ResponseEntity<>(userService.getMeProfile(userId), HttpStatus.OK);
    }
}
