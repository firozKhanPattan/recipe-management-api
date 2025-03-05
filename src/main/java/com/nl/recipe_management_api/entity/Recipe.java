package com.nl.recipe_management_api.entity;

import com.nl.recipe_management_api.enums.Category;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Recipe {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false,unique = true)
    private String recipeName;


    @Enumerated(EnumType.STRING)
    private Category category;


    private String instructions;


    private int servings;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "recipe_ingredient",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    private Set<Ingredient> ingredients;

}
