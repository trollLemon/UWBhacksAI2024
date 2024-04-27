package com.example.demo.tutor;

import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.models.AudioTranscription;
import com.azure.ai.openai.models.AudioTranscriptionFormat;
import com.azure.ai.openai.models.AudioTranscriptionOptions;
import com.azure.core.util.BinaryData;

@Service
public class SpeechToTextService {
    private final OpenAIClient openAIClient;

    public SpeechToTextService(OpenAIClient openAIClient) {
        this.openAIClient = openAIClient;
    }

    public String getTranscription(String fileName) {
        // TODO: Fix path file and correctly read from uploaded file
        Path filePath = Paths.get("path/to/audio/file");

        byte[] file = BinaryData.fromFile(filePath).toBytes();
        AudioTranscriptionOptions transcriptionOptions = new AudioTranscriptionOptions(file)
                .setResponseFormat(AudioTranscriptionFormat.JSON);

        AudioTranscription transcription = openAIClient.getAudioTranscription(
                "whisper-1",
                fileName,
                transcriptionOptions
        );

        return transcription.getText();
    }
}