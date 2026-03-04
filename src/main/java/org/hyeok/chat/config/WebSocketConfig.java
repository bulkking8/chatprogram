package org.hyeok.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // 웹소켓 메시지 브로커를 활성화합니다.
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 1. 웹소켓 연결을 위한 엔드포인트 설정 (ws://localhost:8085/ws-chat)
        registry.addEndpoint("/ws-chat")
                .setAllowedOriginPatterns("*") // 모든 도메인 허용 (테스트용)
                .withSockJS(); // 웹소켓을 지원하지 않는 브라우저를 위한 백업 설정
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 2. 메시지를 받을 때: /sub로 시작하는 주소를 구독하는 사람들에게 메시지 전달
        registry.enableSimpleBroker("/sub");
        
        // 3. 메시지를 보낼 때: 서버의 처리 로직으로 보낼 때 사용하는 접두사
        registry.setApplicationDestinationPrefixes("/pub");
    }
}