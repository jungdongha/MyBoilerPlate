package com.back.myboilerplate.global.config;

import com.back.myboilerplate.global.properties.AiProperties;
import org.springframework.ai.anthropic.AnthropicChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.nio.charset.StandardCharsets;

@Configuration
@EnableConfigurationProperties(AiProperties.class)
public class AiConfig {

    @Bean
    String systemPromptTemplate() {
        try {
            return new ClassPathResource("prompts/system.st").getContentAsString(StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "";
        }
    }

    //chat memory (inmemory)
    @Bean
    ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .maxMessages(20)
                .build();
    }

    //gemini
    @Bean("geminiChatClient")
    ChatClient geminiChatClient(GoogleGenAiChatModel model) {
        return ChatClient.builder(model)
                .defaultSystem("당신은 정확하고 논리적이고 친절한 어시스턴트이다.")
                .build();
    }

    //claude
    @Bean("claudeChatClient")
    ChatClient claudeChatClient(AnthropicChatModel model) {
        return ChatClient.builder(model).build();
    }

    //groq (openai-compatible)
    @Bean("groqChatClient")
    ChatClient groqChatClient(AiProperties aiProperties) {
        OpenAiApi openAiApi = OpenAiApi.builder()
                .baseUrl(aiProperties.baseUrl())
                .apiKey(aiProperties.apiKey())
                .build();
        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .model(aiProperties.model())
                .maxTokens(aiProperties.maxTokens())
                .build();
        return ChatClient.builder(OpenAiChatModel.builder()
                        .openAiApi(openAiApi)
                        .defaultOptions(options)
                        .build())
                .build();
    }
}
