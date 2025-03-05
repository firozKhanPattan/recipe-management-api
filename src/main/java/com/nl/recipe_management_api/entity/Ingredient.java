package com.nl.recipe_management_api.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient {

   @Id
   @GeneratedValue
   private Long id;

  /* @Column(name = "name")*/
   private String name;
}
