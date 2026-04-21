package com.pfe.ultrasound_manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.pfe.entities")
@EnableJpaRepositories(basePackages = "com.pfe.repositories")
@ComponentScan(basePackages = "com.pfe")
public class UltrasoundManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(UltrasoundManagerApplication.class, args);
    }
}