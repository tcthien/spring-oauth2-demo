package com.tts.codelab.oauth2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tts.codelab.oauth2.client.FooClient;
import com.tts.codelab.oauth2.client.UserClient;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    FooClient client;
    
    @Autowired
    UserClient userClient;
    
    @GetMapping(path = "/fooRead")
    public String fooRead() {
        return "Client: " + client.fooRead(); 
    }
    
    @GetMapping(path = "/getUser")
    public String getUser() {
        return "Client: " + userClient.getUser(); 
    }
}
