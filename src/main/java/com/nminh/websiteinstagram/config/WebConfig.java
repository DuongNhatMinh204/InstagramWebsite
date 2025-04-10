package com.nminh.websiteinstagram.config;

import jakarta.persistence.Column;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/images/**") // Định nghĩa URL pattern để truy cập tài nguyên
                .addResourceLocations("file:uploads/"); // Đường dẫn vật lý đến thư mục chứa file
    }
}
