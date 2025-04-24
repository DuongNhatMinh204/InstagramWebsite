package com.nminh.websiteinstagram.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nminh.websiteinstagram.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDTO {
    @NotBlank(message = "ARGURMENT_NOT_VALID")
    private String phone ;
    @Email(message = "EMAIL_NOT_VALID")
    private String email ;
    @NotNull(message = "ARGURMENT_NOT_VALID")
    private String password ;
    @NotNull(message = "ARGURMENT_NOT_VALID")
    private String passwordConfirm ;
    @NotNull(message = "ARGURMENT_NOT_VALID")
    private String fullName ;
    @NotNull(message = "ARGURMENT_NOT_VALID")
    private String nickName ;
    @NotNull(message = "ARGURMENT_NOT_VALID")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate birthday ;
    @NotNull(message = "ARGURMENT_NOT_VALID")
    private String gender ;

    private Role role = Role.USER ;
}
