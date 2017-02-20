package com.tts.codelab.oauth2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/foo")
public class UserController {

    @GetMapping
    public String getUser() {
        return "UserController.getUser";
    }
}
