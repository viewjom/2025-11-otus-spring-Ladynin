package ru.otus.hw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class BillingApplication {

    public static void main(String[] args) {
        SpringApplication.run(BillingApplication.class, args);
        System.out.println("http://localhost:8080/contracts");
        System.out.println("http://localhost:8080/api/contracts/1");
        System.out.println("http://localhost:8080/api/tariffs/1");
        System.out.println("http://localhost:8080/tariffList?id=1");
        System.out.println("http://localhost:8080/api/tells/1");

        System.out.println("http://localhost:8080/api/trafficDaily/contract/1");




        System.out.println("http://localhost:8080/h2-console");
    }
}