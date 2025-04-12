package com.nminh.websiteinstagram.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowerResponseDTO {
    private int total_followers ;
    private List<UserResponseDTO> followers ;
}
