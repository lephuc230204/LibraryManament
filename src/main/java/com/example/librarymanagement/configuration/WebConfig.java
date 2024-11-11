package com.example.librarymanagement.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Đảm bảo rằng đường dẫn tài nguyên tĩnh là hợp lệ
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:///D:/LibraryManament/public/uploads/");
    }
}
