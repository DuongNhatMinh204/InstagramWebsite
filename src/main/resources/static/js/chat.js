const modal = document.getElementById("chatModal");
const btn = document.getElementById("openModalBtn");
const closeBtn = document.querySelector(".close");
const chatList = document.getElementById("chatList");

btn.onclick = () => {
    modal.style.display = "block";
    fetchChatHistory();
};

closeBtn.onclick = () => modal.style.display = "none";
window.onclick = function(event) {
    if (event.target == modal) modal.style.display = "none";
};

function fetchChatHistory() {
    fetch("http://localhost:8080/v1/user/chat/history-people",
        {
            method : "GET" ,
            headers: {
                "Authorization": "Bearer " + localStorage.getItem("token")
            }
        })
        .then(response => response.json())
        .then(data => {
            chatList.innerHTML = ""; // Xóa dữ liệu cũ
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
                chatList.appendChild(item);
            });
        })
        .catch(err => {
            chatList.innerHTML = "<p>Lỗi khi tải dữ liệu.</p>";
            console.error("Lỗi API:", err);
        });
}
