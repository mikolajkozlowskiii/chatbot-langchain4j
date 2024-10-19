package com.example.chatbot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chat/")
@RequiredArgsConstructor
public class PromptController {

    @PostMapping
    public ResponseEntity<String> generateResponse(@RequestBody String prompt){
        return null;
    }
}
