package com.nl.recipe_management_api.mapper;

import com.nl.recipe_management_api.entity.Recipe;
import com.nl.recipe_management_api.model.RecipeDetails;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Mapper
@Component
public interface RecipeMapper {
    RecipeMapper INSTANCE = Mappers.getMapper(RecipeMapper.class);

    Recipe toRecipe(RecipeDetails recipeDetails);

    RecipeDetails fromRecipe(Recipe recipe);
}
