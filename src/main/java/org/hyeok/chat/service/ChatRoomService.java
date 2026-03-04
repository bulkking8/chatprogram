package org.hyeok.chat.service;

import java.util.List;

import org.hyeok.chat.dto.ChatRoomRequestDto;
import org.hyeok.chat.entity.ChatRoom;
import org.hyeok.chat.entity.User;
import org.hyeok.chat.repository.ChatRoomRepository;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    
    
    
    public ChatRoomService(ChatRoomRepository chatRoomRepository) {
		this.chatRoomRepository = chatRoomRepository;
	}

	// 모든 방 목록 조회
    public List<ChatRoom> findAllRooms() {
        return chatRoomRepository.findAll();
    }
    
    @Transactional
    public boolean deleteRoom(Long roomId, Long currentUserId) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 방입니다."));

        // 방장의 ID와 현재 로그인한 유저의 ID를 비교
        if (room.getOwner().getId().equals(currentUserId)) {
            chatRoomRepository.delete(room);
            return true;
        }
        return false;
    }
    
 // ChatRoomService.java 안에 추가
    public ChatRoom findRoomById(Long roomId) {
        return chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 방입니다."));
    }
    
    @Transactional
    public void updateParticipantCount(Long roomId, int delta) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 방입니다."));
        
        // 현재 인원 + delta (입장 시 1, 퇴장 시 -1)
        int updatedCount = room.getUserCount() + delta;
        
        // 인원이 0보다 작아지지 않도록 방어 로직
        room.setUserCount(Math.max(0, updatedCount));
        
        // @Transactional이 붙어있으므로 save를 따로 호출하지 않아도 DB에 반영됩니다.
    }

    // 방 생성
    public ChatRoom createRoom(ChatRoomRequestDto dto, User owner) {
        ChatRoom room = new ChatRoom();
        room.setRoomName(dto.getRoomName());
        room.setPassword(dto.getPassword());
        room.setMaxUser(dto.getMaxUser());
        room.setOwner(owner); // 방장 설정
        room.setUserCount(0); // 초기 인원
        return chatRoomRepository.save(room);
    }

   
}