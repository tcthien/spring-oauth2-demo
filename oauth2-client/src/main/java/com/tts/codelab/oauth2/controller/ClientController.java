package com.tts.codelab.oauth2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tts.codelab.oauth2.client.FooClient;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    FooClient client;
    
    @GetMapping
    public String fooRead() {
        return "Client: " + client.fooRead(); 
    }
}
