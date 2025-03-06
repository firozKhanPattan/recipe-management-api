package com.nl.recipeManagementAPI.repository;

import com.nl.recipeManagementAPI.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * @author Firoz
 * <p>
 * Type RecipeManagementRepository
 * Repository to manage the recipes connecting to pesristent systems.
 * Extends the JpaSpecificationExecutor and enables to filter or search the recipes based on the custom filters in combination.
 */
public interface RecipeManagementRepository extends JpaRepository<Recipe, Long>, JpaSpecificationExecutor<Recipe> {
    Optional<Recipe> findByRecipeName(String recipeName);
}
