package com.example.demo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import com.azure.core.util.BinaryData;
import com.azure.core.util.Configuration;

import com.example.demo.tutor.TextToSpeechService;
import com.example.demo.tutor.OpenAIConfig;
import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.ai.openai.models.SpeechGenerationOptions;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {TextToSpeechService.class, OpenAIConfig.class})
class TextToSpeechServiceTest {
    @Autowired
    private TextToSpeechService instance;

    @Test
    public void testTts() throws IOException {
        String response = "Hello, world!";

        // Calling the method to be tested
        instance.tts(response);

        // Verifying behavior
        // You may add more specific assertions depending on your requirements
        Path outputPath = Paths.get("./azure-ai-openai/src/samples/java/com/azure/ai/openai/resources/speech.wav");
        assertTrue(Files.exists(outputPath), "Output file should be created");
    }

}
