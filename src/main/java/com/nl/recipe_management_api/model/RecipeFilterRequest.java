package com.nl.recipe_management_api.model;

import com.nl.recipe_management_api.enums.Category;
import lombok.Data;

@Data
public class RecipeFilterRequest {

    private Category Category;

    private Integer servings;

    private String instruction;

    private String includesIngredient;

    private String excludesIngredient;
}
