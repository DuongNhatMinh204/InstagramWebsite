package com.nminh.websiteinstagram.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchUserDTOResponse {
    private Long id;
    private String nickname;
    private String avatar_url;
}
