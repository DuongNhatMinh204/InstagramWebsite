// Function to open chat modal with selected person
function openChatModalWith(nickName, avatarUrl, userId) {
    document.getElementById('chatModal').style.display = 'none';
    document.getElementById('chatDetailModal').style.display = 'flex'; // Use flex for centering
    document.getElementById('chatWithName').innerText = nickName;
    document.getElementById('chatHeaderAvatar').src = avatarUrl || '/images/logo.jpg';
    if (window.loadChatHistory) window.loadChatHistory(userId, avatarUrl);
    if (window.setCurrentChatUserId) window.setCurrentChatUserId(userId);
}

// Global function for profile-related modal interaction (if needed elsewhere)
window.openChatModalWith = openChatModalWith;

/**
 * Lấy tất cả URL hình ảnh từ các bài viết của người dùng hiện đang đăng nhập hoặc người dùng được chỉ định.
 * @param {string} targetUserId - ID của người dùng muốn lấy ảnh.
 * @returns {Promise<Array<string>>} Một Promise giải quyết với một mảng các URL hình ảnh.
 * Trả về một mảng rỗng nếu không có token, lỗi xảy ra, hoặc không có ảnh.
 */
async function getUserImageUrls(targetUserId) {
    const token = localStorage.getItem("token");

    // if (!token) {
    //     console.warn("Không tìm thấy token. Vui lòng đăng nhập.");
    //     return [];
    // }

    if (!targetUserId) {
        console.error("Không tìm thấy User ID. Không thể lấy ảnh.");
        return [];
    }

    try {
        const response = await fetch(`/v1/user/post/post-list/${targetUserId}?userId=${targetUserId}`, {
            headers: {
                "Authorization": "Bearer " + token
            }
        });

        if (!response.ok) {
            if (response.status === 401 || response.status === 403) {
                console.error("Phiên đăng nhập hết hạn hoặc không có quyền. Vui lòng đăng nhập lại.");
                localStorage.removeItem("token");
                // Tùy chọn: Chuyển hướng về trang đăng nhập
                // window.location.href = "/login";
            } else {
                console.error(`Lỗi khi lấy bài viết: ${response.status} ${response.statusText}`);
            }
            return [];
        }

        const data = await response.json();
        const posts = data.data;

        const imageUrls = posts
            .filter(post => post.imageUrl) // Sử dụng post.imageUrl thay vì post.img_url
            .map(post => post.imageUrl);

        return imageUrls;

    } catch (error) {
        console.error("Lỗi mạng hoặc lỗi khác khi lấy URL ảnh:", error);
        return [];
    }
}

document.addEventListener('DOMContentLoaded', function() {
    const token = localStorage.getItem("token");
    const urlParams = new URLSearchParams(window.location.search);
    const targetUserId = urlParams.get('userId') || localStorage.getItem("userId"); // Ưu tiên userId từ URL, nếu không có thì lấy của người dùng hiện tại

    // Redirect to login if no token
    if (!token) {
        alert("Vui lòng đăng nhập.");
        window.location.href = "/login";
        return; // Stop execution if not logged in
    }

    // Fetch user info for the target user
    if (targetUserId) {
        fetch(`/v1/auth/user/${targetUserId}`, { // Sử dụng API lấy user theo ID
            // headers: { "Authorization": "Bearer " + token }
        })
            .then(res => {
                if (!res.ok) {
                    if (res.status === 401 || res.status === 403) {
                        // Token invalid or expired
                        localStorage.removeItem('token');
                        localStorage.removeItem('userId');
                        alert('Phiên làm việc đã hết hạn. Vui lòng đăng nhập lại.');
                        window.location.href = '/login';
                    }
                    throw new Error('Failed to fetch user info');
                }
                return res.json();
            })
            .then(response => {
                const user = response; // Dữ liệu người dùng nằm trong data
                const profileAvatar = document.getElementById("profile-avatar");
                if (profileAvatar) profileAvatar.src = user.avatarUrl || "http://localhost:8080/images/default-avatar.png";

                const nickname = document.getElementById("nickname");
                if (nickname) nickname.innerText = user.nickName || "Không tên";

                const fullName = document.getElementById("fullNameDisplay");
                if (fullName) fullName.innerText = user.fullName || "";

                const workplace = document.getElementById("workplaceDisplay");
                if (workplace) workplace.innerText = user.workplace || "";

                const education = document.getElementById("educationDisplay");
                if (education) education.innerText = user.education || "";

                const address = document.getElementById("addressDisplay");
                if (address) address.innerText = user.address || "";

                const maritalStatus = document.getElementById("maritalStatusDisplay");
                if (maritalStatus) maritalStatus.innerText = user.maritalStatus || "";

                const birthday = document.getElementById("birthdayDisplay");
                if (birthday) birthday.innerText = user.birthday || "";

                // Logic để ẩn/hiện nút chỉnh sửa profile
                const editProfileButton = document.getElementById("editProfileButton");
                const editProfileButtonBottom = document.getElementById("editProfileButtonBottom"); // Nút ở dưới phần giới thiệu

                if (String(targetUserId) === String(localStorage.getItem("userId"))) {
                    if (editProfileButton) editProfileButton.style.display = 'block'; // Hiển thị nút chỉnh sửa
                    if (editProfileButtonBottom) editProfileButtonBottom.style.display = 'block';
                } else {
                    if (editProfileButton) editProfileButton.style.display = 'none'; // Ẩn nút chỉnh sửa
                    if (editProfileButtonBottom) editProfileButtonBottom.style.display = 'none';
                }


                getUserImageUrls(targetUserId).then(imageUrls => {
                    console.log("Tất cả URL ảnh của người dùng:", imageUrls);

                    const randomPhotosGrid = document.getElementById("randomPhotosGrid");
                    if (randomPhotosGrid) {
                        if (imageUrls.length === 0) {
                            randomPhotosGrid.innerHTML = '<p class="text-gray-500 col-span-3 text-center">Chưa có ảnh nào để hiển thị.</p>';
                            return;
                        }

                        // Hàm để xáo trộn mảng (Fisher-Yates shuffle)
                        function shuffleArray(array) {
                            for (let i = array.length - 1; i > 0; i--) {
                                const j = Math.floor(Math.random() * (i + 1));
                                [array[i], array[j]] = [array[j], array[i]]; // Hoán đổi vị trí
                            }
                        }

                        shuffleArray(imageUrls); // Xáo trộn mảng URL ảnh

                        // Chọn tối đa 6 ảnh để hiển thị (hoặc ít hơn nếu không đủ)
                        const photosToDisplay = imageUrls.slice(0, 6);

                        randomPhotosGrid.innerHTML = photosToDisplay.map(url => `
                            <img src="${url}" alt="Ảnh người dùng" class="w-full aspect-square object-cover rounded-md bg-gray-300">
                        `).join('');
                    }

                }).catch(error => {
                    console.error("Lỗi khi lấy URL ảnh của người dùng để hiển thị ngẫu nhiên:", error);
                    const randomPhotosGrid = document.getElementById("randomPhotosGrid");
                    if (randomPhotosGrid) {
                        randomPhotosGrid.innerHTML = '<p class="text-red-500 col-span-3 text-center">Không thể tải ảnh.</p>';
                    }
                });

            })
            .catch(error => {
                console.error("Error fetching user info:", error);
                alert("Không thể tải thông tin người dùng.");
            });

        // Fetch and render user posts for the target user
        fetch(`/v1/user/post/post-list/${targetUserId}?userId=${targetUserId}`, { // Sử dụng API lấy bài viết theo userId
            headers: { "Authorization": "Bearer " + token }
        })
            .then(res => {
                if (!res.ok) {
                    if (res.status === 401 || res.status === 403) {
                        localStorage.removeItem('token');
                        localStorage.removeItem('userId');
                        alert('Phiên làm việc đã hết hạn. Vui lòng đăng nhập lại.');
                        window.location.href = '/login';
                    }
                    throw new Error('Failed to fetch posts');
                }
                return res.json();
            })
            .then(response => {
                const posts = response.data;
                const postsContainer = document.getElementById("postsContainer");
                if (!postsContainer) return; // Ensure container exists

                if (posts.length === 0) {
                    postsContainer.innerHTML = '<div class="bg-white rounded-lg shadow-md p-4 text-center text-gray-500">Chưa có bài viết nào.</div>';
                    return;
                }
                postsContainer.innerHTML = posts.map(post => {
                    console.log("Post ID:", post.id, "User ID for post:", post.userId, "Nickname:", post.nickname); // LOGGING: Kiểm tra post.idUser
                    return `
                        <div class="bg-white rounded-lg shadow-md p-4 mb-4">
                            <div class="flex items-center mb-3">
                                <img src="${post.url_avatar || ''}" alt="Avatar" class="w-10 h-10 rounded-full mr-3 object-cover bg-gray-300">
                                <div class="flex-1">
                                   <a href="#" class="nickname-link" data-user-id="${post.userId}" style="cursor: pointer; text-decoration: none; color: inherit;">
                                    <strong>${post.nickname}</strong>
                                  </a>
<!--                                    <div class="font-semibold" style="cursor: pointer;" onclick="viewUserProfile('${post.userId}')">${post.nickname || 'Không tên'}</div>-->
                                    <div class="text-sm text-gray-500">2 giờ trước · <i class="fas fa-globe-americas"></i></div>
                                </div>
                                <i class="fas fa-ellipsis-h text-gray-500 cursor-pointer"></i>
                            </div>
                            <div class="mb-3 leading-relaxed">
                                ${post.content || ''}
                            </div>
                            ${post.imageUrl ? `<img src="${post.imageUrl}" alt="Post Image" class="w-full max-h-96 object-contain rounded-lg bg-gray-100 mb-3">` : ''}
                            <div class="flex justify-between border-b border-gray-200 pb-2 text-gray-600 text-sm">
                                <div><i class="fas fa-thumbs-up text-blue-600 mr-1"></i> ${post.totalLikes || 0}</div>
                                <div>${post.totalComments || 0} bình luận · 0 lượt chia sẻ</div>
                            </div>
                            <div class="flex justify-around py-2">
                                <div class="flex items-center p-2 rounded-md cursor-pointer text-gray-600 font-semibold hover:bg-gray-100">
                                    <i class="far fa-thumbs-up mr-2"></i>
                                    <span>Thích</span>
                                </div>
                                <div class="flex items-center p-2 rounded-md cursor-pointer text-gray-600 font-semibold hover:bg-gray-100">
                                    <i class="far fa-comment mr-2"></i>
                                    <span>Bình luận</span>
                                </div>
                                <div class="flex items-center p-2 rounded-md cursor-pointer text-gray-600 font-semibold hover:bg-gray-100">
                                    <i class="fas fa-share mr-2"></i>
                                    <span>Chia sẻ</span>
                                </div>
                            </div>
                            ${(post.comments && post.comments.length > 0) ? `
                            <div class="bg-gray-100 rounded-b-lg p-4 -mx-4 -mb-4 mt-2">
                                <div class="font-semibold mb-2 text-gray-600">${post.comments.length} bình luận</div>
                                ${post.comments.map(comment => `
                                    <div class="flex mb-3">
                                        <img src="${comment.avatarUrl || ''}" class="w-8 h-8 rounded-full mr-2" alt="">
                                        <div class="bg-gray-200 p-2 rounded-lg flex-1">
                                            <div class="font-semibold text-sm">${comment.nickName || 'Không tên'}</div>
                                            <div class="text-sm">${comment.content || ''}</div>
                                            <div class="flex mt-1">
                                                <span class="text-xs text-gray-500 mr-4">Thích</span>
                                                <span class="text-xs text-gray-500">Phản hồi</span>
                                            </div>
                                        </div>
                                    </div>
                                `).join('')}
                                <div class="flex mt-3">
                                    <img src="${document.getElementById('profile-avatar') ? document.getElementById('profile-avatar').src : 'http://localhost:8080/images/default-avatar.png'}" class="w-8 h-8 rounded-full mr-2">
                                    <input type="text" placeholder="Viết bình luận..." class="flex-1 bg-gray-200 border border-gray-300 rounded-full py-2 px-4 outline-none">
                                </div>
                            </div>
                            ` : ''}
                        </div>
                    `;
                }).join('');
            })
            .catch(error => {
                console.error("Error fetching posts:", error);
                const postsContainer = document.getElementById("postsContainer");
                if (postsContainer) postsContainer.innerHTML = '<div class="bg-white rounded-lg shadow-md p-4 text-center text-gray-500">Không thể tải bài viết.</div>';
            });

    } else {
        console.error("Không tìm thấy userId. Không thể tải trang profile.");
        // Có thể chuyển hướng về trang chủ hoặc hiển thị thông báo lỗi
    }


    // Populate the auth navigation button (logic này có thể giữ nguyên hoặc điều chỉnh tùy theo thiết kế của bạn)
    const authNavButtonContainer = document.getElementById('authNavButton');
    if (token) {
        authNavButtonContainer.innerHTML = `
            <button id="openProfileDropdown" class="text-lg">👤 Profile</button>
            <div id="profileDropdownMenu" class="absolute right-0 mt-2 w-60 bg-white border border-gray-200 rounded-md shadow-lg hidden z-10">
                <div class="px-4 py-2 hover:bg-gray-100 cursor-pointer" id="viewProfileBtnDropdown"><i class="fas fa-user-circle mr-2"></i> Xem tất cả trang cá nhân</div>
                <hr class="border-gray-200"/>
                <div class="px-4 py-2 hover:bg-gray-100 cursor-pointer"><i class="fas fa-cog mr-2"></i> Cài đặt & quyền riêng tư</div>
                <div class="px-4 py-2 hover:bg-gray-100 cursor-pointer"><i class="fas fa-question-circle mr-2"></i> Trợ giúp và hỗ trợ</div>
                <div class="px-4 py-2 hover:bg-gray-100 cursor-pointer"><i class="fas fa-moon mr-2"></i> Màn hình & trợ năng</div>
                <div class="px-4 py-2 hover:bg-gray-100 cursor-pointer"><i class="fas fa-comment-dots mr-2"></i> Đóng góp ý kiến</div>
                <hr class="border-gray-200"/>
                <div class="px-4 py-2 hover:bg-gray-100 cursor-pointer" id="logoutBtn"><i class="fas fa-sign-out-alt mr-2"></i> Đăng xuất</div>
            </div>
        `;

        const openProfileDropdownBtn = document.getElementById('openProfileDropdown');
        const profileDropdownMenu = document.getElementById('profileDropdownMenu');
        const logoutBtn = document.getElementById('logoutBtn');
        const viewProfileBtnDropdown = document.getElementById('viewProfileBtnDropdown'); // Updated ID to avoid conflict

        openProfileDropdownBtn.addEventListener('click', function(event) {
            event.stopPropagation();
            profileDropdownMenu.classList.toggle('hidden');
        });

        document.addEventListener('click', function(event) {
            if (!authNavButtonContainer.contains(event.target)) {
                profileDropdownMenu.classList.add('hidden');
            }
        });

        if (logoutBtn) {
            logoutBtn.addEventListener('click', function() {
                localStorage.removeItem('token');
                localStorage.removeItem('userId');
                window.location.href = '/login';
            });
        }

        if (viewProfileBtnDropdown) {
            viewProfileBtnDropdown.addEventListener('click', function() {
                const currentUserId = localStorage.getItem('userId');
                if (currentUserId) {
                    window.location.href = `/profile?userId=${currentUserId}`;
                } else {
                    alert('Không tìm thấy thông tin người dùng.');
                }
                profileDropdownMenu.classList.add('hidden');
            });
        }
    } else {
        authNavButtonContainer.innerHTML = '<a href="/login" class="text-lg">🔓 Login</a>';
    }

    // Logic for chat modals (có thể giữ nguyên hoặc điều chỉnh tùy theo thiết kế của bạn)
    const chatModal = document.getElementById('chatModal');
    const chatDetailModal = document.getElementById('chatDetailModal');
    const openModalBtn = document.getElementById('openModalBtn');

    if (openModalBtn) {
        openModalBtn.onclick = function() {
            chatModal.style.display = 'flex';
        }
    }

    // Close chat detail modal when clicking 'x'
    const closeDetailSpan = document.querySelector('#chatDetailModal .closeDetail');
    if (closeDetailSpan) {
        closeDetailSpan.onclick = function() {
            chatDetailModal.style.display = 'none';
        }
    }

    // Close chat list modal when clicking 'x'
    const closeChatModalSpan = document.querySelector('#chatModal .close');
    if (closeChatModalSpan) {
        closeChatModalSpan.onclick = function() {
            chatModal.style.display = 'none';
        }
    }

    // Close modals when clicking outside
    window.onclick = function(event) {
        if (event.target === chatModal) {
            chatModal.style.display = 'none';
        }
        if (event.target === chatDetailModal) {
            chatDetailModal.style.display = 'none';
        }
    }

    // Render Followings List (Right Sidebar)
    function renderFollowings() {
        if (!token) {
            const list = document.getElementById("followingsList");
            if (list) list.innerHTML = '<div class="text-gray-500">Bạn cần đăng nhập để xem người liên hệ.</div>';
            return;
        }

        fetch("/v1/profile/followings", {
            headers: { "Authorization": "Bearer " + token }
        })
            .then(res => {
                if (!res.ok) {
                    if (res.status === 401 || res.status === 403) {
                        localStorage.removeItem('token');
                        localStorage.removeItem('userId');
                        alert('Phiên làm việc đã hết hạn. Vui lòng đăng nhập lại.');
                        window.location.href = '/login';
                    }
                    throw new Error('Failed to fetch followings');
                }
                return res.json();
            })
            .then(data => {
                const list = document.getElementById("followingsList");
                const users = data.data && data.data.data ? data.data.data : [];
                if (users.length === 0) {
                    list.innerHTML = '<div class="text-gray-500">Bạn chưa follow ai.</div>';
                    return;
                }
                list.innerHTML = users.map(user => `
                <div class="flex items-center gap-2 p-2 cursor-pointer rounded-md transition-colors duration-200 hover:bg-gray-100" onclick="openChatModalWith('${user.nickName}','${user.avatarUrl || '/images/logo.jpg'}','${user.id}')">
                    <img src="${user.avatarUrl || '/images/logo.jpg'}" class="w-10 h-10 rounded-full object-cover">
                    <span class="font-medium">${user.nickName}</span>
                </div>
            `).join('');
            })
            .catch(error => {
                console.error("Error rendering followings:", error);
                const list = document.getElementById("followingsList");
                if (list) list.innerHTML = '<div class="text-gray-500">Không thể tải danh sách người liên hệ.</div>';
            });
    }
    renderFollowings(); // Call on DOMContentLoaded
});

// Hàm mới để chuyển hướng đến trang cá nhân (được đặt ở global scope để home.js có thể gọi)
window.viewUserProfile = async function(userId) {
    console.log("viewUserProfile called with userId:", userId); // LOGGING: Kiểm tra xem hàm có được gọi không
    const currentLoggedInUserId = localStorage.getItem("userId");

    if (userId && userId !== 'undefined' && userId !== 'null') { // Thêm kiểm tra giá trị 'undefined' và 'null' dưới dạng chuỗi
        if (String(userId) === String(currentLoggedInUserId)) {
            window.location.href = `/profile`; // Đi đến trang profile của chính mình
        } else {
            window.location.href = `/profile-user?userId=${userId}`; // Đi đến trang profile của người khác
        }
    } else {
        console.error("Không tìm thấy userId hợp lệ để chuyển hướng.");
    }
};
