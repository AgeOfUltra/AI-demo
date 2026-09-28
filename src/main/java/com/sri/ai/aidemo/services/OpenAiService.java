package com.sri.ai.aidemo.services;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;

@Service
public class OpenAiService {
//
//	private final ChatClient chatClient;
//
//    public OpenAiService(ChatClient.Builder chatClient, ChatMemory memory) {
////        ChatMemory sanitized = new ReasoningStrippingChatMemory(memory); // to drop the Assistance message from the response
//        this.chatClient = chatClient
////                .defaultAdvisors(MessageChatMemoryAdvisor.builder(sanitized).build())
//                .build();
//    }
//
//    public ChatResponse generateAnswer(String question){
////        OpenAiChatOptions options =  OpenAiChatOptions
////                .builder()
////                .model("llama-3.3-70b-versatile")
////                .temperature(0.7)
////                .maxTokens(20)
////                .build();
//
//        return chatClient.prompt(question)
////                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID,"abc-123"))
//                .call().chatResponse();
//    }
}
