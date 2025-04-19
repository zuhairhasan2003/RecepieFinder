package com.example.thematicproject.services;

import com.example.thematicproject.models.Ingredient;
import com.example.thematicproject.repositories.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    @Autowired
    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public List<Ingredient> getAllIngredients() {
        return ingredientRepository.findAll(); // <- returnerer alle ingredienser
    }

    public Optional<Ingredient> getIngredientById(Long id) {
        return ingredientRepository.findById(id);
    }

    public Ingredient saveIngredient(Ingredient ingredient) {
        return ingredientRepository.save(ingredient);
    }

    public void deleteIngredient(Long id) {
        ingredientRepository.deleteById(id);
    }

    public List<Ingredient> saveAllIngredients(List<Ingredient> ingredients) {
        for (Ingredient ingredient : ingredients) {
            Ingredient existing = ingredientRepository.findByName(ingredient.getName());
            if (existing.isEmpty()) {
                ingredientRepository.save(ingredient);
            }
        }
        return ingredients;
    }

    public List<String> getAllIngredientNames() {
            List<Ingredient> ingredients = ingredientRepository.findAll();
            return ingredients.stream()
                    .map(Ingredient::getName)  // Assuming Ingredient has a 'name' field
                    .collect(Collectors.toList());
    }
}
