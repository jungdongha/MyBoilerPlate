package com.back.myboilerplate.global.advisor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.util.StopWatch;

/**
 * AI 요청 및 응답을 상세히 로깅하고 실행 시간을 측정하는 커스텀 어드바이저.
 * Spring AI 2.0.0-M3 정식 API 구조 반영 (ChatClientRequest.prompt() 사용).
 */
@Slf4j
public class CustomLoggingAdvisor implements CallAdvisor {

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest request, CallAdvisorChain chain) {
        log.info("==== [AI Request Start] ====");
        
        // request.prompt()를 통해 사용자 메시지 추출
        if (request.prompt() != null && request.prompt().getUserMessage() != null) {
            log.info("User Message: {}", request.prompt().getUserMessage().getText());
        }
        
        // 실행 시간 측정을 위한 StopWatch 시작
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        // 다음 체인 실행
        ChatClientResponse chatClientResponse = chain.nextCall(request);

        stopWatch.stop();
        log.info("==== [AI Request Completed] ====");
        
        ChatResponse chatResponse = chatClientResponse.chatResponse();
        if (chatResponse != null) {
            if (chatResponse.getResult() != null && chatResponse.getResult().getOutput() != null) {
                log.info("AI Response: {}", chatResponse.getResult().getOutput().getText());
            }

            // 토큰 사용량 정보 추출
            Usage usage = chatResponse.getMetadata().getUsage();
            if (usage != null) {
                log.info("Token Usage - Prompt: {}, Completion: {}, Total: {}",
                        usage.getPromptTokens(),
                        usage.getCompletionTokens(),
                        usage.getTotalTokens());
            }
        }

        // 실행 시간(ms) 출력
        log.info("Total Execution Time: {} ms", stopWatch.getTotalTimeMillis());
        log.info("================================");

        return chatClientResponse;
    }

    @Override
    public String getName() {
        return "CustomLoggingAdvisor";
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
