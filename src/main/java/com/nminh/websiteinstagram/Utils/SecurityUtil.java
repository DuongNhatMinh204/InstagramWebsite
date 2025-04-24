package com.nminh.websiteinstagram.Utils;

import com.nminh.websiteinstagram.enums.ErrorCode;
import com.nminh.websiteinstagram.exception.AppException;
import com.nminh.websiteinstagram.security.CustomUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public  class  SecurityUtil {
    public static Long getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((CustomUserDetails) principal).getId();
        }
        throw new AppException(ErrorCode.NOT_FOUND_USER_ID);
    }
}
