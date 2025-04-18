package com.payway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.payway.models") // Ensures Hibernate scans the models package
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
