package com.example.eventbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@Configuration
@EnableScheduling
public class EventbotApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventbotApplication.class, args);
    }

}
