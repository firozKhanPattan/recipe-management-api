package com.nl.recipe_management_api.service;

import com.nl.recipe_management_api.entity.Ingredient;
import com.nl.recipe_management_api.entity.Recipe;
import com.nl.recipe_management_api.exception.DuplicateRecipeException;
import com.nl.recipe_management_api.exception.RecipeNotFoundException;
import com.nl.recipe_management_api.mapper.RecipeMapper;
import com.nl.recipe_management_api.model.RecipeDetails;
import com.nl.recipe_management_api.model.RecipeFilterRequest;
import com.nl.recipe_management_api.model.RecipeIngredient;
import com.nl.recipe_management_api.repository.RecipeManagementRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class RecipeManagementServiceImpl implements RecipeManagementService {

    private final RecipeManagementRepository recipeManagementRepository;

    @Override
    public RecipeDetails createRecipe(RecipeDetails recipeDetails){
        Optional<Recipe> recipe = recipeManagementRepository.findByRecipeName(recipeDetails.getRecipeName().trim().toLowerCase());
        if(recipe.isPresent() && recipeDetails.getRecipeName().equalsIgnoreCase(recipe.get().getRecipeName())){
            throw new DuplicateRecipeException("Recipe already exists with the name: "+recipeDetails.getRecipeName());
        }
        Recipe newRecipe = RecipeMapper.INSTANCE.toRecipe(recipeDetails);
        Recipe savedRecipe =  recipeManagementRepository.save(newRecipe);
        return RecipeMapper.INSTANCE.fromRecipe(savedRecipe);
    }

    @Override
    public RecipeDetails updateRecipe(RecipeDetails newRecipeDetails){
        Optional<Recipe> recipe = recipeManagementRepository.findByRecipeName(newRecipeDetails.getRecipeName().trim().toLowerCase());
        if(recipe.isPresent()){
            Recipe recipeTobeUpdated = recipe.get();
            if(!CollectionUtils.isEmpty(newRecipeDetails.getIngredients())){
                recipeTobeUpdated.setIngredients(getDistinctIngredients(recipeTobeUpdated.getIngredients(),newRecipeDetails.getIngredients()));
            }
            recipeTobeUpdated.setCategory(Optional.ofNullable(newRecipeDetails.getCategory()).orElse(recipeTobeUpdated.getCategory()));
            recipeTobeUpdated.setInstructions(Optional.ofNullable(newRecipeDetails.getInstructions()).orElse(recipeTobeUpdated.getInstructions()));
            recipeTobeUpdated.setServings(Optional.ofNullable(newRecipeDetails.getServings()).orElse(recipeTobeUpdated.getServings()));
            Recipe updatedRecipe = recipeManagementRepository.save(recipeTobeUpdated);
            return RecipeMapper.INSTANCE.fromRecipe(updatedRecipe);
        }else{
            throw new RuntimeException("Recipe not found Exception");
        }
    }

    @Override
    public void deleteRecipe(Long recipeId){
        recipeManagementRepository.findById(recipeId).orElseThrow(()-> new RecipeNotFoundException("Recipe not found with id: "+recipeId));
        recipeManagementRepository.deleteById(recipeId);
    }

    @Override
    public List<RecipeDetails> filterRecipes(RecipeFilterRequest recipeFilterRequest){
        Specification<Recipe> specification = RecipeSearchSpecification.filterByCriteria(recipeFilterRequest);
        List<Recipe> recipes = recipeManagementRepository.findAll(specification);
        return recipes.stream()
                .map(recipe -> RecipeMapper.INSTANCE.fromRecipe(recipe))
                .collect(Collectors.toList());
    }

    @Override
    public List<RecipeDetails> getAllRecipes(){
        List<Recipe> recipes  = recipeManagementRepository.findAll();
        return recipes.stream()
                .map(recipe -> RecipeMapper.INSTANCE.fromRecipe(recipe))
                .collect(Collectors.toList());
    }

    @Override
    public RecipeDetails getRecipe(String recipeName){
        return RecipeMapper.INSTANCE.fromRecipe(getRecipeByName(recipeName));
    }

    private Recipe getRecipeByName(String name){
        return recipeManagementRepository.findByRecipeName(name)
                .orElseThrow(()->new RecipeNotFoundException("No Recipe found with the name: "+name));
    }

    private Set<Ingredient> getDistinctIngredients(Set<Ingredient> ingredients, Set<RecipeIngredient> newIngredients) {
        Set<Ingredient> newIngredientSet = newIngredients.stream()
                .map(recipeIngredient -> new Ingredient(recipeIngredient.getId(),recipeIngredient.getName()))
                .collect(Collectors.toSet());
        return new LinkedHashSet<>(Stream.of(ingredients, newIngredientSet)
                .flatMap(Collection::stream)
                .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Ingredient::getName)))));
    }

}
