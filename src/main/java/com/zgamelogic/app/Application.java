package com.zgamelogic.app;

import org.jspecify.annotations.NonNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableCaching
@EnableScheduling
@SpringBootApplication
public class Application {
    static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/**")
                    .allowedOrigins("http://localhost:5173", "https://msm-dev.zgamelogic.com", "https://msm.zgamelogic.com")
                    .allowedMethods("*")
                    .allowedHeaders("*")
                    .allowCredentials(true);
            }
        };
    }
}
