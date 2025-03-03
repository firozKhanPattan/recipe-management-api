package com.nl.recipe_management_api.service;

import com.nl.recipe_management_api.model.RecipeDetails;
import com.nl.recipe_management_api.model.RecipeFilterRequest;

import java.util.List;

public interface RecipeManagementService {

    RecipeDetails createRecipe(RecipeDetails recipeDetails);

    RecipeDetails updateRecipe(RecipeDetails recipeDetails);

    void deleteRecipe(String recipeName);

    List<RecipeDetails> filterRecipes(RecipeFilterRequest recipeFilterRequest);
}
