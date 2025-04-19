package com.example.thematicproject.controllers;

import com.example.thematicproject.DTO.RecipeDTO;
import com.example.thematicproject.models.Ingredient;
import com.example.thematicproject.models.Recipe;
import com.example.thematicproject.models.User;
import com.example.thematicproject.repositories.IngredientRepository;
import com.example.thematicproject.repositories.RecipeRepository;
import com.example.thematicproject.repositories.UserRepository;
import com.example.thematicproject.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:63342")
@RestController
@RequestMapping("/api/recipes")
public class RecipeController {
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private IngredientRepository ingredientRepository;

    private final RecipeService recipeService;
    private final UserRepository userRepository;


    public RecipeController(RecipeService recipeService, UserRepository userRepository, RecipeRepository recipeRepository) {
        this.recipeService = recipeService;
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
    }



    @GetMapping
    public List<Recipe> getAllRecipes() {
        return recipeService.getAllRecipes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable Long id) {
        return recipeService.getRecipeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
        boolean isDeleted = recipeService.deleteRecipe(id);
        return isDeleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/{userId}/add-to-favorites")
    public ResponseEntity<String> addRecipeToFavorites(@PathVariable Long userId, @RequestBody Long recipeId) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        if (userOptional.isPresent() && recipeOptional.isPresent()) {
            User user = userOptional.get();
            Recipe recipe = recipeOptional.get();

            user.getFavoriteRecipes().add(recipe);
            userRepository.save(user);

            return ResponseEntity.ok("Recipe added to favorites!");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User or Recipe not found!");
    }

    @DeleteMapping("/{userId}/remove-from-favorites/{recipeId}")
    public ResponseEntity<String> removeRecipeFromFavorites(@PathVariable Long userId, @PathVariable Long recipeId) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        if (userOptional.isPresent() && recipeOptional.isPresent()) {
            User user = userOptional.get();
            Recipe recipe = recipeOptional.get();

            user.getFavoriteRecipes().remove(recipe);
            userRepository.save(user);

            return ResponseEntity.ok("Recipe removed from favorites!");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User or Recipe not found!");
    }

    @GetMapping("/{userId}/favorites")
    public ResponseEntity<ArrayList<Recipe>> getUserFavorites(@PathVariable Long userId) {
        return userRepository.findById(userId)
                .map(user -> ResponseEntity.ok(new ArrayList<>(user.getFavoriteRecipes())))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @PostMapping("/find")
    public ResponseEntity<List<Recipe>> findRecipes(@RequestBody List<String> ingredients) {
        if (ingredients == null || ingredients.isEmpty()) {
            return ResponseEntity.badRequest().body(new ArrayList<>());
        }

        // Call service to find recipes by ingredients
        List<Recipe> recipes = recipeService.findRecipesByIngredients(ingredients);
        if (recipes.isEmpty()) {
            // If no recipes found, return a list of hardcoded recipes for demo purposes

        }

        return ResponseEntity.ok(recipes);
    }



    @PostMapping(consumes = "application/json")
    public ResponseEntity<Recipe> addRecipe(@RequestBody RecipeDTO recipeDTO) {
        Recipe newRecipe = recipeService.createRecipe(recipeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newRecipe);
    }


    @GetMapping("/getrecipebyingredients")
    public ResponseEntity<List<Recipe>> getRecipeByIngredients(@RequestParam List<String> ingredientNames) {
        System.out.println("Ingredient names: " + ingredientNames);  // Debugging output
        List<Ingredient> ingredients = ingredientRepository.findByNameIn(ingredientNames);

        if (ingredients.isEmpty()) {
            System.out.println("No ingredients found!");  // Debugging output
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        List<Recipe> recipes = recipeRepository.findByIngredientsIn(ingredients);
        System.out.println("Found recipes: " + recipes);  // Debugging output
        return ResponseEntity.ok(recipes);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<Recipe>> createRecipes(@RequestBody List<Recipe> recipes) {
        // Save all ingredients that are not already in the database
        for (Recipe recipe : recipes) {
            for (Ingredient ingredient : recipe.getIngredients()) {
                if (ingredient.getId() == null) {
                    // Check if the ingredient already exists by name
                    Optional<Ingredient> existingIngredient = Optional.ofNullable(ingredientRepository.findByName(ingredient.getName()));
                    if (existingIngredient.isEmpty()) {
                        ingredientRepository.save(ingredient);  // Save the ingredient if it's new
                    } else {
                        ingredient.setId(existingIngredient.get().getId());  // Set the existing ingredient's ID if it's found
                    }
                }
            }
        }

        // Save the recipes after the ingredients are saved
        List<Recipe> savedRecipes = recipeService.saveAll(recipes);
        return ResponseEntity.ok(savedRecipes);
    }


    @GetMapping("/search")
    public ResponseEntity<List<Recipe>> searchRecipesByIngredients(@RequestParam List<String> ingredientNames) {
        List<Recipe> recipes = recipeService.findRecipesByIngredients(ingredientNames);

        if (recipes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Map the ingredients to their names before returning the response
        for (Recipe recipe : recipes) {
            for (Ingredient ingredient : recipe.getIngredients()) {
                ingredient.setName(ingredient.getName()); // Make sure ingredient names are included
            }
        }

        return ResponseEntity.ok(recipes);
    }

}


