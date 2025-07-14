package com.nminh.websiteinstagram.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "Phone number of user", example = "0387330657")
    private String phone ;
    @NotNull(message = "ARGURMENT_NOT_VALID")
    @Schema(description = "Phone number of user", example = "123456789")
    private String password ;
}
