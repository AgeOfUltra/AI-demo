package com.sri.ai.aidemo.config;

import org.springframework.ai.audio.transcription.AudioTranscription;
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.ai.audio.transcription.TranscriptionModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeType;

@Component
public class GemenaiTranscriptionModel implements TranscriptionModel {

    private final ChatClient chatClient;
    private final String model;


    public GemenaiTranscriptionModel(ChatClient.Builder builder,
                                    @Value("${spring.ai.model.audio.transcription}") String model) {
        this.chatClient = builder.build();
        this.model = model;
    }
    @Override
    public AudioTranscriptionResponse call(AudioTranscriptionPrompt prompt) {
        Resource audio = prompt.getInstructions();
        String mime = "audio/mp3"; // detect from file extension in real code

        String text = chatClient.prompt()
                .user(u -> u.text("Transcribe this audio. Return only the transcript.")
                        .media(MimeType.valueOf(mime), audio))
                .call()
                .content();

        return new AudioTranscriptionResponse(new AudioTranscription(text));
    }
}
