package com.example.thematicproject.controllers;

import com.example.thematicproject.DTO.RecipeDTO;
import com.example.thematicproject.models.Recipe;
import com.example.thematicproject.models.User;
import com.example.thematicproject.repositories.RecipeRepository;
import com.example.thematicproject.repositories.UserRepository;
import com.example.thematicproject.services.RecipeService;
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

    private final RecipeService recipeService;
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;

    public RecipeController(RecipeService recipeService, UserRepository userRepository, RecipeRepository recipeRepository) {
        this.recipeService = recipeService;
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<Recipe> addRecipe(@RequestBody RecipeDTO recipeDTO) {
        Recipe newRecipe = recipeService.createRecipe(recipeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newRecipe);
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
            recipes = getDemoRecipes();
        }

        return ResponseEntity.ok(recipes);
    }

    // Example hardcoded recipes to demonstrate functionality when no results found
    private List<Recipe> getDemoRecipes() {
        List<Recipe> demoRecipes = new ArrayList<>();

        demoRecipes.add(new Recipe("Spaghetti Bolognese", Arrays.asList("Spaghetti", "Ground Beef", "Tomato Sauce", "Onion", "Garlic")));
        demoRecipes.add(new Recipe("Vegetable Stir Fry", Arrays.asList("Carrot", "Broccoli", "Soy Sauce", "Tofu", "Garlic")));
        demoRecipes.add(new Recipe("Chicken Salad", Arrays.asList("Chicken", "Lettuce", "Tomatoes", "Cucumber", "Olive Oil")));

        return demoRecipes;
    }
}
