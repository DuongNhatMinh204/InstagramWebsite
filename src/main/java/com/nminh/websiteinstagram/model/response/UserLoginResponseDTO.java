package com.nminh.websiteinstagram.model.response;

public class UserLoginResponseDTO {
    private Long id;
    private String phone;

    public UserLoginResponseDTO(Long id, String phone) {
        this.id = id;
        this.phone = phone;
    }

    // Getters (Spring Boot sẽ tự động serialize thành JSON)
    public Long getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }
}
