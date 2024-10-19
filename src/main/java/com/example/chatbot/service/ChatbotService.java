package com.example.chatbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatbotService {
    private final Assistant assistant;

    public String getResponse(String prompt){
        return assistant.answer(prompt);
    }

}
