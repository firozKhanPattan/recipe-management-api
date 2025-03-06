package com.nl.recipeManagementAPI.service;

import com.nl.recipeManagementAPI.model.RecipeDetails;
import com.nl.recipeManagementAPI.model.RecipeFilterRequest;

import java.util.List;

/**
 * @author Firoz
 * Type RecipeManagementService
 * <p>
 * Enables the management operations on the recipes.
 */
public interface RecipeManagementService {

    RecipeDetails createRecipe(RecipeDetails recipeDetails);

    RecipeDetails updateRecipe(Long id, RecipeDetails recipeDetails);

    void deleteRecipe(Long recipeId);

    List<RecipeDetails> filterRecipes(RecipeFilterRequest recipeFilterRequest);

    List<RecipeDetails> getAllRecipes();

    RecipeDetails getRecipe(String recipeName);
}
