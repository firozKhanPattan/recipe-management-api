package com.nl.recipeManagementAPI.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nl.recipeManagementAPI.entity.Ingredient;
import com.nl.recipeManagementAPI.entity.Recipe;
import com.nl.recipeManagementAPI.enums.Category;
import com.nl.recipeManagementAPI.mapper.RecipeMapper;
import com.nl.recipeManagementAPI.model.RecipeFilterRequest;
import com.nl.recipeManagementAPI.repository.RecipeManagementRepository;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;
import java.util.Set;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RecipeManagementIntegrationTest {

    @Autowired
    private RecipeManagementRepository recipeManagementRepository;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() throws Exception {
        recipeManagementRepository.deleteAll();
        List<Recipe> recipes = createTestRecipes();
        recipeManagementRepository.saveAll(recipes);
    }

    @Test
    @DisplayName("testAddRecipe : GIVEN a Request with RecipeDetails to create a Recipe THEN a creates a new Recipe")
    void testAddRecipe() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Recipe recipe = createTestRecipes().get(0);
        recipe.setRecipeName("salmon with salad");
        String recipeJson = mapper.writeValueAsString(RecipeMapper.INSTANCE.fromRecipe(recipe));
        given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(recipeJson)
                .when()
                .post("/recipe")
                .then()
                .statusCode(201)
                .body("recipeName", Matchers.equalTo("salmon with salad"));
    }

    @Test
    @DisplayName("testUpdateRecipe : GIVEN a request with new RecipeDetails THEN updates the existing recipe")
    void testUpdateRecipe() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Recipe recipe = createTestRecipes().get(0);
        recipe.setServings(10);
        String recipeJson = mapper.writeValueAsString(recipe);
        given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(recipeJson)
                .when()
                .put("/recipe/"+recipeManagementRepository.findAll().get(0).getId())
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("testDeleteRecipe : GIVEN a request to delete a Recipe THEN deletes a recipe")
    void testDeleteRecipe() throws Exception {
        given()
                .port(port)
                .contentType(ContentType.JSON)
                .delete("/recipe/" + recipeManagementRepository.findAll().get(0).getId())
                .then()
                .statusCode(204);
    }

    @Test
    @DisplayName("testGetAllRecipes : GIVEN a request to fetch all recipes THEN returns a list of Recipes")
    void testGetAllRecipes() throws Exception {
        given()
                .port(port)
                .accept(ContentType.JSON)
                .when()
                .get("/recipes")
                .then()
                .statusCode(200)
                .body("size()", Matchers.equalTo(2));
    }

    @Test
    @DisplayName("testGetRecipeByName : GIVEN a Request to get a recipe by name THEN returns the recipe details")
    void testGetRecipeByName() throws Exception {
        given()
                .log().everything()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .get("/recipe/" + createTestRecipes().get(0).getRecipeName())
                .then()
                .statusCode(200)
                .body("recipeName", Matchers.equalTo(createTestRecipes().get(0).getRecipeName()));
    }

    @Test
    @DisplayName("testFilterRecipes : GIVEN a request with searchCriteria THEN returns the list of Recipes matching the criteria")
    void testFilterRecipes() throws Exception {
        RecipeFilterRequest recipeFilterRequest = new RecipeFilterRequest();
        recipeFilterRequest.setServings(6);
        given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(new ObjectMapper().writeValueAsString(recipeFilterRequest))
                .post("/recipe/search")
                .then()
                .statusCode(200)
                .body("size()", Matchers.greaterThan(0))
                .body("get(0).servings", Matchers.equalTo(6));
    }

    @Test
    @DisplayName("testFilterRecipeByCriteria : GIVEN a request with searchCriteria THEN returns the list of Recipes matching the criteria")
    void testFilterRecipeByCriteria() throws Exception {
        RecipeFilterRequest recipeFilterRequest = new RecipeFilterRequest();
        recipeFilterRequest.setIncludesIngredient("potato");
        recipeFilterRequest.setExcludesIngredient("oil");
        given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(new ObjectMapper().writeValueAsString(recipeFilterRequest))
                .post("/recipe/search")
                .then()
                .statusCode(200)
                .body("get(0).recipeName", Matchers.equalTo(createTestRecipes().get(0).getRecipeName()));
    }

    private List<Recipe> createTestRecipes() throws Exception {
        Recipe recipe1 = new Recipe();
        recipe1.setRecipeName("Salmon Platter");
        recipe1.setCategory(Category.NON_VEGETARIAN);
        recipe1.setServings(4);
        recipe1.setInstructions("Grill the salmon");
        recipe1.setIngredients(Set.of(new Ingredient(null, "salmon"), new Ingredient(null, "potato"), new Ingredient(null, "salt")));
        Recipe recipe2 = new Recipe();
        recipe2.setRecipeName("Frites");
        recipe2.setCategory(Category.VEGETARIAN);
        recipe2.setServings(6);
        recipe2.setInstructions("deep fry in the oil");
        recipe2.setIngredients(Set.of(new Ingredient(null, "potato"), new Ingredient(null, "oil"), new Ingredient(null, "salt")));

        return List.of(recipe1, recipe2);
    }
}
