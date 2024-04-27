package com.example.demo.tutor;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.ai.openai.models.SpeechGenerationOptions;
import com.azure.ai.openai.models.SpeechVoice;
import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.util.BinaryData;
import com.azure.core.util.Configuration;

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
        String azureOpenaiKey =  Configuration.getGlobalConfiguration().get("AZURE_OPENAI_KEY");
        String endpoint = Configuration.getGlobalConfiguration().get("AZURE_OPENAI_ENDPOINT");
        String deploymentOrModelId = "tts";


        SpeechGenerationOptions options = new SpeechGenerationOptions(
                response,
                SpeechVoice.ALLOY);

        BinaryData speech = openAIClient.generateSpeechFromText(deploymentOrModelId, options);
        // Checkout your generated speech in the file system.
        Path path = Paths.get("./azure-ai-openai/src/samples/java/com/azure/ai/openai/resources/speech.wav");
        Files.write(path, speech.toBytes());




     }

}
