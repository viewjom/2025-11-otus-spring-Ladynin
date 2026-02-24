package ru.otus.hw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        System.out.println("http://localhost:8080/authors");
        System.out.println("http://localhost:8080/genres");
        System.out.println("http://localhost:8080/comments?id=1");
        System.out.println("http://localhost:8080/books");
        System.out.println("http://localhost:8080/api/books");
    }
}