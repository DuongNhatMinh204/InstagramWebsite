package com.nminh.websiteinstagram.model.request;

import lombok.Data;

@Data
public class ProfileRequestDTO {
    private String fullName;
    private String nickName;
    private String birthday;
    private String gender;
    private String address;
    private String workplace;
    private String maritalStatus;
    private String education;
}