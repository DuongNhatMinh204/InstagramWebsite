// Profile Modal Logic
const profileBtn = document.getElementById("openProfileModal");
const profileModal = document.getElementById("profileModal");
const closeProfile = document.querySelector(".closeProfile");

profileBtn.addEventListener("click", () => {
    profileModal.style.display = "block";
});

closeProfile.addEventListener("click", () => {
    profileModal.style.display = "none";
});

window.addEventListener("click", (event) => {
    if (event.target === profileModal) {
        profileModal.style.display = "none";
    }
});


const viewProfileBtn = document.getElementById("viewProfileBtn");

viewProfileBtn.addEventListener("click", async () => {
    const token = localStorage.getItem("token");

    if (!token) {
        alert("Bạn chưa đăng nhập!");
        return;
    }

    window.location.href = "/profile";
});

