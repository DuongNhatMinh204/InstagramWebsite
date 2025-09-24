package com.nminh.websiteinstagram.model.response;


import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserInfoResponse {
    private Long id ;

    private String phone ;

    private String email ;

    private String password ;

    private String fullName ;

    private String nickName ;

    private LocalDate birthday;

    private String gender ;

    private String avatarUrl ;

    private Integer status ;
}
