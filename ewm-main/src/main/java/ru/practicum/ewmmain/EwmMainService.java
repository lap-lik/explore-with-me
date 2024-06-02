package ru.practicum.ewmmain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

//@ComponentScan(basePackages = "ru.practicum.client")
@SpringBootApplication
public class EwmMainService {

    public static void main(String[] args) {
        SpringApplication.run(EwmMainService.class, args);
    }

}
