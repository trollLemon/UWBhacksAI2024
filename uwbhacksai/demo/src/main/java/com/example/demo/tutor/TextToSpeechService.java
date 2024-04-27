package com.example.demo.tutor;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.models.SpeechGenerationOptions;
import com.azure.ai.openai.models.SpeechVoice;
import com.azure.core.util.BinaryData;

import org.springframework.stereotype.Service;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class TextToSpeechService{

    private final OpenAIClient openAIClient;

    public TextToSpeechService(OpenAIClient openAIClient) {
        this.openAIClient = openAIClient;
    }



    public void tts(String response) throws IOException {
        String deploymentOrModelId = "tts";


        SpeechGenerationOptions options = new SpeechGenerationOptions(
                response,
                SpeechVoice.ALLOY);

       BinaryData speech = openAIClient.generateSpeechFromText(deploymentOrModelId, options);
       Path path = Paths.get("./temp/speech.wav");
       Files.write(path, speech.toBytes());




     }

}
