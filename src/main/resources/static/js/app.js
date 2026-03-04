var stompClient = null;
var roomId = document.getElementById('roomId').value;
var sender = document.getElementById('sender').value;

function connect() {
    var socket = new SockJS('/ws-chat');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/sub/chat/room/' + roomId, function (chat) {
            showGreeting(JSON.parse(chat.body));
        });
        sendEnterMessage();
    });
}

function sendEnterMessage() {
    stompClient.send("/pub/chat/message", {}, JSON.stringify({
        type: 'ENTER',
        roomId: roomId,
        sender: sender
    }));
}

function sendMessage() {
    var messageContent = document.getElementById('message').value;
    if (messageContent && stompClient) {
        stompClient.send("/pub/chat/message", {}, JSON.stringify({
            type: 'TALK',
            roomId: roomId,
            sender: sender,
            message: messageContent
        }));
        document.getElementById('message').value = '';
    }
}

function showGreeting(message) {
    const chatArea = document.getElementById('chat-content');
    const myName = document.getElementById('sender').value.trim();
    const type = (message.type || "").trim().toUpperCase();

    // 1. 시스템 메시지 (입장/퇴장) 최우선 처리
    if (type === 'ENTER' || type === 'LEAVE') {
        const sysDiv = document.createElement('div');
        sysDiv.className = 'system-msg'; 
        sysDiv.innerText = message.message;
        chatArea.appendChild(sysDiv);
        chatArea.scrollTop = chatArea.scrollHeight;
        return; // 아래 말풍선 로직을 실행하지 않고 종료
    } 

    // 2. 일반 채팅 메시지 로직
    const senderName = (message.sender || "익명").trim();
    const isMine = (senderName === myName);
    
    const now = new Date();
    const timeStr = (now.getHours() >= 12 ? "오후 " : "오전 ") + 
                  (now.getHours() % 12 || 12) + ":" + 
                  now.getMinutes().toString().padStart(2, '0');

    const row = document.createElement('div');
    row.style.display = "flex";
    row.style.flexDirection = "column";
    row.style.alignItems = isMine ? "flex-end" : "flex-start";
    row.style.width = "100%";
    row.style.marginBottom = "10px";

    if (!isMine) {
        const nameSpan = document.createElement('span');
        nameSpan.className = 'sender-name';
        nameSpan.innerText = senderName;
        row.appendChild(nameSpan);
    }

    const contentBox = document.createElement('div');
    contentBox.style.display = "flex";
    contentBox.style.alignItems = "flex-end"; 

    const msgDiv = document.createElement('div');
    msgDiv.className = isMine ? 'msg-box my-msg' : 'msg-box other-msg';
    msgDiv.innerText = message.message;

    const timeSpan = document.createElement('span');
    timeSpan.className = 'chat-time';
    timeSpan.innerText = timeStr;

    if (isMine) {
        contentBox.appendChild(timeSpan);
        contentBox.appendChild(msgDiv);
    } else {
        contentBox.appendChild(msgDiv);
        contentBox.appendChild(timeSpan);
    }

    row.appendChild(contentBox);
    chatArea.appendChild(row);
    chatArea.scrollTop = chatArea.scrollHeight;
}

document.addEventListener("DOMContentLoaded", function() {
    connect();
});