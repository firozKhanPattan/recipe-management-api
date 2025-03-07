package com.nl.recipeManagementAPI.exception;

/**
 * @author Firoz
 *
 * Type RecipeExistsException
 *
 * thrown when a user tries to add am already existing recipe with the same name.
 */
public class RecipeExistsException extends RuntimeException {
    public RecipeExistsException(String message){
        super(message);
    }
}
