💬 Real-time Chatting Service with Spring Boot & STOMP
-Spring Boot와 WebSocket(STOMP)을 활용한 사용자 친화적 실시간 채팅 플랫폼입니다. > 사용자 인증(Session)과 실시간 통신을 결합하고, Enum 기반의 데이터 구조로 유지보수성을 높인 프로젝트입니다.

🛠 Tech Stack
-Backend: Java 17, Spring Boot 3.x, Spring Data JPA

-Database: MySQL 8.0

-Real-time: WebSocket, STOMP, SockJS

-Frontend: JavaScript, Thymeleaf, CSS3

🗂 System Architecture
-User Authentication: HttpSession을 통해 사용자 정보를 관리하며, 채팅 참여 시 세션의 정보를 기반으로 사용자를 식별합니다.

-Message Broker: STOMP 프로토콜의 Pub/Sub 모델을 채택하여 서버 부하를 최소화하고 실시간 메시지 브로드캐스팅을 구현했습니다.

-Data Persistence: 채팅방의 상태(제목, 인원수, 비밀번호)를 MySQL에 영속화하여 안정적인 서비스 운영이 가능하도록 설계했습니다.

✨ Key Features
[회원가입/로그인 - Auth Service]
-회원가입: 닉네임 중복 체크 및 이메일 인증을 통한 본인 확인 프로세스

-세션 인증: 로그인 시 User 객체를 세션에 저장하여 채팅 발신자(sender) 정보로 자동 매핑

-보안: 비밀번호 암호화 저장 및 미인증 사용자의 채팅방 접근 차단(Redirect 처리)

[채팅 서비스 - Chat Service]
-실시간 방 관리: 채팅방 생성, 비밀번호 기반 비공개 방 입장 및 실시간 참여 인원 증감 로직(MySQL 연동)

-메시지 타입 최적화: Enum(MessageType)을 도입하여 ENTER, TALK, LEAVE 타입을 명확히 구분

-반응형 UI: 본인(우측), 상대방(좌측), 시스템 메시지(중앙) 레이아웃 차별화

-신규 메시지 수신 시 하단 자동 스크롤(Auto-scroll) 기능

🚀 Troubleshooting & Learnings
1. Enum 도입을 통한 메시지 정합성 해결
문제: 퇴장 메시지가 시스템 공지로 처리되지 않고 '익명'의 일반 메시지로 렌더링되는 이슈 발생.

원인: DTO 내부의 수동 Setter 중복과 데이터 타입(String vs Enum) 불일치로 인해 서버에서 보낸 LEAVE 타입이 유실됨.

해결: MessageType을 Enum으로 정의하고 중복 메서드를 제거하여 타입 안정성을 확보. 서버 컨트롤러에서 Enum 상수를 직접 호출함으로써 데이터 신뢰성을 100% 확보함.

2. 무상태성 프로토콜에서의 상태 유지
해결: WebSocket은 연결 시점 이후 세션 유지가 까다롭지만, 연결 초기 단계에서 HttpSession 정보를 추출하여 STOMP 페이로드에 결합함으로써 클라이언트가 별도로 이름을 입력하지 않아도 로그인 정보를 유지하도록 구현.
