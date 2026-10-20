package com.sri.ai.aidemo.services;

import com.sri.ai.aidemo.text.prompttemplate.dto.CountryCuisines;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

import java.util.List;
import java.util.Map;

@Service
public class GoogleAiService implements  AIService{

    private final ChatClient chatClient;
    private final EmbeddingModel embeddingModel;
    private final VectorStore vectorStore;
    private final ImageModel genAiImageModel;



    public GoogleAiService(ChatClient.Builder chatClient, EmbeddingModel embeddingModel, VectorStore vectorStore, ImageModel genAiImageModel) {
        this.chatClient = chatClient.build();
        this.embeddingModel = embeddingModel;
        this.vectorStore = vectorStore;
        this.genAiImageModel = genAiImageModel;
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
                "Return a list of {numCuisines} in {language}" +
                "Avoid giving information about fictional places. If the country is fictional\n" +
                "or non-existent " +
                "answer: I don't know ask for valid Country!"
        );
        Prompt prompt = promptTemplate.create(Map.of("country", country, "numCuisines", numCuisines, "language", language));


        return chatClient.prompt(prompt).call().entity(CountryCuisines.class);
    }

    @Override
    public String interviewGuideHelper(String company, String jobTitle, String strength, String weakness) {
        PromptTemplate template = new PromptTemplate("You are a career coach. Provide tailored interview tips for the\n" +
                "position of {jobTitle} at {company}.\n" +
                "Highlight your strengths in {strengths} and prepare for questions\n" +
                "about your weaknesses such as {weaknesses}.");

        Prompt prompt = template.create(Map.of("jobTitle", jobTitle, "company", company, "strengths", strength,"weaknesses",weakness));

        return chatClient.prompt(prompt).call().chatResponse().getResult().getOutput().getText();
    }

    public float[] embed(String word){
       return embeddingModel.embed(word);

    }

    public String answer(String query){
        return chatClient.prompt(query)
                .advisors(QuestionAnswerAdvisor.builder(vectorStore).build())
                .call().chatResponse().getResult().getOutput().getText();
    }

    public double findSimilarity(String text1, String text2){

        List<float[]> embed = embeddingModel.embed(List.of(text1,text2));
        return cosineSimilarity(embed.get(0),embed.get(1));

    }

    private double cosineSimilarity(float[] vectorA, float[] vectorB) {
        if (vectorA.length != vectorB.length) {
            throw new IllegalArgumentException("Vectors must be of the same length");
        }

        // Initialize variables for dot product and magnitudes
        double dotProduct = 0.0;
        double magnitudeA = 0.0;
        double magnitudeB = 0.0;

        // Calculate dot product and magnitudes
        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            magnitudeA += vectorA[i] * vectorA[i];
            magnitudeB += vectorB[i] * vectorB[i];
        }

        // Calculate and return cosine similarity
        return dotProduct / (Math.sqrt(magnitudeA) * Math.sqrt(magnitudeB));
    }

    public List<Document> searchJob(String query){
        return vectorStore.similaritySearch(SearchRequest.builder().topK(3).query(query).build());
    }

    public  String generateImage(String query){// return binary image
        ImageResponse call = genAiImageModel.call(new ImagePrompt(query));

        return call.getResult().getOutput().getB64Json();

    }

    public String explainImage(String prompt, String path) {
        return chatClient.prompt().user(u-> u.text(prompt).media(MimeTypeUtils.IMAGE_JPEG,new FileSystemResource(path))).call().content();
    }

    public String getDietAdvice(String prompt, String path, String path1) {
        return chatClient.prompt()
                .user(u-> u.text(prompt)
                        .media(MimeTypeUtils.IMAGE_JPEG,new FileSystemResource(path))
                        .media(MimeTypeUtils.IMAGE_JPEG,new FileSystemResource(path1)))
                .call().content();

    }
}
