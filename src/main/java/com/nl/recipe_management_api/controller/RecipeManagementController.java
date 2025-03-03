package com.nl.recipe_management_api.controller;

import com.nl.recipe_management_api.model.RecipeDetails;
import com.nl.recipe_management_api.model.RecipeFilterRequest;
import com.nl.recipe_management_api.service.RecipeManagementService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RecipeManagementController {

    private final RecipeManagementService recipeManagementService;

    @PostMapping("/recipe")
    public ResponseEntity<RecipeDetails> createRecipe(@NonNull @RequestBody final RecipeDetails recipeDetails){
        return new ResponseEntity<>(recipeManagementService.createRecipe(recipeDetails), HttpStatus.CREATED);
    }

    @PutMapping("/recipe")
    public  ResponseEntity updateRecipe(@NonNull @RequestBody final RecipeDetails newRecipeDetails){
        RecipeDetails updatedRecipe = recipeManagementService.updateRecipe(newRecipeDetails);
        return new ResponseEntity<>("Recipe updated Succesasfully",HttpStatus.OK);
    }

    @DeleteMapping("/recipe/{recipeName}")
    public ResponseEntity deleteRecipe(@NonNull @PathVariable("recipeName") final String recipeName){
        recipeManagementService.deleteRecipe(recipeName);
        return new ResponseEntity<>("Recipe deleted successfully",HttpStatus.OK);
    }

    @PostMapping("/recipe/search")
    public ResponseEntity<List<RecipeDetails>> filterRecipes(@RequestBody @NonNull final RecipeFilterRequest recipeFilterRequest){
        List<RecipeDetails> recipes = recipeManagementService.filterRecipes(recipeFilterRequest);
        return new ResponseEntity<>(recipes,HttpStatus.OK);
    }
}
