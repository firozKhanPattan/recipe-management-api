package com.nl.recipeManagementAPI.mapper;

import com.nl.recipeManagementAPI.entity.Recipe;
import com.nl.recipeManagementAPI.model.RecipeDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.nl.recipeManagementAPI.utils.RecipeFactory.recipe;
import static com.nl.recipeManagementAPI.utils.RecipeFactory.recipeDetails;
import static org.assertj.core.api.Assertions.assertThat;

class RecipeMapperTest {

    private final RecipeMapper recipeMapper = RecipeMapper.INSTANCE;

    @Test
    @DisplayName("toRecipe : GIVEN a RecipeDetails maps to Recipe Entity")
    void toRecipe() {
        RecipeDetails recipeDetails = recipeDetails();
        Recipe recipe = recipeMapper.toRecipe(recipeDetails);
        assertThat(recipe).isNotNull();
        assertThat(recipe.getRecipeName())
                .isEqualToIgnoringCase(recipeDetails.getRecipeName());
        assertThat(recipe.getIngredients())
                .hasSameSizeAs(recipeDetails.getIngredients());
        assertThat(recipe.getCategory())
                .isEqualTo(recipeDetails.getCategory());
        assertThat(recipe.getServings())
                .isEqualTo(recipeDetails.getServings());
        assertThat(recipe.getInstructions())
                .isEqualTo(recipeDetails.getInstructions());
    }

    @Test
    @DisplayName("toRecipe : GIVEN a Recipe Entity maps to RecipeDetails")
    void fromRecipe() {
        Recipe recipe = recipe();
        RecipeDetails recipeDetails = recipeMapper.fromRecipe(recipe);
        assertThat(recipeDetails).isNotNull();
        assertThat(recipeDetails.getRecipeName())
                .isEqualToIgnoringCase(recipe.getRecipeName());
        assertThat(recipeDetails.getIngredients())
                .hasSameSizeAs(recipe.getIngredients());
        assertThat(recipeDetails.getCategory())
                .isEqualTo(recipe.getCategory());
        assertThat(recipeDetails.getServings())
                .isEqualTo(recipe.getServings());
        assertThat(recipeDetails.getInstructions())
                .isEqualTo(recipe.getInstructions());
    }
}