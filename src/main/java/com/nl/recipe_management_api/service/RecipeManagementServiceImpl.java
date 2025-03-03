package com.nl.recipe_management_api.service;

import com.nl.recipe_management_api.entity.Ingredient;
import com.nl.recipe_management_api.entity.Recipe;
import com.nl.recipe_management_api.exception.DuplicateRecipeException;
import com.nl.recipe_management_api.mapper.RecipeMapper;
import com.nl.recipe_management_api.model.RecipeDetails;
import com.nl.recipe_management_api.model.RecipeFilterRequest;
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
        Optional<Recipe> recipe = recipeManagementRepository.findByRecipeName(recipeDetails.getRecipeName());
        if(recipe.isPresent() && recipeDetails.getRecipeName().equalsIgnoreCase(recipe.get().getRecipeName())){
            throw new DuplicateRecipeException("Recipe already exists with the name: "+recipeDetails.getRecipeName());
        }
        Recipe newRecipe = RecipeMapper.INSTANCE.toRecipe(recipeDetails);
        Recipe savedRecipe =  recipeManagementRepository.save(newRecipe);
        return RecipeMapper.INSTANCE.fromRecipe(savedRecipe);
    }

    @Override
    public RecipeDetails updateRecipe(RecipeDetails newRecipeDetails){
        Optional<Recipe> recipe = recipeManagementRepository.findByRecipeName(newRecipeDetails.getRecipeName());
        if(recipe.isPresent()){
            Recipe recipeTobeUpdated = recipe.get();
            if(!CollectionUtils.isEmpty(newRecipeDetails.getIngredients())){
                recipeTobeUpdated.getIngredients()
                        .addAll(getDistinctIngredients(recipeTobeUpdated.getIngredients(),newRecipeDetails.getIngredients()));
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
    public void deleteRecipe(String recipeName){
       Recipe recipe = recipeManagementRepository.findByRecipeName(recipeName).orElseThrow(()-> new RuntimeException("No recipe found with the name"));
       recipeManagementRepository.deleteById(recipe.getId());
    }

    @Override
    public List<RecipeDetails> filterRecipes(RecipeFilterRequest recipeFilterRequest){
        Specification<Recipe> specification = RecipeSearchSpecification.filterByCriteria(recipeFilterRequest);
        List<Recipe> recipes = recipeManagementRepository.findAll(specification);
        return recipes.stream()
                .map(recipe -> RecipeMapper.INSTANCE.fromRecipe(recipe))
                .collect(Collectors.toList());
    }

    private Set<Ingredient> getDistinctIngredients(Set<Ingredient> ingredients, Set<Ingredient> newIngredients) {
        return new LinkedHashSet<>(Stream.of(ingredients, newIngredients)
                .flatMap(Collection::stream)
                .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Ingredient::getName)))));
    }

}
