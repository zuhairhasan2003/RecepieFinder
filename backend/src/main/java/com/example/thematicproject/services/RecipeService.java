package com.example.thematicproject.services;

import com.example.thematicproject.DTO.IngredientDTO;
import com.example.thematicproject.DTO.RecipeDTO;

import com.example.thematicproject.models.Ingredient;
import com.example.thematicproject.models.Recipe;
import com.example.thematicproject.repositories.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    // Fetch all recipes
    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    // Fetch a recipe by ID
    public Optional<Recipe> getRecipeById(Long id) {
        return recipeRepository.findById(id);
    }

    // Save or update a recipe
    public Recipe saveRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    // Delete a recipe by ID
    public boolean deleteRecipe(Long id) {
        if (recipeRepository.existsById(id)) {
            recipeRepository.deleteById(id);
            return true; // Return true if recipe was successfully deleted
        }
        return false; // Return false if recipe was not found
    }

    // Convert IngredientDTO to Ingredient
    private List<Ingredient> convertIngredientDTOToIngredient(List<IngredientDTO> ingredientDTOs) {
        List<Ingredient> ingredients = new ArrayList<>();
        for (IngredientDTO ingredientDTO : ingredientDTOs) {
            Ingredient ingredient = new Ingredient();
            ingredient.setName(ingredientDTO.getName());
            // Set other properties of Ingredient as needed
            ingredients.add(ingredient);
        }
        return ingredients;
    }



    public List<Recipe> findRecipesByIngredients(List<String> ingredients) {
        // This is a pseudo-logic, adjust it based on your actual repository and query.
        return recipeRepository.findAll().stream()
                .filter(recipe -> recipe.getIngredients().containsAll(ingredients))
                .collect(Collectors.toList());
    }

    public Recipe createRecipe(RecipeDTO recipeDTO) {
        Recipe recipe = new Recipe();
        recipe.setName(recipeDTO.getName());

        // Convert the list of IngredientDTO to Ingredient
        List<Ingredient> ingredients = convertIngredientDTOToIngredient(recipeDTO.getIngredients());
        recipe.setIngredients(ingredients); // Set the ingredients in the Recipe

        return recipeRepository.save(recipe);
    }

}
