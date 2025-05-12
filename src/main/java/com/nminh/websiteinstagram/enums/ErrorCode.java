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
    CANNOT_REFOLLOW(1007,"you can not follow this person times" , HttpStatus.BAD_REQUEST) ,
    CANNOT_UNFOLLOW(1008,"you can not unfollow this person because you have not been following before" , HttpStatus.BAD_REQUEST) ,
    CANNOT_LIKE_TWO_TIMES(1009,"you can not like two times" , HttpStatus.BAD_REQUEST) ,
    POST_NOT_FOUND(1010,"post not found" , HttpStatus.NOT_FOUND) ,
    CANNOT_UNLIKE(1011,"you can not unlike this post" , HttpStatus.BAD_REQUEST) ,
    CANNOT_FOUND_LIKE(1012,"you can not found like this post" , HttpStatus.BAD_REQUEST) ,
    TOKEN_EXPIRED(1013,"token expired" , HttpStatus.BAD_REQUEST) ,
    NOT_FOUND_USER_ID(1014,"not found user id in token" , HttpStatus.BAD_REQUEST) ;


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
