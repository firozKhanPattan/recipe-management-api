package com.nl.recipeManagementAPI.exception;

import com.nl.recipeManagementAPI.model.ErrorDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

/**
 * Global Exception Handler for the recipe management api.
 * <p>
 * This class handles all exceptions thrown by controllers and provides
 * meaningful error responses.
 * </p>
 *
 * <h2>Supported Exceptions:</h2>
 * <ul>
 *     <li>MethodArgumentNotValidException - Handles validation errors</li>
 *     <li>RecipeExistsException - </li>
 *     <li>RecipeNotFoundException - Handles missing resources</li>
 *     <li>Generic Exception - Catches all unhandled exceptions</li>
 * </ul>
 *
 * @author Firoz
 * @version 0.0.1
 */
@Slf4j
@RestControllerAdvice
public class RecipeManagementControllerAdvice {

    private static final String EXCEPTION_OCCURRED = "Exception occurred :{}";

    @ExceptionHandler(RecipeNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleRecipeNotFoundException(RecipeNotFoundException exception,
                                                                      WebRequest webRequest) {
        log.error(EXCEPTION_OCCURRED, exception.getMessage(), exception);
        return new ResponseEntity<>(getErrorDetails(exception.getMessage(), webRequest), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RecipeExistsException.class)
    public ResponseEntity<ErrorDetails> handleRecipeExistsException(RecipeExistsException exception, WebRequest webRequest) {
        log.error(EXCEPTION_OCCURRED, exception.getMessage(), exception);
        return new ResponseEntity<>(getErrorDetails(exception.getMessage(), webRequest), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(MethodArgumentNotValidException exception, WebRequest webRequest) {
        log.error(EXCEPTION_OCCURRED, exception.getMessage(), exception);
        return ResponseEntity.badRequest().body(getErrorDetails(exception.getMessage(), webRequest));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(Exception exception,
                                                              WebRequest webRequest) {

        log.error(EXCEPTION_OCCURRED, exception.getMessage(), exception);
        return new ResponseEntity<>(getErrorDetails(exception.getMessage(), webRequest), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ErrorDetails getErrorDetails(String message, WebRequest webRequest) {
        return new ErrorDetails(LocalDateTime.now(), message, webRequest.getDescription(false));
    }
}
