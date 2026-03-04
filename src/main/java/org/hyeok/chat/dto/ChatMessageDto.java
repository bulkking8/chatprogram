package org.hyeok.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDto {
    private String roomId;    // 방 번호
    private String sender;    // 보낸 사람
    private String message;   // 내용
    private MessageType type; // 입장, 퇴장, 채팅 구분
    
    
    
    
    public ChatMessageDto() {
	}




	public ChatMessageDto(String roomId, String sender, String message, MessageType type) {
		this.roomId = roomId;
		this.sender = sender;
		this.message = message;
		this.type = type;
	}




	public String getRoomId() {
		return roomId;
	}




	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}




	public String getSender() {
		return sender;
	}




	public void setSender(String sender) {
		this.sender = sender;
	}




	public String getMessage() {
		return message;
	}




	public void setMessage(String message) {
		this.message = message;
	}




	public MessageType getType() {
		return type;
	}




	public void setType(MessageType type) {
		this.type = type;
	}




	public enum MessageType {
        ENTER, TALK, LEAVE
    }




	
}