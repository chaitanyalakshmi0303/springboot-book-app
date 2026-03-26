package com.vvit.bookapp;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Book API",
                version = "1.0",
                description = "API for managing books"
        )
)
public class BookAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookAppApplication.class, args);
    }

}
