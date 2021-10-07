package org.simple.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class TestController {

    @GetMapping("testForAll")
    public ResponseEntity<String> getTestForAll() {
        return new ResponseEntity<>("testForAll", HttpStatus.OK);
    }

    @GetMapping("test")
    public ResponseEntity<String> getTest() {
        return new ResponseEntity<>("test", HttpStatus.OK);
    }
}
