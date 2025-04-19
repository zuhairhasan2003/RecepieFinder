package com.example.thematicproject.services;

import com.example.thematicproject.DTO.IngredientDTO;
import com.example.thematicproject.DTO.RecipeDTO;

import com.example.thematicproject.controllers.RecipeRequest;
import com.example.thematicproject.models.Ingredient;
import com.example.thematicproject.models.Recipe;
import com.example.thematicproject.repositories.IngredientRepository;
import com.example.thematicproject.repositories.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

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

    public Recipe createRecipe(RecipeDTO recipeDTO) {
        Recipe recipe = new Recipe();
        recipe.setName(recipeDTO.getName());

        // Convert the list of IngredientDTO to Ingredient
        List<Ingredient> ingredients = convertIngredientDTOToIngredient(recipeDTO.getIngredients());
        recipe.setIngredients(ingredients); // Set the ingredients in the Recipe

        return recipeRepository.save(recipe);
    }

    public List<Recipe> getRecipeByIngredientName(String ingredientName) {
        return recipeRepository.findByIngredients_NameIgnoreCase(ingredientName);
    }

    public Recipe createRecipeWithIngredients(String name, List<String> ingredientNames) {
        Recipe recipe = new Recipe();
        recipe.setName(name);

        List<Ingredient> ingredients = new ArrayList<>();

        for (String ingredientName : ingredientNames) {
            Ingredient ingredient = ingredientRepository.findByName(ingredientName);

            if (ingredient == null) {
                ingredient = new Ingredient();
                ingredient.setName(ingredientName);
                ingredient = ingredientRepository.save(ingredient);
            }

            ingredients.add(ingredient);
        }

        recipe.setIngredients(ingredients);
        return recipeRepository.save(recipe); // This saves the recipe and the join table
    }

    public List<Recipe> createRecipesWithIngredients(List<RecipeRequest> requests) {
        List<Recipe> result = new ArrayList<>();

        for (RecipeRequest req : requests) {
            // 1) Ensure each Ingredient exists or save it
            List<Ingredient> ingredients = req.getIngredients().stream()
                    .map(name -> {
                        Ingredient existing = ingredientRepository.findByName(name);
                        if (existing != null)
                            return existing;
                        Ingredient created = new Ingredient();
                        created.setName(name);
                        return ingredientRepository.save(created);
                    })
                    .collect(Collectors.toList());

            // 2) Create and save the Recipe
            Recipe r = new Recipe();
            r.setName(req.getName());
            r.setIngredients(ingredients);
            result.add(recipeRepository.save(r));
        }

        return result;
    }

    public List<Recipe> saveAll(List<Recipe> recipes) {
        // Ensure that all recipes are saved, and check for errors during saving
        try {
            return recipeRepository.saveAll(recipes); // Use the repository to save recipes
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error saving recipes");
        }
    }

    public List<Recipe> findRecipesByIngredients(List<String> ingredientNames) {
        List<Recipe> recipes = recipeRepository.findByIngredients_NameIn(ingredientNames);
        System.out.println("Found Recipes: " + recipes); // Debug log
        return recipes;
    }

    public List<Recipe> findRecipesByIngredientNames(List<String> ingredientNames) {
        return recipeRepository.findRecipesByIngredientNames(ingredientNames);
    }

}
