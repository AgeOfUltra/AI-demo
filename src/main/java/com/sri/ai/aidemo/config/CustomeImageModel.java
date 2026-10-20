package com.sri.ai.aidemo.config;
import org.springframework.ai.image.Image;
import org.springframework.ai.image.ImageGeneration;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Base64;
import java.util.List;
import java.util.Map;

@Component
@Primary
public class CustomeImageModel implements ImageModel {

    private final RestClient restClient;

    public CustomeImageModel(
            @Value("${custome.api-key}") String apiKey,
            @Value("${custome.base-url}") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .build();
    }

    @Override
    public ImageResponse call(ImagePrompt prompt) {
        // 1. Extract the prompt text
        String text = prompt.getInstructions().get(0).getText();

        // 2. Send only { "prompt": text } to "/" — your Worker accepts just the prompt
        Map<String, Object> body = Map.of("prompt", text);

        // 3. Read the response as raw JPEG bytes (your Worker returns image/jpeg directly)
        byte[] imageBytes = restClient.post()
                .uri("/")                              // ← root path, not /v1/images/generations
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .body(byte[].class);

        // 4. Base64-encode the bytes to fit the ImageModel b64_json contract
        String b64 = Base64.getEncoder().encodeToString(imageBytes);

        // 5. Wrap in Spring AI's ImageResponse
        return new ImageResponse(List.of(new ImageGeneration(new Image(null, b64))));
    }
}
