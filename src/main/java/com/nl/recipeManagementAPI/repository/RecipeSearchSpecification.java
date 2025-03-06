package com.nl.recipeManagementAPI.repository;

import com.nl.recipeManagementAPI.entity.Ingredient;
import com.nl.recipeManagementAPI.entity.Recipe;
import com.nl.recipeManagementAPI.model.RecipeFilterRequest;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;


/**
 * @author Firoz
 * <p>
 * Type RecipeSearchSpecification
 * <p>
 * builds custom specifications based on the filter criteria provided.
 */
public class RecipeSearchSpecification {
    public static Specification<Recipe> filterByCriteria(RecipeFilterRequest recipeFilterRequest) {

        return (Root<Recipe> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (null != recipeFilterRequest.getCategory()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("category"), recipeFilterRequest.getCategory()));
            }
            if (null != recipeFilterRequest.getServings()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("servings"), recipeFilterRequest.getServings()));
            }
            if (null != recipeFilterRequest.getIncludesIngredient() && !recipeFilterRequest.getIncludesIngredient().isEmpty()) {
                Join<Recipe, Ingredient> includeIngredientsJoin = root.join("ingredients");
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(includeIngredientsJoin.get("name"), recipeFilterRequest.getIncludesIngredient()));
            }
            if (null != recipeFilterRequest.getExcludesIngredient() && !recipeFilterRequest.getExcludesIngredient().isEmpty()) {
                Subquery<Long> subquery = query.subquery(Long.class);
                Root<Recipe> subRoot = subquery.from(Recipe.class);
                Join<Recipe, Ingredient> excludeIngredientsJoin = subRoot.join("ingredients");
                subquery.select(subRoot.get("id")).where(criteriaBuilder.equal(excludeIngredientsJoin.get("name"), recipeFilterRequest.getExcludesIngredient()));
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.not(root.get("id").in(subquery)));
            }
            if (null != recipeFilterRequest.getInstruction() && !recipeFilterRequest.getInstruction().isEmpty()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(criteriaBuilder.lower(root.get("instructions")), "%" + recipeFilterRequest.getInstruction().toLowerCase() + "%"));
            }
            return predicate;
        };
    }
}
