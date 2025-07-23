package com.nminh.websiteinstagram.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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