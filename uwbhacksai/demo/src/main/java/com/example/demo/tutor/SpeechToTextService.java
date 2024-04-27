package com.example.demo.tutor;

import org.springframework.stereotype.Service;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.models.AudioTranscription;
import com.azure.ai.openai.models.AudioTranscriptionFormat;
import com.azure.ai.openai.models.AudioTranscriptionOptions;

@Service
public class SpeechToTextService {
    private final OpenAIClient openAIClient;

    public SpeechToTextService(OpenAIClient openAIClient) {
        this.openAIClient = openAIClient;
    }

    public String getTranscription(byte[] audioData, String fileName) {
        AudioTranscriptionOptions transcriptionOptions = new AudioTranscriptionOptions(audioData)
                .setResponseFormat(AudioTranscriptionFormat.JSON);

        AudioTranscription transcription = openAIClient.getAudioTranscription(
                "whisper-1",
                fileName,
                transcriptionOptions
        );

        return transcription.getText();
    }
}