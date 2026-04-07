package com.back.myboilerplate.domain.ai.service;

import com.back.myboilerplate.domain.ai.exception.AiException;
import com.back.myboilerplate.domain.ai.exception.AiExceptionInformation;
import com.back.myboilerplate.global.advisor.CustomLoggingAdvisor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

@Slf4j
@Service
public class AiService {
    private final Map<String, ChatClient> chatClients;
    private final ChatMemory chatMemory;
    private final String systemPromptTemplate;

    public AiService(
            Map<String, ChatClient> chatClients,
            ChatMemory chatMemory,
            @Qualifier("systemPromptTemplate") String systemPromptTemplate
    ) {
        this.chatClients = chatClients;
        this.chatMemory = chatMemory;
        this.systemPromptTemplate = systemPromptTemplate;
    }

    public String chat(String modelName, String conversationId, String message) {
        String clientBeanName;
        
        if (modelName == null) {
            clientBeanName = "groqChatClient";
        } else {
            String expectedBeanName = modelName + "ChatClient";
            if (!chatClients.containsKey(expectedBeanName)) {
                throw new AiException(AiExceptionInformation.MODEL_NOT_FOUND);
            }
            clientBeanName = expectedBeanName;
        }
        
        ChatClient selectedClient = chatClients.get(clientBeanName);

        String currentDate = LocalDate.now().toString();
        String systemPrompt = systemPromptTemplate.replace("{currentDate}", currentDate);

        try {
            return selectedClient.prompt()
                    .system(systemPrompt)
                    .user(message)
                    .advisors(spec -> spec
                            .advisors(
                                    new CustomLoggingAdvisor(),
                                    MessageChatMemoryAdvisor.builder(chatMemory)
                                            .conversationId(conversationId)
                                            .build()
                            )
                            .param("conversationId", conversationId))
                    .call()
                    .content();
        } catch (Exception e) {
            log.error("AI Client Error [model: {}, conversationId: {}]: {}", clientBeanName, conversationId, e.getMessage(), e);
            throw new AiException(AiExceptionInformation.AI_CLIENT_ERROR);
        }
    }
}

