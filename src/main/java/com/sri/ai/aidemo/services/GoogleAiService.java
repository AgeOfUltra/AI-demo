package com.sri.ai.aidemo.services;

import com.sri.ai.aidemo.text.prompttemplate.dto.CountryCuisines;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

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

    @Override
    public String getTravelGuidence(String city, String month, String language, String budget) {
        PromptTemplate promptTemplate = new PromptTemplate("Welcome to the {city} travel guide!\n" +
                "If you're visiting in {month}, here's what you can do:\n" +
                "1. Must-visit attractions.\n" +
                "2. Local cuisine you must try.\n" +
                "3. Useful phrases in {language}.\n" +
                "4. Tips for traveling on a {budget} budget.\n" +
                "Enjoy your trip!");
        Prompt prompt = promptTemplate.create(Map.of("city", city, "month", month, "language", language, "budget", budget));

        System.out.println(prompt.getContents());

        return chatClient.prompt(prompt).call().chatResponse().getResult().getOutput().getText();
    }

    @Override
    public CountryCuisines getCuisines(String country, String numCuisines, String language) {

        PromptTemplate promptTemplate = new PromptTemplate("You are an expert in traditional cuisines.\n" +
                "You provide information about a specific dish from a specific country.\n" +
                "Answer the question: What is the traditional cuisine of {country}?" +
                "Return a list of {numCuisines} in {language}");
        Prompt prompt = promptTemplate.create(Map.of("country", country, "numCuisines", numCuisines, "language", language));


        return chatClient.prompt(prompt).call().entity(CountryCuisines.class);
    }
}
