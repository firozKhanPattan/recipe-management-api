package com.nl.recipeManagementAPI.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Recipe categories available in the system")
public enum Category {

    @Schema(description = "Recipes that fall under Vegetarian category")
    VEGETARIAN,
    @Schema(description = "Recipes that fall under Non Vegetarian category")
    NON_VEGETARIAN,
    @Schema(description = "Recipes that fall under Vegan category")
    VEGAN
}
