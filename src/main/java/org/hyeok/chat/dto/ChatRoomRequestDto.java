package org.hyeok.chat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ChatRoomRequestDto {
    private String roomName;
    private String password;
    private int maxUser;
	
    
    
    public ChatRoomRequestDto() {
	}
	public ChatRoomRequestDto(String roomName, String password, int maxUser) {
		this.roomName = roomName;
		this.password = password;
		this.maxUser = maxUser;
	}
	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getMaxUser() {
		return maxUser;
	}
	public void setMaxUser(int maxUser) {
		this.maxUser = maxUser;
	}
    
    
}