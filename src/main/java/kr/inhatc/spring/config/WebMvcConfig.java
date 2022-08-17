package kr.inhatc.spring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 업로드한 파일을 읽어올 경로를 설정
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {    // alt + shift + p -> 메소드 오버라이드

    @Value(value = "${uploadPath}")     // 프로퍼티 값 읽어오기
    String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")            // /images로 시작할 경우
                .addResourceLocations(uploadPath);                          // 로컬 경로
    }
}
