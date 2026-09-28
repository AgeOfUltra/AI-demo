package com.sri.ai.aidemo.services;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;

@Service
public class GoogleAiService implements  AIService{

    private final ChatClient chatClient;


    public GoogleAiService(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    public ChatResponse generateAnswer(String question){
        return chatClient.prompt(question)
//                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID,"abc-123"))
                .call().chatResponse();
    }
}
