package com.nl.recipe_management_api.model;

import com.nl.recipe_management_api.entity.Ingredient;
import com.nl.recipe_management_api.enums.Category;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDetails {

    private Long id;

    @NotEmpty(message="Recipe name cannot be empty")
    @NotNull(message = "Recipe name cannot be null")
    private String recipeName;

    @NotEmpty(message = "Please enter a valid Category")
    private Category category;

    private String instructions;

    private int servings;

    @NotEmpty(message="Ingredients list cannot be empty")
    private Set<Ingredient> ingredients;
}
