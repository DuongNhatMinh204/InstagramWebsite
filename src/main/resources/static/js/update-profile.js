// Đường dẫn: src/main/resources/static/js/update-profile.js
document.addEventListener('DOMContentLoaded', function() {
    const token = localStorage.getItem("token");
    const avatarFile = document.getElementById('avatarFile');
    const changeAvatarBtn = document.getElementById('changeAvatarBtn');
    const profileAvatarPreview = document.getElementById('profile-avatar-preview');
    const avatarUploadStatus = document.getElementById('avatarUploadStatus');
    const profileUpdateForm = document.getElementById('profileUpdateForm');
    const saveProfileBtn = document.getElementById('saveProfileBtn');
    const saveProfileText = document.getElementById('saveProfileText');
    const saveProfileSpinner = document.getElementById('saveProfileSpinner');
    const profileUpdateStatus = document.getElementById('profileUpdateStatus');

    document.addEventListener('DOMContentLoaded', function() {
        const token = localStorage.getItem("token");
        const userIdFromLocalStorage = localStorage.getItem("userId"); // Get current user ID

        // Redirect to login if no token
        if (!token) {
            alert("Vui lòng đăng nhập.");
            window.location.href = "/login";
            return; // Stop execution if not logged in
        }


    async function loadCurrentProfile() {
        try {
            // ĐIỀU CHỈNH ENDPOINT TẠI ĐÂY
            const res = await fetch("/v1/user/info", { // Sử dụng API từ UserController
                headers: { "Authorization": "Bearer " + token }
            });

            if (!res.ok) {
                if (res.status === 401 || res.status === 403) {
                    alert("Phiên đăng nhập hết hạn hoặc không có quyền. Vui lòng đăng nhập lại.");
                    localStorage.removeItem("token");
                    localStorage.removeItem("userId");
                    window.location.href = "/login";
                    return;
                }
                throw new Error(`Lỗi khi tải profile: ${res.statusText}`);
            }

            const data = await res.json();
            const profile = data.data; // Giả định `ApiResponse` vẫn được dùng và dữ liệu nằm trong trường `data`

            if (profile) {
                document.getElementById('fullName').value = profile.fullName || '';
                document.getElementById('nickName').value = profile.nickName || '';
                document.getElementById('birthday').value = profile.birthday || '';
                document.getElementById('gender').value = profile.gender || '';
                document.getElementById('address').value = profile.address || '';
                document.getElementById('workplace').value = profile.workplace || '';
                document.getElementById('maritalStatus').value = profile.maritalStatus || '';
                document.getElementById('education').value = profile.education || '';

                if (profile.avatarUrl) {
                    profileAvatarPreview.src = profile.avatarUrl;
                } else {
                    profileAvatarPreview.src = 'https://via.placeholder.com/150';
                }
            } else {
                profileUpdateStatus.className = 'text-red-500';
                profileUpdateStatus.innerText = data.message || 'Không thể tải thông tin profile. Dữ liệu rỗng.';
            }
        } catch (error) {
            console.error("Lỗi tải profile:", error);
            profileUpdateStatus.className = 'text-red-500';
            profileUpdateStatus.innerText = 'Đã xảy ra lỗi khi tải thông tin profile.';
        }
    }

    loadCurrentProfile();

    changeAvatarBtn.addEventListener('click', () => {
        avatarFile.click();
    });

    avatarFile.addEventListener('change', async () => {
        const file = avatarFile.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = (e) => {
                profileAvatarPreview.src = e.target.result;
            };
            reader.readAsDataURL(file);

            avatarUploadStatus.className = 'text-blue-500';
            avatarUploadStatus.innerText = 'Đang tải ảnh lên...';

            const formData = new FormData();
            formData.append('file', file);

            try {
                const uploadRes = await fetch("http://localhost:8080/api/images/upload", {
                    method: "POST",
                    body: formData
                });

                if (!uploadRes.ok) {
                    throw new Error(`Lỗi khi tải ảnh: ${uploadRes.statusText}`);
                }

                const imageUrl = await uploadRes.text();

                const updateAvtRes = await fetch(`/v1/profile/upload-avt?pathUrl=${encodeURIComponent(imageUrl)}`, {
                    method: "POST",
                    headers: { "Authorization": "Bearer " + token }
                });

                if (!updateAvtRes.ok) {
                    throw new Error(`Lỗi khi cập nhật avatar: ${updateAvtRes.statusText}`);
                }

                const updateAvtData = await updateAvtRes.json();
                if (updateAvtData.status === 'success') {
                    avatarUploadStatus.className = 'text-green-500';
                    avatarUploadStatus.innerText = 'Ảnh đại diện đã được cập nhật thành công!';
                } else {
                    avatarUploadStatus.className = 'text-red-500';
                    avatarUploadStatus.innerText = updateAvtData.message || 'Lỗi khi cập nhật avatar.';
                }

            } catch (error) {
                console.error("Lỗi tải ảnh hoặc cập nhật avatar:", error);
                avatarUploadStatus.className = 'text-red-500';
                avatarUploadStatus.innerText = 'Đã xảy ra lỗi khi tải ảnh hoặc cập nhật avatar.';
            }
        }
    });

    profileUpdateForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        saveProfileBtn.disabled = true;
        saveProfileText.classList.add('hidden');
        saveProfileSpinner.classList.remove('hidden');
        profileUpdateStatus.innerText = '';

        const formData = {
            fullName: document.getElementById('fullName').value,
            nickName: document.getElementById('nickName').value,
            birthday: document.getElementById('birthday').value,
            gender: document.getElementById('gender').value,
            address: document.getElementById('address').value,
            workplace: document.getElementById('workplace').value,
            maritalStatus: document.getElementById('maritalStatus').value,
            education: document.getElementById('education').value
        };

        try {
            const res = await fetch("/v1/profile/update", {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": "Bearer " + token
                },
                body: JSON.stringify(formData)
            });

            const data = await res.json();

            if (res.ok && data.status === 'success') {
                profileUpdateStatus.className = 'text-green-500';
                profileUpdateStatus.innerText = 'Cập nhật profile thành công!';
            } else {
                profileUpdateStatus.className = 'text-red-500';
                profileUpdateStatus.innerText = data.message || 'Cập nhật profile thất bại. Vui lòng thử lại.';
            }
        } catch (error) {
            console.error("Lỗi khi cập nhật profile:", error);
            profileUpdateStatus.className = 'text-red-500';
            profileUpdateStatus.innerText = 'Đã xảy ra lỗi mạng hoặc lỗi server.';
        } finally {
            saveProfileBtn.disabled = false;
            saveProfileText.classList.remove('hidden');
            saveProfileSpinner.classList.add('hidden');
        }
    });
});
});