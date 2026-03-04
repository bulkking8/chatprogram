# 💬 Real-time Chatting Service
> **Spring Boot & STOMP 기반의 실시간 채팅 플랫폼**
> 
> 사용자 인증(Session)과 실시간 통신을 유기적으로 결합하고, Enum 기반의 데이터 구조 설계로 유지보수성을 높인 프로젝트입니다.

---

## 🛠 Tech Stack
- **Backend**: Java 17, Spring Boot 3.x, Spring Data JPA
- **Database**: MySQL 8.0
- **Real-time**: WebSocket, STOMP, SockJS
- **Frontend**: JavaScript, Thymeleaf, CSS3

---

## 🗂 System Architecture
* **User Authentication**: `HttpSession`을 통해 사용자 정보를 관리하며, 채팅 참여 시 세션 정보를 기반으로 사용자를 식별합니다.
* **Message Broker**: STOMP 프로토콜의 `Pub/Sub` 모델을 채택하여 서버 부하를 최소화하고 실시간 브로드캐스팅을 구현했습니다.
* **Data Persistence**: 채팅방 정보(제목, 인원수, 비밀번호)를 **MySQL**에 영속화하여 안정적인 서비스 운영이 가능하도록 설계했습니다.



---

## ✨ Key Features

### 🔐 Auth Service (회원가입/로그인)
* **회원가입**: 닉네임 중복 체크 및 이메일 인증을 통한 본인 확인 프로세스 구현
* **세션 인증**: 로그인 시 `User` 객체를 세션에 저장하여 채팅 발신자(`sender`) 정보와 자동 매핑
* **보안**: 비밀번호 암호화 저장 및 미인증 사용자의 채팅방 접근 차단(Redirect 처리)

### 💬 Chat Service (채팅 서비스)
* **실시간 방 관리**: 채팅방 생성, 비밀번호 기반 비공개 입장 및 실시간 참여 인원 증감 로직(MySQL 연동)
* **데이터 최적화**: `Enum(MessageType)`을 도입하여 `ENTER`, `TALK`, `LEAVE` 타입을 명확히 구분 및 처리
* **반응형 UI**: 
  * 발신 주체별 말풍선 레이아웃 차별화 (본인: 우측 / 타인: 좌측 / 시스템: 중앙)
  * 신규 메시지 수신 시 하단 자동 스크롤(Auto-scroll) 기능 탑재

---

## 🚀 Troubleshooting & Learnings

### 1️⃣ Enum 도입을 통한 메시지 정합성 해결
> **Issue**: 퇴장 메시지가 시스템 공지로 처리되지 않고 '익명'의 일반 메시지로 렌더링되는 버그 발생

* **원인**: DTO 내부의 수동 Setter 중복 정의와 데이터 타입(String ↔ Enum) 불일치로 인해 서버가 전송한 `LEAVE` 타입 데이터가 유실됨을 발견했습니다.
* **해결**: `MessageType`을 **Enum**으로 정의하고 불필요한 메서드를 제거하여 타입 안정성을 확보했습니다. 서버 컨트롤러에서 Enum 상수를 직접 호출함으로써 데이터 신뢰성을 100% 확보하고 의도한 UI 렌더링을 구현했습니다.

### 2️⃣ 무상태성 프로토콜에서의 상태 유지 전략
* **이슈**: WebSocket은 연결 시점 이후 세션 유지가 까다로운 특성을 가집니다.
* **해결**: 연결 초기 단계(Handshake)에서 **HttpSession** 정보를 추출하여 STOMP 페이로드에 결합했습니다. 이를 통해 클라이언트가 매번 정보를 재입력하지 않아도 로그인 상태를 안정적으로 유지하도록 설계했습니다.

---

## 📂 Project Structure
```text
src/main/java/org/hyeok/chat
 ├── controller   # 채팅방 비즈니스 제어 및 세션 관리
 ├── dto          # ChatMessageDto (Enum 포함 데이터 구조)
 ├── entity       # User, ChatRoom (JPA-MySQL 엔티티 매핑)
 └── service      # 채팅방 참여 인원 및 데이터 처리 로직




```

## 🎬 Demo

-로그인




<img width="310" height="248" alt="image" src="https://github.com/user-attachments/assets/1a233daa-33a2-4941-ad3a-2298ef936f85" />
<img width="206" height="178" alt="image" src="https://github.com/user-attachments/assets/1ce90bcc-ff33-4656-ad7e-a0ecb92a7585" />



-로비화면



<img width="468" height="238" alt="image" src="https://github.com/user-attachments/assets/540a35f2-2cdd-4f92-8862-21c8b9ef1c90" />
<img width="459" height="184" alt="image" src="https://github.com/user-attachments/assets/59045de3-8e3b-4f1a-9226-47c34b126d93" />
<img width="462" height="256" alt="image" src="https://github.com/user-attachments/assets/40e50a80-f366-4ee3-83b0-3d863fe8c824" />
<img width="430" height="241" alt="image" src="https://github.com/user-attachments/assets/58899de5-449d-4216-8345-af2d74955231" />
<img width="415" height="233" alt="image" src="https://github.com/user-attachments/assets/dd37f83b-262c-46c1-b2d6-7999f700ec0e" />


-채팅방



<img width="303" height="354" alt="image" src="https://github.com/user-attachments/assets/b22b485d-9c3d-423a-8fe0-481bc69ec71a" />
<img width="443" height="207" alt="image" src="https://github.com/user-attachments/assets/29977128-271d-4463-8263-28cc3dd609e7" />
<img width="440" height="350" alt="image" src="https://github.com/user-attachments/assets/d1c09a16-7e24-4a15-a5da-6cc6702e151d" />
<img width="460" height="355" alt="image" src="https://github.com/user-attachments/assets/72053fbf-c38f-4f3d-b794-54d31a47e2a8" />




=방 삭제



<img width="515" height="281" alt="image" src="https://github.com/user-attachments/assets/1fcb29ac-aac0-4def-b462-f1862de14173" />
<img width="627" height="276" alt="image" src="https://github.com/user-attachments/assets/add59f84-d550-414c-babc-2405b26a2807" />





-암호화



<img width="299" height="237" alt="image" src="https://github.com/user-attachments/assets/a2692add-7931-40ec-bcd1-69cfadf55b5e" />











