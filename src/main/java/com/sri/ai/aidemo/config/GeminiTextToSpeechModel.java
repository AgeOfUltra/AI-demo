package com.sri.ai.aidemo.config;

import com.google.genai.Client;
import com.google.genai.types.*;
import org.springframework.ai.audio.tts.Speech;
import org.springframework.ai.audio.tts.TextToSpeechModel;
import org.springframework.ai.audio.tts.TextToSpeechPrompt;
import org.springframework.ai.audio.tts.TextToSpeechResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class GeminiTextToSpeechModel implements TextToSpeechModel {

    private final Client client;
    private final String ttsModel;
    private final String voice;

    public GeminiTextToSpeechModel(
            @Value("${spring.ai.google.genai.api-key}") String apiKey,
            @Value("${spring.ai.google.genai.tts.model}") String ttsModel,
            @Value("${spring.ai.google.genai.tts.voice}") String voice) {
        this.client = Client.builder().apiKey(apiKey).build();
        this.ttsModel = ttsModel;
        this.voice = voice;
    }

    @Override
    public TextToSpeechResponse call(TextToSpeechPrompt prompt) {
        byte[] wav = generate(prompt.getInstructions().getText());
        return new TextToSpeechResponse(List.of(new Speech(wav)));
    }

    @Override
    public Flux<TextToSpeechResponse> stream(TextToSpeechPrompt prompt) {
        // Gemini call here is not chunked, so emit the full result as one item
        return Flux.just(call(prompt));
    }

    private byte[] generate(String text) {
        GenerateContentConfig config = GenerateContentConfig.builder()
                .responseModalities(List.of("AUDIO"))
                .speechConfig(SpeechConfig.builder()
                        .voiceConfig(VoiceConfig.builder()
                                .prebuiltVoiceConfig(PrebuiltVoiceConfig.builder()
                                        .voiceName(voice)
                                        .build())
                                .build())
                        .build())
                .build();

        try {
            GenerateContentResponse response =
                    client.models.generateContent(ttsModel, text, config);

            byte[] pcm = response.candidates().get().get(0)
                    .content().get()
                    .parts().get().get(0)
                    .inlineData().get()
                    .data().get();

            return pcmToWav(pcm, 24000, 1, 16);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate speech with Gemini API", e);
        }
    }

    // Gemini returns raw PCM (16-bit, 24 kHz, mono); browsers need a WAV header
    private byte[] pcmToWav(byte[] pcm, int sampleRate, int channels, int bitsPerSample) {
        int byteRate = sampleRate * channels * bitsPerSample / 8;
        int blockAlign = channels * bitsPerSample / 8;

        ByteBuffer buf = ByteBuffer.allocate(44 + pcm.length).order(ByteOrder.LITTLE_ENDIAN);
        buf.put("RIFF".getBytes(StandardCharsets.US_ASCII));
        buf.putInt(36 + pcm.length);
        buf.put("WAVE".getBytes(StandardCharsets.US_ASCII));
        buf.put("fmt ".getBytes(StandardCharsets.US_ASCII));
        buf.putInt(16);
        buf.putShort((short) 1);
        buf.putShort((short) channels);
        buf.putInt(sampleRate);
        buf.putInt(byteRate);
        buf.putShort((short) blockAlign);
        buf.putShort((short) bitsPerSample);
        buf.put("data".getBytes(StandardCharsets.US_ASCII));
        buf.putInt(pcm.length);
        buf.put(pcm);
        return buf.array();
    }
}