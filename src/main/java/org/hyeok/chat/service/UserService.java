package org.hyeok.chat.service;

import org.hyeok.chat.entity.User;
import org.hyeok.chat.repository.UserRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    
    

    public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}



	// 로그인 및 회원가입 (있으면 로그인, 없으면 가입시키는 초간단 방식)
    public User login(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(u -> u.getPassword().equals(password)) // 비번 일치 확인
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setUsername(username);
                    newUser.setPassword(password);
                    return userRepository.save(newUser);
                });
    }
}