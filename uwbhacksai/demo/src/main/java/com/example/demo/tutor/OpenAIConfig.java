package com.example.demo.tutor;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAIConfig {

    @Value("${azure.openai.key}")
    private String azureOpenaiKey;

    @Bean
    public OpenAIClient openAIClient() {
        return new OpenAIClientBuilder()
                .credential(new AzureKeyCredential(azureOpenaiKey))
                .buildClient();
    }
}
