package com.nl.recipe_management_api.controller;

import com.nl.recipe_management_api.model.RecipeDetails;
import com.nl.recipe_management_api.model.RecipeFilterRequest;
import com.nl.recipe_management_api.service.RecipeManagementService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
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

@RestController
@RequiredArgsConstructor
public class RecipeManagementController {

    private final RecipeManagementService recipeManagementService;


    @PostMapping(path = "/recipe", consumes = "application/json", produces = "application/json")
    public ResponseEntity<RecipeDetails> addRecipe(@Valid @RequestBody final RecipeDetails recipeDetails){
        return new ResponseEntity<>(recipeManagementService.createRecipe(recipeDetails), HttpStatus.CREATED);
    }

    @PutMapping(path = "/recipe" , consumes = "application/json", produces = "application/json" )
    public  ResponseEntity updateRecipe(@NotNull @RequestBody final RecipeDetails newRecipeDetails){
        RecipeDetails updatedRecipe = recipeManagementService.updateRecipe(newRecipeDetails);
        return new ResponseEntity<>("Recipe updated Succesasfully",HttpStatus.OK);
    }

    @GetMapping(path="/recipes", produces = "application/json")
    public ResponseEntity<List<RecipeDetails>> getAllRecipes(){
        List<RecipeDetails> recipes = recipeManagementService.getAllRecipes();
        return new ResponseEntity<>(recipes,HttpStatus.OK);
    }

    @GetMapping(path ="/recipe/{recipeName}" , produces = "application/json")
    public ResponseEntity<RecipeDetails> getRecipe(@NotBlank @PathVariable("recipeName") final String recipeName){
        return new ResponseEntity<>(recipeManagementService.getRecipe(recipeName),HttpStatus.FOUND);
    }


    @DeleteMapping(path ="/recipe/{recipeName}", produces = "application/json")
    public ResponseEntity deleteRecipe(@NonNull @PathVariable("recipeName") final String recipeName){
        recipeManagementService.deleteRecipe(recipeName);
        return new ResponseEntity<>("Recipe deleted successfully",HttpStatus.OK);
    }

    @PostMapping(path ="/recipe/search", produces = "application/json")
    public ResponseEntity<List<RecipeDetails>> filterRecipes(@RequestBody @NonNull final RecipeFilterRequest recipeFilterRequest){
        List<RecipeDetails> recipes = recipeManagementService.filterRecipes(recipeFilterRequest);
        return new ResponseEntity<>(recipes,HttpStatus.OK);
    }
}
