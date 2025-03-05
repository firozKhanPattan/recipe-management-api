package com.nl.recipe_management_api.service;

import com.nl.recipe_management_api.entity.Ingredient;
import com.nl.recipe_management_api.entity.Recipe;
import com.nl.recipe_management_api.enums.Category;
import com.nl.recipe_management_api.exception.DuplicateRecipeException;
import com.nl.recipe_management_api.model.RecipeDetails;
import com.nl.recipe_management_api.model.RecipeFilterRequest;
import com.nl.recipe_management_api.model.RecipeIngredient;
import com.nl.recipe_management_api.repository.RecipeManagementRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.nl.recipe_management_api.utils.RecipeFactory.recipe;
import static com.nl.recipe_management_api.utils.RecipeFactory.recipeDetails;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeManagementServiceImplTest {

    @InjectMocks
    private RecipeManagementServiceImpl recipeManagementService;

    @Mock
    RecipeManagementRepository recipeManagementRepository;

    @Test
    void createRecipe() {
        Recipe recipe = recipe();
        Recipe savedRecipe = recipe();
        savedRecipe.setId(111L);
        when(recipeManagementRepository.findByRecipeName(anyString())).thenReturn(Optional.ofNullable(null));
        when(recipeManagementRepository.save(recipe)).thenReturn(savedRecipe);
        RecipeDetails recipeDetails = recipeManagementService.createRecipe(recipeDetails());
        assertThat(recipeDetails.getRecipeName())
                .isEqualTo(savedRecipe.getRecipeName());
        assertThat(recipeDetails.getId())
                .isNotNull();
      }

    @Test
    @DisplayName("addExistingRecipe : GIVEN a recipe which already exists  THEN returns an Exception with message")
     void duplicateRecipeTest() {
        when(recipeManagementRepository.findByRecipeName(anyString())).thenReturn(Optional.of(recipe()));
        assertThatThrownBy( () -> recipeManagementService.createRecipe(recipeDetails()))
                .isInstanceOf(DuplicateRecipeException.class)
                .hasMessage("Recipe already exists with the name: "+recipe().getRecipeName());
    }

    @Test
    void updateRecipe() {
        Recipe recipe = recipe();
        recipe.setServings(14);
        RecipeDetails recipeDetails = recipeDetails();
        recipeDetails.setServings(14);
        when(recipeManagementRepository.findByRecipeName(recipe.getRecipeName())).thenReturn(Optional.of(recipe));
        when(recipeManagementRepository.save(recipe)).thenReturn(recipe);
        RecipeDetails upDatedRecipe = recipeManagementService.updateRecipe(recipeDetails);
        assertThat(upDatedRecipe.getServings())
                .isEqualTo(recipeDetails.getServings());
      }

    @Test
    @DisplayName("deleteRecipeById : GIVEN a recipeId  THEN deletes that from the database")
    void deleteRecipe() {
        Recipe recipe = recipe();
        when(recipeManagementRepository.findById(anyLong())).thenReturn(Optional.of(recipe));
        doNothing().when(recipeManagementRepository).deleteById(recipe.getId());
        recipeManagementService.deleteRecipe(recipe.getId());
    verify(recipeManagementRepository, times(1)).deleteById(recipe.getId());
    }


    @Test
    void filterRecipes() {
        Recipe recipe1 = recipe();
        Recipe  recipe2 = recipe();
        recipe2.setRecipeName("cake");
        recipe2.setServings(10);
        RecipeFilterRequest recipeFilterRequest = new RecipeFilterRequest();
        recipeFilterRequest.setCategory(Category.VEGETARIAN);
        when(recipeManagementRepository.findAll(Mockito.<Specification<Recipe>>any())).thenReturn(Arrays.asList(recipe1, recipe2));
        List<RecipeDetails> recipeDetails = recipeManagementService.filterRecipes(recipeFilterRequest);
        assertThat(recipeDetails).hasSize(2);
        assertThat(recipeDetails.stream()
                .map(recipeDetails1 -> recipeDetails1.getRecipeName())
                .collect(Collectors.toList())).containsAll(List.of("cake","bread"));

      }

    @Test
    @DisplayName("getAllRecipes : Fetches all the available recipes in the database")
    void getAllRecipes() {
        List<Recipe> recipeList = Arrays.asList(recipe());
        when(recipeManagementRepository.findAll()).thenReturn(recipeList);
        List<RecipeDetails> recipesList = recipeManagementService.getAllRecipes();
        assertThat(recipesList.size()).isEqualTo(1);
    }

    @Test
    void getRecipe() {
        when(recipeManagementRepository.findByRecipeName(anyString())).thenReturn(Optional.of(recipe()));
        RecipeDetails recipeDetails = recipeManagementService.getRecipe("bread");
        assertThat(recipeDetails.getRecipeName()).isEqualTo("bread");
      }
}