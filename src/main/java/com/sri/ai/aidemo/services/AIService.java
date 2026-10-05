package com.sri.ai.aidemo.services;

import org.springframework.ai.chat.model.ChatResponse;

public interface AIService {
    ChatResponse generateAnswer(String question);

    String getTravelGuidence(String city, String month, String language, String budget);
}
