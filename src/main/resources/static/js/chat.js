// Biến toàn cục để lưu trữ kết nối STOMP và trạng thái chat
let stompClient = null;
let currentChatUserId = null; // Lưu ID của người dùng đang chat

// DOM Elements
const modal = document.getElementById("chatModal");
const btn = document.getElementById("openModalBtn");
const closeBtn = document.querySelector(".close");
const chatList = document.getElementById("chatList");
const chatDetailModal = document.getElementById("chatDetailModal");
const closeDetail = document.querySelector(".closeDetail");
const chatMessages = document.getElementById("chatMessages");
const chatWithName = document.getElementById("chatWithName");

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

                // Thêm avatar của đối phương
                const avatarEl = document.createElement("img");
                avatarEl.src = document.querySelector(".chat-header-avatar").src || 'default-avatar.png';
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

// Mở modal danh sách tin nhắn
btn.onclick = () => {
    modal.style.display = "block";
    fetchChatHistory();
    connectWebSocket(); // Kết nối WebSocket khi mở modal
};

// Đóng modal danh sách tin nhắn
closeBtn.onclick = () => {
    modal.style.display = "none";
    disconnectWebSocket();
};

window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none";
        disconnectWebSocket();
    }
};

// Đóng modal chi tiết cuộc trò chuyện
closeDetail.onclick = () => {
    chatDetailModal.style.display = "none";
    currentChatUserId = null; // Xóa ID người dùng đang chat
};

// Lấy lịch sử tin nhắn
function fetchChatHistory() {
    fetch("/v1/user/chat/history-people", {
        method: "GET",
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("token")
        }
    })
        .then(response => response.json())
        .then(data => {
            chatList.innerHTML = "";
            data.data.forEach(user => {
                const item = document.createElement("div");
                item.className = "chat-item";
                item.innerHTML = `
                <img src="${user.avatar_url}" class="avatar">
                <div>
                    <strong>${user.username}</strong><br>
                    <small>Đã nhắn với bạn</small>
                </div>
            `;
                item.onclick = () => openChatWithUser(user.id, user.username, user.avatar_url);
                chatList.appendChild(item);
            });
        })
        .catch(err => {
            chatList.innerHTML = "<p>Lỗi khi tải dữ liệu.</p>";
            console.error("Lỗi API:", err);
        });
}

// Mở cuộc trò chuyện với người dùng
function openChatWithUser(userId, username, avatarUrl) {
    chatDetailModal.style.display = "block";
    chatWithName.textContent = username;
    currentChatUserId = userId; // Lưu ID người dùng đang chat

    // Xóa avatar cũ nếu có
    const existingAvatar = document.querySelector(".chat-header-avatar");
    if (existingAvatar) existingAvatar.remove();

    // Thêm avatar mới
    const chatWithAvatar = document.createElement("img");
    chatWithAvatar.src = avatarUrl || 'default-avatar.png';
    chatWithAvatar.className = "chat-header-avatar";
    chatWithAvatar.style.width = "30px";
    chatWithAvatar.style.height = "30px";
    chatWithAvatar.style.borderRadius = "50%";
    chatWithAvatar.style.marginRight = "10px";

    const header = chatWithName.parentElement;
    header.prepend(chatWithAvatar);

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
                    avatarEl.src = avatarUrl || 'default-avatar.png';
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
    const sendChatBtn = document.getElementById("sendChatBtn");
    const chatInput = document.getElementById("chatInput");

    const newSendChatBtn = sendChatBtn.cloneNode(true);
    sendChatBtn.parentNode.replaceChild(newSendChatBtn, sendChatBtn);

    newSendChatBtn.onclick = () => {
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

// Lấy ID người dùng hiện tại
function getCurrentUserId() {
    return parseInt(localStorage.getItem("userId"));
}