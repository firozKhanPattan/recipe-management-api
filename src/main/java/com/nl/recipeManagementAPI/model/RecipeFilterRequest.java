package com.nl.recipeManagementAPI.model;

import com.nl.recipeManagementAPI.enums.Category;
import lombok.Data;

/**
 * Type RecipeFilterRequest
 * <p>
 * The combinations on which the recipes can be filtered or searched.
 */
@Data
public class RecipeFilterRequest {

    private Category Category;

    private Integer servings;

    private String instruction;

    private String includesIngredient;

    private String excludesIngredient;
}
