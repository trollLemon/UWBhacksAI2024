package com.example.demo;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.KeyCredential;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExternalApiConfig {

    @Value("${openai.key}")
    private String openaiKey;

    @Bean
    public OpenAIClient openAIClient() {
        return new OpenAIClientBuilder()
                .credential(new KeyCredential(openaiKey))
                .buildClient();
    }

    @Value("${behavioral.key}")
    private String behavioralKey;

}
