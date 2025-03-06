package com.nl.recipeManagementAPI.exception;

/**
 * @author Firoz
 * <p>
 * Type RecipeNotFoundException
 * <p>
 * thrown when the requested recipe is not found.
 */
public class RecipeNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public RecipeNotFoundException(String message) {
        super(String.format(message));
    }
}
