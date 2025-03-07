package com.nl.recipeManagementAPI.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nl.recipeManagementAPI.enums.Category;
import com.nl.recipeManagementAPI.exception.RecipeExistsException;
import com.nl.recipeManagementAPI.exception.RecipeNotFoundException;
import com.nl.recipeManagementAPI.model.RecipeDetails;
import com.nl.recipeManagementAPI.model.RecipeFilterRequest;
import com.nl.recipeManagementAPI.model.RecipeIngredient;
import com.nl.recipeManagementAPI.service.RecipeManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest
class RecipeManagementControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    RecipeManagementService recipeManagementService;

    RecipeDetails recipeDetails;

    @BeforeEach
    void setUp() {
        recipeDetails = RecipeDetails
                .builder()
                .recipeName("kip suate")
                .category(Category.NON_VEGETARIAN)
                .instructions("grill on flame")
                .servings(4)
                .ingredients(Set.of(new RecipeIngredient(202L, "kip"),
                        new RecipeIngredient(203L, "Pinda Saus")))
                .build();
    }

    @Test
    @DisplayName("getAllRecipes : GIVEN a request to fetch all recipes THEN returns a list of Recipes")
    void getAllRecipes() throws Exception {
        when(recipeManagementService.getAllRecipes()).thenReturn(List.of(recipeDetails));
        mockMvc.perform(get("/recipes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("getRecipe : GIVEN a Request to get a recipe by name THEN returns the recipe details")
    void getRecipe() throws Exception {
        when(recipeManagementService.getRecipe(any(String.class))).thenReturn(recipeDetails);
        mockMvc.perform(get("/recipe/kip suate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recipeName").value(recipeDetails.getRecipeName()));
    }

    @Test
    @DisplayName("createRecipe : GIVEN a Request with RecipeDetails to create a Recipe THEN a creates a new Recipe")
    void createRecipe() throws Exception {
        mockMvc.perform(post("/recipe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(recipeDetails)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("createExistingRecipe : GIVEN a request to create an already existing recipe THEN returns status CONFLICT")
    void createExistingRecipe() throws Exception{
        when(recipeManagementService.createRecipe(any(RecipeDetails.class))).thenThrow(RecipeExistsException.class);
        mockMvc.perform(post("/recipe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(recipeDetails)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("updateRecipe : GIVEN a request with new RecipeDetails THEN updates the existing recipe")
    void updateRecipe() throws Exception {
        recipeDetails.setServings(8);
        mockMvc.perform(MockMvcRequestBuilders.put("/recipe/222")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(recipeDetails)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("updateNoNExistingRecipe: GIVEN a request to update non Existing Recipe THEN returns the status NOTFOUND")
    void updateNoNExistingRecipe() throws Exception {
        when(recipeManagementService.updateRecipe(anyLong(),any(RecipeDetails.class))).thenThrow(RecipeNotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders.put("/recipe/222")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(recipeDetails)))
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("deleteRecipe : GIVEN a request to delete a Recipe THEN deletes a recipe")
    void deleteRecipe() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/recipe/232"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("deleteNonExistingRecipe: GIVEN a request to delete non existing Recipe THEN returns status NOTFOUND")
    void deleteNonExistingRecipe() throws Exception {
        doThrow(RecipeNotFoundException.class).when(recipeManagementService)
                .deleteRecipe(anyLong());
        mockMvc.perform(MockMvcRequestBuilders.delete("/recipe/232"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("filterRecipes : GIVEN a request with searchCriteria THEN returns the list of Recipes matching the criteria")
    void filterRecipes() throws Exception {
        String filterRequest = "{\"servings\":4,\"instructions\":null,\"includesIngredient\":null,\"excludesIngredient\":null,\"Category\":null }";
        when(recipeManagementService.filterRecipes(any(RecipeFilterRequest.class))).thenReturn(List.of(recipeDetails));
        mockMvc.perform(post("/recipe/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filterRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}