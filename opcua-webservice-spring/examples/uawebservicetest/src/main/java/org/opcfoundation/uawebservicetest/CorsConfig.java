package org.opcfoundation.uawebservicetest;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 对所有接口生效
                .allowedOriginPatterns("*")  // 允许的源（Spring Boot 2.4+ 推荐使用 allowedOriginPatterns）
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // 允许的请求方法
                .allowedHeaders("*")  // 允许的请求头
                .exposedHeaders("token")  // 允许前端获取的响应头（如自定义的 token）
                .allowCredentials(true)  // 是否允许携带 cookie
                .maxAge(3600);  // 预检请求的有效期（秒），避免频繁预检
    }
}
