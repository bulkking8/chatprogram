package org.hyeok.chat.controller;

import org.hyeok.chat.dto.ChatMessageDto;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    private final SimpMessageSendingOperations messagingTemplate;

    public MessageController(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat/message") // 클라이언트가 /pub/chat/message로 보낸 메시지를 처리
    public void message(ChatMessageDto message) {
        // 입장 메시지 처리 로직 등 추가 가능
        if (ChatMessageDto.MessageType.ENTER.equals(message.getType())) {
            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
        }
        
        // 해당 방을 구독 중인(/sub/chat/room/{roomId}) 모든 유저에게 메시지 전송
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }
}