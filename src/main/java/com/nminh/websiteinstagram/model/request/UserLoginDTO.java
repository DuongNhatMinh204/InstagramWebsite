package com.nminh.websiteinstagram.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDTO {
    @NotNull(message = "ARGURMENT_NOT_VALID")
    private String phone ;
    @NotNull(message = "ARGURMENT_NOT_VALID")
    private String password ;
}
