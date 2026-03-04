package org.hyeok.chat.controller;

import java.util.List;

import org.hyeok.chat.dto.ChatMessageDto;
import org.hyeok.chat.dto.ChatRoomRequestDto;
import org.hyeok.chat.entity.ChatRoom;
import org.hyeok.chat.entity.User;
import org.hyeok.chat.service.ChatRoomService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
@Controller
@RequestMapping("/rooms")
public class RoomController {

    private final ChatRoomService chatRoomService;
    private final SimpMessagingTemplate messagingTemplate;

    

    public RoomController(ChatRoomService chatRoomService, SimpMessagingTemplate messagingTemplate) {
		this.chatRoomService = chatRoomService;
		this.messagingTemplate = messagingTemplate;
	}

	// 1. 방 목록 페이지
    @GetMapping("/list")
    public String roomList(Model model, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/";
        }
        List<ChatRoom> rooms = chatRoomService.findAllRooms();
        model.addAttribute("rooms", rooms);
        return "lobby"; 
    }

    // 2. 방 생성 처리
    @PostMapping("/create")
    public String createRoom(@ModelAttribute ChatRoomRequestDto dto, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        chatRoomService.createRoom(dto, user);
        return "redirect:/rooms/list";
    }

    // 3. 채팅방 입장 (인원수 +1 추가)
    @GetMapping("/enter/{roomId}")
    public String enterRoom(@PathVariable Long roomId, 
                            @RequestParam(required = false) String password, 
                            Model model, HttpSession session) {
        if (session.getAttribute("user") == null) return "redirect:/";

        // Repository 대신 Service를 호출하여 방 정보를 가져옵니다.
        ChatRoom room = chatRoomService.findRoomById(roomId);

        // 1. 방에 비밀번호가 설정되어 있는 경우 검증
        if (room.getPassword() != null && !room.getPassword().isEmpty()) {
            if (!room.getPassword().equals(password)) {
                return "redirect:/rooms/list?error=wrong_password";
            }
        }

        // 2. 검증 통과 시 인원수 증가 및 입장
        chatRoomService.updateParticipantCount(roomId, 1);
        model.addAttribute("roomId", roomId);
        return "chat"; 
    }

    @GetMapping("/exit/{roomId}")
    public String exitRoom(@PathVariable Long roomId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        
        if (user != null) {
            chatRoomService.updateParticipantCount(roomId, -1);
            
            ChatMessageDto leaveMsg = new ChatMessageDto();
            // 문자열이 아닌 Enum 상수를 사용해 오류 해결
            leaveMsg.setType(ChatMessageDto.MessageType.LEAVE); 
            // JS에서 '익명' 말풍선이 생성되는 것을 방지하기 위해 빈 문자열 설정
            leaveMsg.setSender(""); 
            leaveMsg.setRoomId(roomId.toString());
            leaveMsg.setMessage(user.getUsername() + "님이 퇴장하셨습니다.");
            
            messagingTemplate.convertAndSend("/sub/chat/room/" + roomId, leaveMsg);
        }
        return "redirect:/rooms/list";
    }
    
    @PostMapping("/delete/{roomId}")
    public String deleteRoom(@PathVariable Long roomId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/";

        boolean isDeleted = chatRoomService.deleteRoom(roomId, user.getId());
        
        // 삭제 성공 시 목록으로, 실패 시(권한 없음 등) 알림과 함께 리턴
        return "redirect:/rooms/list" + (isDeleted ? "" : "?error=no_permission");
    }
    
}