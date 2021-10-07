package org.simple.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.simple.chat")
public class ServerApp {

    public static void main(String[] args){
        SpringApplication.run(ServerApp.class, args);
    }
}
