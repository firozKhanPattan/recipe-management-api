package com.nl.recipe_management_api.exception;

/**
 * @author Firoz
 *
 * Type DuplicateRecipeException
 *
 * thrown when a user tries to add am already existing recipe with the same name.
 */
public class DuplicateRecipeException extends RuntimeException {
    public DuplicateRecipeException(String message){
        super(message);
    }
}
