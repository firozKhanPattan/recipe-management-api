package com.nl.recipeManagementAPI.utils;

import com.nl.recipeManagementAPI.entity.Ingredient;
import com.nl.recipeManagementAPI.entity.Recipe;
import com.nl.recipeManagementAPI.enums.Category;
import com.nl.recipeManagementAPI.model.RecipeDetails;
import com.nl.recipeManagementAPI.model.RecipeIngredient;
import lombok.experimental.UtilityClass;

import java.util.Set;

@UtilityClass
public class RecipeFactory {

    public static RecipeDetails recipeDetails() {
        return RecipeDetails.builder()
                .recipeName("bread")
                .instructions("Bake in oven")
                .category(Category.VEGETARIAN)
                .servings(5)
                .ingredients(Set.of(new RecipeIngredient(null, "flour"), new RecipeIngredient(null, "yeast")))
                .build();
    }

    public static Recipe recipe() {
        Recipe recipe = new Recipe();
        recipe.setId(111L);
        recipe.setCategory(Category.VEGETARIAN);
        recipe.setRecipeName("bread");
        recipe.setInstructions("Bake in oven");
        recipe.setServings(5);
        recipe.setIngredients(Set.of(new Ingredient(null, "flour"), new Ingredient(null, "yeast")));
        return recipe;
    }
}