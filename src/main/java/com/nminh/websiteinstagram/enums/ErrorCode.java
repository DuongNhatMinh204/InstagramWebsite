package com.nminh.websiteinstagram.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    USER_EXISTS(1001,"user existed" , HttpStatus.BAD_REQUEST) ,
    EMAIL_NOT_VALID(1002,"email not valid" , HttpStatus.BAD_REQUEST) ,
    ARGURMENT_NOT_VALID(1003,"argument must be filled" , HttpStatus.BAD_REQUEST) ,
    PASSSWORD_MISMATCH(1004,"password not match" , HttpStatus.BAD_REQUEST) ,
    USER_NOT_EXISTS(1005,"user not existed" , HttpStatus.BAD_REQUEST) ,
    CANNOT_FOLLOW_YOURSELF(1006,"you can not follow yourself" , HttpStatus.BAD_REQUEST) ,
    CANNOT_REFOLLOW(1007,"you can not follow this person times" , HttpStatus.BAD_REQUEST) ;
    private int code ;
    private String message ;
    private HttpStatusCode httpStatusCode ;
    ErrorCode(int code, String message, HttpStatusCode httpStatusCode){
        this.code = code ;
        this.message = message;
        this.httpStatusCode = httpStatusCode ;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatusCode getHttpStatusCode() {
        return httpStatusCode;
    }
}
