package com.sri.ai.aidemo.services;

import com.sri.ai.aidemo.text.prompttemplate.dto.CountryCuisines;
import org.springframework.ai.chat.model.ChatResponse;

public interface AIService {
    ChatResponse generateAnswer(String question);

    String getTravelGuidence(String city, String month, String language, String budget);

    CountryCuisines getCuisines(String country, String numCuisines, String language);
}
