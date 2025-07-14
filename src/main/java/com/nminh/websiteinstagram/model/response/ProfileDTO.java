package com.nminh.websiteinstagram.model.response;

import lombok.Data;

@Data
public class ProfileDTO {
    private String email;
    private String fullName;
    private String nickName;
    private String birthday;
    private String gender;
    private String avatarUrl;
    // Các trường profile đã được gộp
    private String address;
    private String workplace;
    private String maritalStatus;
    private String education;
}