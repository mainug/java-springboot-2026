package com.pknu26.studygroup.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    private final FileProperties fileProperties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadDir = fileProperties.getUploadDir();

        // Window OS서버 경로처리부분 (file:/// 필수)
        // 예전에는 Spring 윈도우 경로 작성 시 D:\\upload\\studygroup
        // 현재는 D:/upload/studygroup
        // String resourcePath = "file:///" + uploadDir;
        String resourcePath = "file:///" + uploadDir.replace("\\", "/");
        
        registry.addResourceHandler("/upload/**")
                .addResourceLocations(resourcePath);
    }
}
