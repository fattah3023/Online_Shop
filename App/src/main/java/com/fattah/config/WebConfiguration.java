package com.fattah.config;


import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Value("${app.cors.allow-origins}")
    List<String> validAddresses;

    @PostConstruct
    public void printAllowedOrigins() {
        System.out.println("Allowed Origins = " + validAddresses);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowCredentials(true)
                .allowedMethods("POST","PUT","GET","DELETE")
                .allowedHeaders("*")
                .allowedOrigins(validAddresses.toArray(new String[0]));
    }
}
