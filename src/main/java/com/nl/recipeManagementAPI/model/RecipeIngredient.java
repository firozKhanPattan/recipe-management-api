package com.nl.recipeManagementAPI.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RecipeIngredient {

    private Long id;

    private String name;
}