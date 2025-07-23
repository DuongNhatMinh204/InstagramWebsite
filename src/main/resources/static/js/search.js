// Hàm gọi API tìm kiếm người dùng
function searchUsers(query, callback) {
    const token = localStorage.getItem('token');
    fetch(`/v1/user/search?query=${encodeURIComponent(query)}`,
        {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}` // Thêm token vào header
            }
        })
        .then(res => {
            if (!res.ok) throw new Error('Không thể tìm kiếm người dùng');
            return res.json();
        })
        .then(data => {
            const users = data.data || [];
            callback(users);
        })
        .catch(error => {
            console.error('Lỗi khi tìm kiếm:', error);
            callback([]); // Trả về mảng rỗng nếu có lỗi
        });
}

document.addEventListener('DOMContentLoaded', function() {
    const searchInput = document.getElementById('searchInput');
    const searchButton = document.getElementById('searchButton');

    if (searchButton && searchInput) {
        searchButton.addEventListener('click', function() {
            const query = searchInput.value.trim();
            if (query) {
                window.location.href = `/search-results.html?query=${encodeURIComponent(query)}`;
            }
        });

        // Cho phép nhấn Enter để tìm kiếm
        searchInput.addEventListener('keydown', function(e) {
            if (e.key === 'Enter') {
                searchButton.click();
            }
        });
    }
});

// Hàm kiểm tra trạng thái follow
function checkFollow(userId, callback) {
    const token = localStorage.getItem('token');
    fetch(`/v1/user/follow/check-follow?followingUserId=${userId}`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}` // Thêm token vào header
        }
    })
        .then(res => {
            if (!res.ok) throw new Error('Không thể kiểm tra trạng thái follow');
            return res.json();
        })
        .then(data => {
            callback(data); // Trả về true hoặc false
        })
        .catch(error => {
            console.error('Lỗi khi kiểm tra follow:', error);
            callback(false); // Giả định không follow nếu có lỗi
        });
}

// Hàm follow người dùng
function followUser(userId, callback) {
    const token = localStorage.getItem('token');
    fetch(`/v1/user/follow/following?followingUserId=${userId}`, {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${token}`, // Thêm token vào header
            'Content-Type': 'application/json'
        }
    })
        .then(res => {
            if (!res.ok) throw new Error('Không thể follow người dùng');
            return res.json();
        })
        .then(data => {
            callback(data); // Trả về phản hồi từ API
        })
        .catch(error => {
            console.error('Lỗi khi follow:', error);
            callback({ code: 400, data: 'Follow thất bại' });
        });
}

// Hàm unfollow người dùng
function unfollowUser(userId, callback) {
    const token = localStorage.getItem('token');
    fetch(`/v1/user/follow/unfollowing?unfollowingUserId=${userId}`, {
        method: 'DELETE',
        headers: {
            'Authorization': `Bearer ${token}`// Thêm token vào header,

        }
    })
        .then(res => {
            if (!res.ok) throw new Error('Không thể unfollow người dùng');
            return res.json();
        })
        .then(data => {
            callback(data); // Trả về phản hồi từ API
        })
        .catch(error => {
            console.error('Lỗi khi unfollow:', error);
            callback({ code: 400, data: 'Unfollow thất bại' });
        });
}