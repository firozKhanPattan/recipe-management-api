package com.nl.recipeManagementAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.nl.recipeManagementAPI")
public class RecipeManagementApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecipeManagementApiApplication.class, args);
    }

}