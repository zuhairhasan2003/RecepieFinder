package com.example.thematicproject.repositories;


import com.example.thematicproject.models.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    Ingredient findByName(String name);


    List<Ingredient> findByNameIn(List<String> ingredientNames);


}

