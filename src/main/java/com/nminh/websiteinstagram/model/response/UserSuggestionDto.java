package com.nminh.websiteinstagram.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSuggestionDto {
    private Long id;
    private String nickName;
    private String avatarUrl;
    private boolean isFollowing; // Có thể thêm trạng thái follow

}
