package com.example.christmas_wish_list.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                    "http://localhost:8081", 
                    "http://frontendflutter:8081", 
                    "https://literate-tribble-pjp6wr65vj4rhr65w-8081.app.github.dev") // Allow Flutter web client
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Ensure all necessary methods are allowed
                .allowedHeaders("*") // Allow all headers
                .allowCredentials(true); // Allow credentials if needed
    }
}