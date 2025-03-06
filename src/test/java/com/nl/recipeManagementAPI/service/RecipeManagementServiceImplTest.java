package com.nl.recipeManagementAPI.service;

import com.nl.recipeManagementAPI.entity.Recipe;
import com.nl.recipeManagementAPI.enums.Category;
import com.nl.recipeManagementAPI.exception.RecipeExistsException;
import com.nl.recipeManagementAPI.exception.RecipeNotFoundException;
import com.nl.recipeManagementAPI.model.RecipeDetails;
import com.nl.recipeManagementAPI.model.RecipeFilterRequest;
import com.nl.recipeManagementAPI.repository.RecipeManagementRepository;
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

import static com.nl.recipeManagementAPI.utils.RecipeFactory.recipe;
import static com.nl.recipeManagementAPI.utils.RecipeFactory.recipeDetails;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecipeManagementServiceImplTest {

    @InjectMocks
    private RecipeManagementServiceImpl recipeManagementService;

    @Mock
    RecipeManagementRepository recipeManagementRepository;

    @Test
    @DisplayName("createRecipe : GIVEN recipeDetails THEN creates a new recipe")
    void createRecipe() {
        Recipe savedRecipe = recipe();
        savedRecipe.setId(111L);
        when(recipeManagementRepository.findByRecipeName(anyString())).thenReturn(Optional.ofNullable(null));
        when(recipeManagementRepository.save(any(Recipe.class))).thenReturn(savedRecipe);
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
        assertThatThrownBy(() -> recipeManagementService.createRecipe(recipeDetails()))
                .isInstanceOf(RecipeExistsException.class)
                .hasMessage("Recipe already exists with the name: " + recipe().getRecipeName());
    }

    @Test
    @DisplayName("updateRecipe: GIVEN new recipeDetails to update existing recipe THEN updates the recipe")
    void updateRecipe() {
        Recipe recipe = recipe();
        recipe.setServings(14);
        RecipeDetails recipeDetails = recipeDetails();
        recipeDetails.setServings(14);
        when(recipeManagementRepository.findById(recipe.getId())).thenReturn(Optional.of(recipe));
        when(recipeManagementRepository.save(recipe)).thenReturn(recipe);
        RecipeDetails upDatedRecipe = recipeManagementService.updateRecipe(111L, recipeDetails);
        assertThat(upDatedRecipe.getServings())
                .isEqualTo(recipeDetails.getServings());
    }

    @Test
    @DisplayName("updateNonExistentRecipe : GIVEN a request to update a non existent recipe THEN throws RecipeNotFoundException")
    void updateNonExistentRecipe(){
        RecipeDetails recipe = recipeDetails();
        when(recipeManagementRepository.findById(anyLong())).thenReturn(Optional.ofNullable(null));
        assertThrows(RecipeNotFoundException.class,()->recipeManagementService.updateRecipe(111L,recipe));
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
    @DisplayName("deleteNonExistenRecipe : GIVEN a request to delete a non existing recipe THEN throws RecipeNotFoundException")
    void deleteNonExistenRecipe(){
        when(recipeManagementRepository.findById(anyLong())).thenReturn(Optional.ofNullable(null));
        assertThrows(RecipeNotFoundException.class,()->recipeManagementService.deleteRecipe(23L));
    }


    @Test
    @DisplayName("filterRecipes:GIVEN a request to filter recipes based on criteria THEN returns the recipes matching criteria")
    void filterRecipes() {
        Recipe recipe1 = recipe();
        Recipe recipe2 = recipe();
        recipe2.setRecipeName("cake");
        recipe2.setServings(10);
        RecipeFilterRequest recipeFilterRequest = new RecipeFilterRequest();
        recipeFilterRequest.setCategory(Category.VEGETARIAN);
        when(recipeManagementRepository.findAll(Mockito.<Specification<Recipe>>any())).thenReturn(Arrays.asList(recipe1, recipe2));
        List<RecipeDetails> recipeDetails = recipeManagementService.filterRecipes(recipeFilterRequest);
        assertThat(recipeDetails).hasSize(2);
        assertThat(recipeDetails.stream()
                .map(RecipeDetails::getRecipeName)
                .collect(Collectors.toList())).containsAll(List.of("cake", "bread"));

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
    @DisplayName("getRecipe : GIVEN a request to fetch recipe by name  THEN returns the recipe")
    void getRecipe() {
        when(recipeManagementRepository.findByRecipeName(anyString())).thenReturn(Optional.of(recipe()));
        RecipeDetails recipeDetails = recipeManagementService.getRecipe("bread");
        assertThat(recipeDetails.getRecipeName()).isEqualTo("bread");
    }

    @Test
    @DisplayName("getNonExistentRecipe : GIVEN a request to fetch non existing recipe by name THEN throws RecipeNotFoundException")
    void getNonExistentRecipe(){
        when(recipeManagementRepository.findByRecipeName(anyString())).thenReturn(Optional.ofNullable(null));
        assertThrows(RecipeNotFoundException.class,()->recipeManagementService.getRecipe("bread"));
    }
}