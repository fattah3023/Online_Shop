package com.fattah.app;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
@EnableCaching
@SpringBootApplication
@ComponentScan(basePackages = "com.fattah.*")
@EntityScan(basePackages = "com.fattah.entity")
@EnableJpaRepositories(basePackages = "com.fattah.repository")
@OpenAPIDefinition(info=@Info(title = "Online_Shop_API",version = "1.0",description = "This is a Website API programmed by Java Spring Boot by Fattah Arbab"))
public class AppApplication {
    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }

}
