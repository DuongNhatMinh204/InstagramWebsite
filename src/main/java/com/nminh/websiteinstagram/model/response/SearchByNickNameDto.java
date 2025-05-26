package com.nminh.websiteinstagram.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchByNickNameDto {
    private long id;
    private String nickName ;
    private String gender ;
    private String avatarUrl ;
}
