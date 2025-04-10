package com.nminh.websiteinstagram.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private int code = 1000 ;
    private String message ;
    private T data ;
    public ApiResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }
    public ApiResponse( String message, T data) {
        this.message = message;
        this.data = data;
    }
}
