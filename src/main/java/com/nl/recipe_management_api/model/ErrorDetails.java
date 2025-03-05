package com.nl.recipe_management_api.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
public class ErrorDetails {
        private LocalDateTime timestamp;
        private String message;
        private String details;
}
