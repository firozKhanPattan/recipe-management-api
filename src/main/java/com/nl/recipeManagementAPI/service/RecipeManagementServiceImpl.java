package com.nl.recipeManagementAPI.service;

import com.nl.recipeManagementAPI.entity.Ingredient;
import com.nl.recipeManagementAPI.entity.Recipe;
import com.nl.recipeManagementAPI.exception.RecipeExistsException;
import com.nl.recipeManagementAPI.exception.RecipeNotFoundException;
import com.nl.recipeManagementAPI.mapper.RecipeMapper;
import com.nl.recipeManagementAPI.model.RecipeDetails;
import com.nl.recipeManagementAPI.model.RecipeFilterRequest;
import com.nl.recipeManagementAPI.model.RecipeIngredient;
import com.nl.recipeManagementAPI.repository.RecipeManagementRepository;
import com.nl.recipeManagementAPI.repository.RecipeSearchSpecification;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Firoz
 * Type RecipeManagementServiceImpl
 * Provides the implementation for RecipeManagementService.
 */
@Slf4j
@Service
@AllArgsConstructor
public class RecipeManagementServiceImpl implements RecipeManagementService {

    private final RecipeManagementRepository recipeManagementRepository;

    /**
     * Adds a recipe to the database
     *
     * @param recipeDetails the recipe to be saved
     * @return the created recipeDetails.
     */
    @Override
    public RecipeDetails createRecipe(RecipeDetails recipeDetails) {
        log.info("Create recipe {}", recipeDetails);
        Optional<Recipe> recipe = recipeManagementRepository.findByRecipeName(recipeDetails.getRecipeName().trim().toLowerCase());
        if (recipe.isPresent()) {
            throw new RecipeExistsException("Recipe already exists with the name: " + recipeDetails.getRecipeName());
        }
        Recipe newRecipe = RecipeMapper.INSTANCE.toRecipe(recipeDetails);
        Recipe savedRecipe = recipeManagementRepository.save(newRecipe);
        return RecipeMapper.INSTANCE.fromRecipe(savedRecipe);
    }

    /**
     * Updates a Recipe, Gets the existing recipe by id or name and compares the ingredients and other values updates the recipe
     * with the latest details.
     *
     * @param newRecipeDetails The recipe to be updated.
     * @return
     */
    @Override
    public RecipeDetails updateRecipe(Long id, RecipeDetails newRecipeDetails) {
        log.info("Update recipe: {},{}", id, newRecipeDetails);
        Optional<Recipe> recipe = recipeManagementRepository.findById(id);
        if (recipe.isPresent()) {
            log.info("Recipe found, updating the recipe");
            Recipe recipeTobeUpdated = recipe.get();
            if (!CollectionUtils.isEmpty(newRecipeDetails.getIngredients())) {
                recipeTobeUpdated.setIngredients(getDistinctIngredients(recipeTobeUpdated.getIngredients(), newRecipeDetails.getIngredients()));
            }
            recipeTobeUpdated.setRecipeName(newRecipeDetails.getRecipeName());
            recipeTobeUpdated.setCategory(Optional.ofNullable(newRecipeDetails.getCategory()).orElse(recipeTobeUpdated.getCategory()));
            recipeTobeUpdated.setInstructions(Optional.ofNullable(newRecipeDetails.getInstructions()).orElse(recipeTobeUpdated.getInstructions()));
            recipeTobeUpdated.setServings(Optional.ofNullable(newRecipeDetails.getServings()).orElse(recipeTobeUpdated.getServings()));
            Recipe updatedRecipe = recipeManagementRepository.save(recipeTobeUpdated);
            return RecipeMapper.INSTANCE.fromRecipe(updatedRecipe);
        } else {
            throw new RecipeNotFoundException("Recipe not found with the name: " + newRecipeDetails.getRecipeName());
        }
    }

    /**
     * Deletes the recipe by id.
     *
     * @param recipeId The recipe Id to be deleted.
     */
    @Override
    public void deleteRecipe(Long recipeId) {
        log.info("delete recipe" + recipeId);
        recipeManagementRepository.findById(recipeId).orElseThrow(() -> new RecipeNotFoundException("Recipe not found with id: " + recipeId));
        recipeManagementRepository.deleteById(recipeId);
    }

    /**
     * Fetches all the recipes that satisfy the filter criteria.
     * The search can be in the combination of Category, servings, include ingredients and exclude ingredients.
     *
     * @param recipeFilterRequest The search criteria
     * @return
     */
    @Override
    public List<RecipeDetails> filterRecipes(RecipeFilterRequest recipeFilterRequest) {
        log.info("filter recipe with criteria {}", recipeFilterRequest);
        Specification<Recipe> specification = RecipeSearchSpecification.filterByCriteria(recipeFilterRequest);
        List<Recipe> recipes = recipeManagementRepository.findAll(specification);
        log.info("Recipes found with the criteria: " + recipes.size());
        return recipes.stream()
                .map(recipe -> RecipeMapper.INSTANCE.fromRecipe(recipe))
                .collect(Collectors.toList());
    }

    /**
     * Gets all the available recipes in the system
     *
     * @return lis to recipes found.
     */
    @Override
    public List<RecipeDetails> getAllRecipes() {
        List<Recipe> recipes = recipeManagementRepository.findAll();
        log.info("get all recipes: total recipes found: " + recipes.size());
        return recipes.stream()
                .map(recipe -> RecipeMapper.INSTANCE.fromRecipe(recipe))
                .collect(Collectors.toList());
    }

    /**
     * Gets recipe by the given name
     * or throws RecipeNotFoundException if not found.
     *
     * @param recipeName
     * @return
     */
    @Override
    public RecipeDetails getRecipe(String recipeName) {
        return RecipeMapper.INSTANCE.fromRecipe(getRecipeByName(recipeName));
    }

    private Recipe getRecipeByName(String name) {
        return recipeManagementRepository.findByRecipeName(name)
                .orElseThrow(() -> new RecipeNotFoundException("No Recipe found with the name: " + name));
    }

    private Set<Ingredient> getDistinctIngredients(Set<Ingredient> ingredients, Set<RecipeIngredient> newIngredients) {
        Set<Ingredient> newIngredientSet = newIngredients.stream()
                .map(recipeIngredient -> new Ingredient(recipeIngredient.getId(), recipeIngredient.getName()))
                .collect(Collectors.toSet());
        return new LinkedHashSet<>(Stream.of(ingredients, newIngredientSet)
                .flatMap(Collection::stream)
                .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Ingredient::getName)))));
    }
}
