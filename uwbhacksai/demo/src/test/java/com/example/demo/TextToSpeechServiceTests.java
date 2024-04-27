package com.example.demo;
import static org.mockito.Mockito.mock;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import com.azure.core.util.BinaryData;
import com.example.demo.tutor.TextToSpeechService;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
@SpringBootTest(classes = {TextToSpeechService.class, ExternalApiConfig.class})
class TextToSpeechServiceTest {
    @Autowired
    private TextToSpeechService instance;

    @Test
    public void testTts() throws IOException {
        String response = "Hello and welcome! Today, we're going to test your understanding of generative pre-trained transformers (GPT). I'll be asking you a series of questions to see how well you grasp the concepts we've discussed. Feel free to express your thoughts and reasoning as we go along. Are you ready to dive into the world of GPTs?Let's start with the first question:Can you explain what a generative pre-trained transformer (GPT) is and how it differs from other types of language models in the field of artificial intelligence?";

        // Calling the method to be tested
        byte[] speech = instance.tts(response);

        // Verifying behavior
        // You may add more specific assertions depending on your requirements
        Path outputPath = Paths.get("./temp/tutor_response.wav");
        assertTrue(Files.exists(outputPath), "Output file should be created");
    }

}
