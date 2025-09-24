package com.nminh.websiteinstagram.model.response;

import com.nminh.websiteinstagram.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private Role role;
}
