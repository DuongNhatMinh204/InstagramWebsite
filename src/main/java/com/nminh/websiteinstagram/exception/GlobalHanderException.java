package com.nminh.websiteinstagram.exception;

import com.nminh.websiteinstagram.enums.ErrorCode;
import com.nminh.websiteinstagram.model.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalHanderException {
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<String> runtimeExceptionHandler(RuntimeException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<ApiResponse> appException(final AppException e) {

        ErrorCode errorCode = e.getErrorCode();
        ApiResponse apiResponse = new ApiResponse(errorCode.getCode(),errorCode.getMessage());

        return ResponseEntity.status(errorCode.getHttpStatusCode()).body(apiResponse) ;
    }
}
