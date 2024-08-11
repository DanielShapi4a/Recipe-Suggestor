package com.example.recipes_suggester.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAIConfig {

    @Value("${OPENAI_KEY}")
    private String openaiApiKey;

    @Bean
    public String openaiApiKey() {
        return openaiApiKey;
    }
}