package com.nl.recipeManagementAPI.controller;

import com.nl.recipeManagementAPI.model.RecipeDetails;
import com.nl.recipeManagementAPI.model.RecipeFilterRequest;
import com.nl.recipeManagementAPI.service.RecipeManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Firoz
 * @version 0.0.1
 * <p>
 * Type RecipeManagementController
 * Exposes end points to create, update, delete, fetch and filter the recipes
 */
@RestController
@RequiredArgsConstructor
public class RecipeManagementController {

    private final RecipeManagementService recipeManagementService;

    /**
     * Creates a new recipe.
     *
     * @param recipeDetails the recipe details that needs to be added
     * @return the created recipe
     */
    @Operation(summary = "Create a new recipe", description = "Adds a new recipe to the system with category." +
            "Allowed Categories: VEGETARIAN,NON_VEGETARIAN,VEGAN")
    @ApiResponse(responseCode = "201", description = "Recipe created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RecipeDetails.class)))
    @PostMapping(path = "/recipe", consumes = "application/json", produces = "application/json")
    public ResponseEntity<RecipeDetails> addRecipe(@Valid @RequestBody final RecipeDetails recipeDetails) {
        RecipeDetails createdRecipe = recipeManagementService.createRecipe(recipeDetails);
        return new ResponseEntity<>(createdRecipe, HttpStatus.CREATED);
    }

    /**
     * Updates an existing Recipe.
     *
     * @param newRecipeDetails The updated recipe details.
     * @return A {@link ResponseEntity} containing the updated Recipe.
     */
    @Operation(summary = "Update a Recipe", description = "Updates an existing Recipe details.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Recipe updated successfully"),
            @ApiResponse(responseCode = "404", description = "Recipe not found")
    })
    @PutMapping(path = "/recipe/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<RecipeDetails> updateRecipe(@NotNull(message = "RecipeId is missing") @PathVariable("id") Long id, @Valid @RequestBody final RecipeDetails newRecipeDetails) {
        RecipeDetails recipeDetails = recipeManagementService.updateRecipe(id, newRecipeDetails);
        return new ResponseEntity<>(recipeDetails, HttpStatus.OK);
    }

    /**
     * Retrieves a list of all available Recipes.
     *
     * @return A {@link ResponseEntity} containing a list of Recipes.
     */
    @Operation(summary = "Get all Recipes", description = "Retrieves all available recipes in the system.")
    @ApiResponse(responseCode = "200", description = "Recipes retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RecipeDetails.class)))
    @GetMapping(path = "/recipes", produces = "application/json")
    public ResponseEntity<List<RecipeDetails>> getAllRecipes() {
        List<RecipeDetails> recipes = recipeManagementService.getAllRecipes();
        return new ResponseEntity<>(recipes, HttpStatus.OK);
    }

    /**
     * Retrieves a recipe by their unique name.
     *
     * @param recipeName The unique name of the recipe to retrieve.
     * @return A {@link ResponseEntity} containing the recipe if found, or a 404 response if not found.
     */
    @Operation(summary = "Get Recipe by name", description = "Retrieves a specific recipe based on its name.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Recipe found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RecipeDetails.class))),
            @ApiResponse(responseCode = "404", description = "Recipe not found")
    })
    @GetMapping(path = "/recipe/{recipeName}", produces = "application/json")
    public ResponseEntity<RecipeDetails> getRecipe(@NotBlank @PathVariable("recipeName") final String recipeName) {
        return new ResponseEntity<>(recipeManagementService.getRecipe(recipeName), HttpStatus.OK);
    }

    /**
     * Deletes a recipe by their ID.
     *
     * @param recipeId The ID of the recipe to delete.
     * @return A {@link ResponseEntity} indicating success or failure.
     */
    @Operation(summary = "Delete a recipe", description = "Removes a recipe from the system.")
    @ApiResponse(responseCode = "204", description = "Recipe deleted successfully")
    @DeleteMapping(path = "/recipe/{recipeId}", produces = "application/json")
    public ResponseEntity deleteRecipe(@NotNull(message="Recipe Id is missing") @PathVariable("recipeId") final Long recipeId) {
        recipeManagementService.deleteRecipe(recipeId);
        return new ResponseEntity<>("Recipe deleted successfully", HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Search for recipes based on criteria recipe", description = "Search can be combination of servings,category,includesIngredient,excludesIngredient" +
            "Allowed Categories: VEGETARIAN,NON_VEGETARIAN,VEGAN")
    @ApiResponse(responseCode = "200", description = "Recipes retrieved successfully based on the criteria",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RecipeDetails.class)))
    @PostMapping(path = "/recipe/search", produces = "application/json")
    public ResponseEntity<List<RecipeDetails>> filterRecipes(@RequestBody @Valid final RecipeFilterRequest recipeFilterRequest) {
        List<RecipeDetails> recipes = recipeManagementService.filterRecipes(recipeFilterRequest);
        return new ResponseEntity<>(recipes, HttpStatus.OK);
    }
}
