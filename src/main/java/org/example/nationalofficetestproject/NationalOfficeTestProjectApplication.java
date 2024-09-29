package org.example.nationalofficetestproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class NationalOfficeTestProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(NationalOfficeTestProjectApplication.class, args);
    }

}
