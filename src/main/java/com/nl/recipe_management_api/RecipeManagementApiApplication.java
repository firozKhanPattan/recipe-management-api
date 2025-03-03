package com.nl.recipe_management_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.nl.recipe_management_api")
public class RecipeManagementApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecipeManagementApiApplication.class, args);
	}

}
