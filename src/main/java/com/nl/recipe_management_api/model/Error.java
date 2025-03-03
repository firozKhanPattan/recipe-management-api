package com.nl.recipe_management_api.model;

import lombok.Data;

import java.util.Date;

@Data
public class Error {
        private Date timestamp;
        private String message;
        private String details;
}
