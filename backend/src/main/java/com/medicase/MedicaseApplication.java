package com.medicase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class MedicaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(MedicaseApplication.class, args);
    }
}