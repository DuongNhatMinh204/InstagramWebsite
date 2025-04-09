package com.nminh.websiteinstagram.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    USER_EXISTS(1001,"user existed" , HttpStatus.BAD_REQUEST) ;
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
