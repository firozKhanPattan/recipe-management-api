package com.nl.recipeManagementAPI.model;

import com.nl.recipeManagementAPI.enums.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeDetails {

    private Long id;

    @NotBlank(message = "Recipe name cannot be empty")
    @NotNull(message = "Recipe name cannot be null")
    private String recipeName;

    @Schema(description = "The Category of the recipe", example = "VEGETARIAN", allowableValues = {"VEGETARIAN", "NON_VEGETARIAN", "VEGAN"})
    private Category category;

    private String instructions;

    private int servings;

    @NotEmpty(message = "Ingredients list cannot be empty")
    private Set<RecipeIngredient> ingredients;
}
