package com.nl.recipe_management_api.exception;

import com.nl.recipe_management_api.model.ErrorDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class RecipeManagementControllerAdvice {

    @ExceptionHandler(RecipeNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleRecipeNotFoundException(RecipeNotFoundException exception,
                                                                      WebRequest webRequest){
        return new ResponseEntity<>(getErrorDetails(exception.getMessage(),webRequest), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateRecipeException.class)
    public ResponseEntity<ErrorDetails> handleDuplicateRecipeException(DuplicateRecipeException exception,WebRequest webRequest){
        return new ResponseEntity<>(getErrorDetails(exception.getMessage(),webRequest), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(MethodArgumentNotValidException exception, WebRequest webRequest) {
        log.error("Exception Occurred", exception.getMessage(), exception);
        return ResponseEntity.badRequest().body(getErrorDetails(exception.getMessage(), webRequest));
    }

    private ErrorDetails getErrorDetails(String message, WebRequest webRequest) {
        return new ErrorDetails(LocalDateTime.now(),message,webRequest.getDescription(false) );
    }

}
