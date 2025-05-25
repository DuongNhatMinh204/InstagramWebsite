document.getElementById("postForm").addEventListener("submit", async function (e) {
    e.preventDefault();
    const content = document.getElementById("content").value;
    const fileInput = document.getElementById("imgFile");
    let imageUrl = "";

    if (fileInput.files.length > 0) {
        const formData = new FormData();
        formData.append("file", fileInput.files[0]);

        const uploadRes = await fetch("http://localhost:8080/api/images/upload", {
            method: "POST",
            body: formData
        });

        imageUrl = await uploadRes.text();
    }

    const res = await fetch("/v1/user/post/create", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + localStorage.getItem("token")
        },
        body: JSON.stringify({ content, img_url: imageUrl })
    });

    const result = await res.json();
    alert(result.message);
    document.getElementById("postForm").reset();
    loadPosts();
});

async function loadPosts() {
    const res = await fetch("/v1/user/post/getpost", {
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("token")
        }
    });

    const data = await res.json();
    const posts = data.data;
    const container = document.getElementById("postsContainer");
    container.innerHTML = "";

    for (const post of posts) {
        // Gọi API check-liked để biết user hiện tại đã like post này chưa
        const checkLikeRes = await fetch(`/v1/user/check-liked?postId=${post.id}`, {
            headers: {
                "Authorization": "Bearer " + localStorage.getItem("token")
            }
        });
        const checkLikeData = await checkLikeRes.json();
        const isLiked = checkLikeData.data;
        const heartIcon = isLiked ? "❤️" : "🖤";

        const postEl = document.createElement("div");
        postEl.className = "post-card";

        postEl.innerHTML = `
            <div class="post-header">
                <img src="${post.url_avatar || 'default-avatar.png'}" class="post-avatar" />
                <strong>${post.nickname}</strong>
            </div>
            <div class="post-content">
                <p>${post.content}</p>
                ${post.imageUrl ? `<img src="${post.imageUrl}" class="post-image" />` : ""}
            </div>
            <div class="post-actions">
                <button onclick="toggleLike(${post.id}, ${isLiked})" id="like-btn-${post.id}">
                    ${heartIcon} Thích (${post.totalLikes})
                </button>
            </div>
            <div class="post-stats">
                <input type="text" placeholder="Viết bình luận..." id="comment-input-${post.id}" style="width: 80%;" />
                <button onclick="addComment(${post.id})">Gửi</button>
            </div>
            <div class="comments">
                ${post.comments.map(c => `
                    <div class="comment"><strong>${c.nickName}</strong>: ${c.content}</div>
                `).join("")}
            </div>
        `;

        container.appendChild(postEl);
    }
}


async function toggleLike(postId, isLiked) {
    const url = isLiked ? `/v1/user/unlike?postId=${postId}` : `/v1/user/likepost?postId=${postId}`;
    const method = isLiked ? "DELETE" : "POST";

    // Gửi yêu cầu like hoặc unlike
    await fetch(url, {
        method: method,
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("token")
        }
    });

    // Gọi API lấy lại số lượt like mới nhất
    const countRes = await fetch(`/v1/user/count-liked?postId=${postId}`, {
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("token")
        }
    });

    const countData = await countRes.json();
    const likeCount = countData.data;

    // Cập nhật giao diện
    const btn = document.getElementById(`like-btn-${postId}`);
    const newIsLiked = !isLiked;
    const icon = newIsLiked ? "❤️" : "🖤";
    btn.innerText = `${icon} Thích (${likeCount})`;
    btn.setAttribute("onclick", `toggleLike(${postId}, ${newIsLiked})`);
}


async function addComment(postId) {
    const input = document.getElementById(`comment-input-${postId}`);
    const content = input.value.trim();
    if (!content) return;

    const res = await fetch(`/v1/user/cmt/add?postId=${postId}`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + localStorage.getItem("token")
        },
        body: JSON.stringify({ content })
    });

    const result = await res.json();
    alert(result.data);
    input.value = "";
    loadPosts(); // Reload để hiển thị bình luận mới
}

loadPosts();
