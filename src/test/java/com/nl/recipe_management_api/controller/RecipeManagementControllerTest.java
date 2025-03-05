package com.nl.recipe_management_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nl.recipe_management_api.enums.Category;
import com.nl.recipe_management_api.model.RecipeDetails;
import com.nl.recipe_management_api.model.RecipeFilterRequest;
import com.nl.recipe_management_api.model.RecipeIngredient;
import com.nl.recipe_management_api.service.RecipeManagementService;
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
                .ingredients(Set.of(new RecipeIngredient(202L,"kip"),
                        new RecipeIngredient(203L,"Pinda Saus")))
                .build();
    }

    @Test
    @DisplayName("GIVEN a request to fetch all recipes THEN returns a list of Recipes")
    void getAllRecipes() throws Exception {
        when(recipeManagementService.getAllRecipes()).thenReturn(List.of(recipeDetails));
        mockMvc.perform(get("/recipes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1)
                );
    }

    @Test
    @DisplayName("GIVEN a Request to get a recipe by name THEN returns the recipe details")
    void getRecipe() throws Exception {
        when(recipeManagementService.getRecipe(any(String.class))).thenReturn(recipeDetails);
        mockMvc.perform(get("/recipe/kip suate"))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.recipeName").value(recipeDetails.getRecipeName()));
    }

    @Test
    @DisplayName("GIVEN a Request with RecipeDetails to create a Recipe THEN a creates a new Recipe")
    void createRecipe() throws Exception {
        mockMvc.perform(post("/recipe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(recipeDetails)))
                .andExpect(status().isCreated());
    }


    @Test
    @DisplayName("GIVEN a request with new RecipeDetails THEN updates the existing recipe")
    void updateRecipe() throws Exception {
        recipeDetails.setServings(8);
        mockMvc.perform(MockMvcRequestBuilders.put("/recipe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(recipeDetails)))
                .andExpect(status().isOk());
    }


    @Test
    @DisplayName("GIVEN a request to delete a Recipe THEN deletes a recipe")
    void deleteRecipe() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/recipe/kip suate"))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("GIVEN a request with searchCriteria THEN returns the list of Recipes matching the criteria")
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