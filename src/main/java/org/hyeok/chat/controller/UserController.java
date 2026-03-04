package org.hyeok.chat.controller;

import org.hyeok.chat.entity.User;
import org.hyeok.chat.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

    private final UserService userService;

    // 생성자 주입 (Lombok을 쓰지 않기로 했으므로 직접 작성)
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 1. 로그인 페이지 접속 (localhost:8085/)
    @GetMapping("/")
    public String index() {
        return "index"; // templates/index.html을 보여줌
    }

    // 2. 로그인 처리
    @PostMapping("/login")
    public String login(@RequestParam String username, 
                        @RequestParam String password, 
                        HttpSession session) {
        try {
            // 서비스 로직 실행 (유저가 없으면 가입, 있으면 로그인)
            User user = userService.login(username, password);
            
            // 로그인 성공 시 세션에 유저 객체 저장
            session.setAttribute("user", user);
            
            // 로비(방 목록) 페이지로 이동
            return "redirect:/rooms/list";
        } catch (Exception e) {
            // 에러 발생 시 에러 파라미터를 들고 다시 로그인창으로
            return "redirect:/?error";
        }
    }

    // 3. 로그아웃 처리
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 삭제
        return "redirect:/";
    }
}