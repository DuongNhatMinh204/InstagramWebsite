
// Biến toàn cục để lưu trữ kết nối STOMP và trạng thái chat
let stompClient = null;
let currentChatUserId = null;

// DOM Elements
const modal = document.getElementById("chatModal");
const chatList = document.getElementById("chatList");
const chatDetailModal = document.getElementById("chatDetailModal");
const closeDetail = document.querySelector(".closeDetail");
const chatMessages = document.getElementById("chatMessages");
const chatWithName = document.getElementById("chatWithName");
const chatHeaderAvatar = document.getElementById("chatHeaderAvatar");
const chatInput = document.getElementById("chatInput");
const sendChatBtn = document.getElementById("sendChatBtn");

// Hàm kết nối WebSocket
function connectWebSocket() {
    const userId = getCurrentUserId();
    if (!userId) {
        console.error("Không thể lấy userId để kết nối WebSocket.");
        return;
    }

    const socket = new SockJS("http://localhost:8080/ws");
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        console.log("Đã kết nối WebSocket: " + frame);

        // Đăng ký lắng nghe tin nhắn từ /user/{userId}/queue/messages
        stompClient.subscribe(`/user/${userId}/queue/messages`, function (message) {
            const savedMessage = JSON.parse(message.body);
            console.log("Tin nhắn nhận được:", savedMessage);

            // Chỉ hiển thị tin nhắn nếu nó đến từ người dùng đang chat
            if (savedMessage.senderId === currentChatUserId) {
                const messageEl = document.createElement("div");
                messageEl.className = "message-received";

                const avatarEl = document.createElement("img");
                avatarEl.src = chatHeaderAvatar.src || '/images/logo.jpg';
                avatarEl.className = "message-avatar";
                avatarEl.style.width = "25px";
                avatarEl.style.height = "25px";
                avatarEl.style.borderRadius = "50%";
                avatarEl.style.marginRight = "10px";
                messageEl.prepend(avatarEl);

                const textEl = document.createElement("span");
                textEl.textContent = savedMessage.content;
                messageEl.appendChild(textEl);

                chatMessages.appendChild(messageEl);
                chatMessages.scrollTop = chatMessages.scrollHeight;
            }
        });
    }, function (error) {
        console.error("Lỗi kết nối WebSocket:", error);
    });
}

// Hàm ngắt kết nối WebSocket
function disconnectWebSocket() {
    if (stompClient !== null) {
        stompClient.disconnect();
        console.log("Đã ngắt kết nối WebSocket.");
        stompClient = null;
    }
}

// Lấy lịch sử tin nhắn (được gọi từ home.html)
window.fetchChatHistory = function() {
    fetch("/v1/user/chat/history-people", {
        method: "GET",
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("token")
        }
    })
        .then(response => response.json())
        .then(data => {
            chatList.innerHTML = "";
            if (data.data && data.data.length > 0) {
                data.data.forEach(user => {
                    const item = document.createElement("div");
                    item.className = "chat-item";
                    item.innerHTML = `
                    <img src="${user.avatar_url || '/images/logo.jpg'}" class="avatar">
                    <div>
                        <strong>${user.username}</strong><br>
                        <small>Đã nhắn với bạn</small>
                    </div>
                `;
                    item.onclick = () => openChatWithUser(user.id, user.username, user.avatar_url);
                    chatList.appendChild(item);
                });
            } else {
                chatList.innerHTML = "<p>Không có tin nhắn nào.</p>";
            }
            connectWebSocket(); // Kết nối WebSocket khi tải danh sách
        })
        .catch(err => {
            chatList.innerHTML = "<p>Lỗi khi tải dữ liệu.</p>";
            console.error("Lỗi API:", err);
        });
};

// Mở cuộc trò chuyện với người dùng
function openChatWithUser(userId, username, avatarUrl) {
    chatModal.style.display = "none"; // Ẩn modal danh sách
    chatDetailModal.style.display = "flex"; // Hiển thị modal chi tiết
    chatWithName.textContent = username;
    currentChatUserId = userId;

    chatHeaderAvatar.src = avatarUrl || '/images/logo.jpg';

    // Lấy lịch sử tin nhắn
    fetch(`/v1/user/chat/history?user2Id=${userId}`, {
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("token")
        }
    })
        .then(response => response.json())
        .then(data => {
            chatMessages.innerHTML = "";
            data.forEach(msg => {
                const messageEl = document.createElement("div");
                messageEl.className = msg.senderId == getCurrentUserId() ? "message-sent" : "message-received";

                if (msg.senderId !== getCurrentUserId()) {
                    const avatarEl = document.createElement("img");
                    avatarEl.src = avatarUrl || '/images/logo.jpg';
                    avatarEl.className = "message-avatar";
                    avatarEl.style.width = "25px";
                    avatarEl.style.height = "25px";
                    avatarEl.style.borderRadius = "50%";
                    avatarEl.style.marginRight = "10px";
                    messageEl.prepend(avatarEl);
                }

                const textEl = document.createElement("span");
                textEl.textContent = msg.content;
                messageEl.appendChild(textEl);

                chatMessages.appendChild(messageEl);
            });
            chatMessages.scrollTop = chatMessages.scrollHeight;
        });

    // Xử lý sự kiện gửi tin nhắn
    if (sendChatBtn) {
        sendChatBtn.onclick = () => {
            const content = chatInput.value.trim();
            if (!content) return;

            const messageData = {
                senderId: getCurrentUserId(),
                receiverId: userId,
                content: content
            };

            fetch("/v1/user/chat/send", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": "Bearer " + localStorage.getItem("token")
                },
                body: JSON.stringify(messageData)
            })
                .then(response => response.json())
                .then(savedMessage => {
                    const messageEl = document.createElement("div");
                    messageEl.className = "message-sent";
                    const textEl = document.createElement("span");
                    textEl.textContent = savedMessage.content;
                    messageEl.appendChild(textEl);
                    chatMessages.appendChild(messageEl);
                    chatMessages.scrollTop = chatMessages.scrollHeight;
                    chatInput.value = "";
                })
                .catch(err => {
                    console.error("Lỗi khi gửi tin nhắn:", err);
                    alert("Không thể gửi tin nhắn. Vui lòng thử lại!");
                });
        };
    }
}

// Đóng modal chi tiết và quay lại danh sách
if (closeDetail) {
    closeDetail.onclick = () => {
        chatDetailModal.style.display = "none";
        chatModal.style.display = "flex"; // Hiển thị lại modal danh sách
        currentChatUserId = null; // Xóa ID người dùng đang chat
    };
}

// Lưu lại userId của người đang chat
window.setCurrentChatUserId = function(userId) {
    currentChatUserId = Number(userId);
};

// Hàm load lịch sử chat với userId
window.loadChatHistory = function(userId, avatarUrl) {
    chatMessages.innerHTML = '<div style="color:#888;">Đang tải...</div>';
    fetch(`/v1/user/chat/history?user2Id=${userId}`, {
        headers: { "Authorization": "Bearer " + localStorage.getItem("token") }
    })
        .then(res => res.json())
        .then(data => {
            chatMessages.innerHTML = '';
            const myId = getCurrentUserId();
            const messages = Array.isArray(data) ? data : [];
            if (!messages.length) {
                chatMessages.innerHTML = '<div style="color:#888;">Chưa có tin nhắn nào.</div>';
                return;
            }
            messages.forEach(msg => {
                const messageEl = document.createElement("div");
                if (msg.senderId == myId) {
                    messageEl.className = "message-sent";
                    messageEl.innerHTML = `<span>${msg.content}</span>`;
                } else {
                    messageEl.className = "message-received";
                    messageEl.innerHTML = `<img src="${avatarUrl}" class="message-avatar" style="width:25px;height:25px;border-radius:50%;margin-right:10px;"> <span>${msg.content}</span>`;
                }
                chatMessages.appendChild(messageEl);
            });
            chatMessages.scrollTop = chatMessages.scrollHeight;
        })
        .catch(err => {
            chatMessages.innerHTML = '<div style="color:#888;">Không thể tải tin nhắn.</div>';
            console.error('Lỗi loadChatHistory:', err);
        });
};

// Gửi tin nhắn
if (sendChatBtn && chatInput) {
    sendChatBtn.onclick = function() {
        sendMessage();
    };
    chatInput.onkeydown = function(e) {
        if (e.key === "Enter") {
            e.preventDefault();
            sendMessage();
        }
    };
}

function sendMessage() {
    const content = chatInput.value.trim();
    if (!content || !currentChatUserId) return;
    const myId = getCurrentUserId();
    const msg = { senderId: myId, receiverId: currentChatUserId, content };
    fetch("/v1/user/chat/send", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + localStorage.getItem("token")
        },
        body: JSON.stringify(msg)
    })
        .then(res => res.json())
        .then(savedMessage => {
            const messageEl = document.createElement("div");
            messageEl.className = "message-sent";
            messageEl.innerHTML = `<span>${savedMessage.content}</span>`;
            chatMessages.appendChild(messageEl);
            chatMessages.scrollTop = chatMessages.scrollHeight;
            chatInput.value = "";
        })
        .catch(err => {
            alert("Không thể gửi tin nhắn. Vui lòng thử lại!");
            console.error("Lỗi gửi tin nhắn:", err);
        });
}

// Lấy ID người dùng hiện tại
function getCurrentUserId() {
    return parseInt(localStorage.getItem("userId"));
}


