package com.nl.recipe_management_api.utils;

import com.nl.recipe_management_api.entity.Ingredient;
import com.nl.recipe_management_api.entity.Recipe;
import com.nl.recipe_management_api.model.RecipeDetails;
import com.nl.recipe_management_api.model.RecipeIngredient;
import lombok.experimental.UtilityClass;

import java.util.HashSet;
import java.util.Set;

@UtilityClass
public class RecipeFactory {

    public static RecipeDetails recipeDetails() {
    return RecipeDetails.builder()
        .recipeName("bread")
        .instructions("Bake in oven")
        .servings(5)
        .ingredients(Set.of(new RecipeIngredient(null,"flour"),new RecipeIngredient(null,"yeast")))
        .build();
    }

    public static Recipe recipe() {
        Recipe recipe = new Recipe();
        recipe.setRecipeName("bread");
        recipe.setInstructions("Bake in oven");
        recipe.setServings(5);
    recipe.setIngredients(Set.of(new Ingredient(null, "flour"), new Ingredient(null, "yeast")));
    return recipe;
    }
}
