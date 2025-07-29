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
    if (!res.ok) throw new Error("Không thể lấy danh sách bài đăng");
    const data = await res.json();
    let posts = data.data;
    posts = posts.sort((a, b) => new Date(b.created_at || b.createdAt) - new Date(a.created_at || a.createdAt));
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

        // Tính thời gian đăng
        let timeAgo = "";
        if (post.created_at || post.createdAt) {
            const created = new Date(post.created_at || post.createdAt);
            const now = new Date();
            const diffMs = now - created;
            const diffMins = Math.floor(diffMs / 60000);
            const diffHours = Math.floor(diffMins / 60);
            const diffDays = Math.floor(diffHours / 24);
            if (diffMins < 1) timeAgo = "Vừa xong";
            else if (diffMins < 60) timeAgo = `${diffMins}p`;
            else if (diffHours < 24) timeAgo = `${diffHours}h`;
            else timeAgo = `${diffDays}d`;
        }

        const postEl = document.createElement("div");
        postEl.className = "post-card";

        postEl.innerHTML = `
            <div class="post-header">
                <img src="${post.url_avatar || 'http://localhost:8080/images/default-avatar.png'}" class="post-avatar" />
                <div style="display:flex;flex-direction:column;">
<!--                  <strong>${post.nickname}</strong>-->
                 <a href="" class="nickname-link" data-user-id="${post.userId}" style="cursor: pointer; text-decoration: none; color: inherit;">
                        <strong>${post.nickname}</strong>
                    </a>
                  <span style="font-size:12px;color:#888;">${timeAgo}</span>
                </div>
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
    const nicknameLinks = document.querySelectorAll('.nickname-link');
    console.log('Found nickname links:', nicknameLinks.length);
    nicknameLinks.forEach(link => {
        console.log('Attaching click event to:', link);
        link.addEventListener('click', (e) => {
            e.preventDefault();
            const userId = link.getAttribute('data-user-id');
            console.log('Clicked nickname with userId:', userId);
            if (typeof window.viewUserProfile === 'function') {
                window.viewUserProfile(userId);
            } else {
                console.error('window.viewUserProfile is not a function');
            }
        });
    });
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
