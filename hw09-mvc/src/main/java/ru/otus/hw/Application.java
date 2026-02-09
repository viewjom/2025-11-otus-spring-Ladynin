package ru.otus.hw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        System.out.println("http://localhost:8080/author");
        System.out.println("http://localhost:8080/genre");
        System.out.println("http://localhost:8080/comment?id=1");
        System.out.println("http://localhost:8080/book");
    }
}