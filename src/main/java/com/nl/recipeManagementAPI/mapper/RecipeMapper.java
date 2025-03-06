package com.nl.recipeManagementAPI.mapper;

import com.nl.recipeManagementAPI.entity.Recipe;
import com.nl.recipeManagementAPI.model.RecipeDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

@Mapper
@Service
public interface RecipeMapper {
    RecipeMapper INSTANCE = Mappers.getMapper(RecipeMapper.class);

    @Mapping(target = "recipeName", expression = "java(recipeDetails.getRecipeName().trim().toLowerCase())")
    Recipe toRecipe(RecipeDetails recipeDetails);

    RecipeDetails fromRecipe(Recipe recipe);
}
