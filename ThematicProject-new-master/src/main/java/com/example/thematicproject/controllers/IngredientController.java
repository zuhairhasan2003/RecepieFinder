package com.example.thematicproject.controllers;

import com.example.thematicproject.models.Ingredient;
import com.example.thematicproject.models.Recipe;
import com.example.thematicproject.repositories.IngredientRepository;
import com.example.thematicproject.services.IngredientService;
import com.example.thematicproject.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*") // Tillader frontend kald fra browseren
@RestController
@RequestMapping("/ingredients")
public class IngredientController {

    @Autowired
    private RecipeService recipeService;

    private final IngredientService ingredientService;

    private final IngredientRepository ingredientRepository;

    public IngredientController(IngredientService ingredientService, RecipeService recipeService, IngredientRepository ingredientRepository) {
        this.ingredientService = ingredientService;
        this.recipeService = recipeService;
        this.ingredientRepository = ingredientRepository;
    }



    @GetMapping("/{id}")
    public ResponseEntity<Ingredient> getIngredientById(@PathVariable Long id) {
        return ingredientService.getIngredientById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Ingredient createIngredient(@RequestBody Ingredient ingredient) {
        return ingredientService.saveIngredient(ingredient);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable Long id) {
        ingredientService.deleteIngredient(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Ingredient>> getAllIngredients() {
        List<Ingredient> ingredients = ingredientService.getAllIngredients();
        return ResponseEntity.ok(ingredients);
    }

    @PostMapping("/find")
    public ResponseEntity<List<Recipe>> findRecipes(@RequestBody IngredientRequest request) {
        List<String> ingredients = request.getIngredients();
        if (ingredients == null || ingredients.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<Recipe> recipes = recipeService.findRecipesByIngredients(ingredients);
        return new ResponseEntity<>(recipes, HttpStatus.OK);
    }



    @PostMapping("/bulk")
    public ResponseEntity<List<Ingredient>> createIngredients(@RequestBody List<Ingredient> ingredients) {
        if (ingredients == null || ingredients.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<Ingredient> saved = ingredientService.saveAllIngredients(ingredients);
        return ResponseEntity.ok(saved);
    }

    public List<Ingredient> saveAllIngredients(List<Ingredient> ingredients) {
        List<Ingredient> savedIngredients = new ArrayList<>();

        for (Ingredient ingredient : ingredients) {
            Optional<Ingredient> existing = Optional.ofNullable(ingredientRepository.findByName(ingredient.getName()));
            if (existing.isPresent()) {
                savedIngredients.add(existing.get());
            } else {
                savedIngredients.add(ingredientRepository.save(ingredient));
            }
        }

        return savedIngredients;
    }


    @GetMapping("/getrecipebyingredientname")
    public ResponseEntity<List<Recipe>> getRecipesByIngredients(
            @RequestParam List<String> ingredientNames) {
        List<Recipe> recipes = recipeService.findRecipesByIngredientNames(ingredientNames);
        return ResponseEntity.ok(recipes);
    }

    @PostMapping("/createrecipe")
    public ResponseEntity<Recipe> createRecipe(@RequestBody RecipeRequest request) {
        Recipe recipe = recipeService.createRecipeWithIngredients(request.getName(), request.getIngredients());
        return new ResponseEntity<>(recipe, HttpStatus.CREATED);
    }

    @GetMapping("/names")
    public ResponseEntity<List<String>> getAllIngredientNames() {
        List<String> ingredientNames = ingredientService.getAllIngredientNames();
        return ResponseEntity.ok(ingredientNames);
    }
    @PostMapping("/findRecipes")
    public ResponseEntity<List<Recipe>> findRecipesByIngredients(@RequestBody List<String> ingredients) {
        // Call a service to get recipes based on ingredient names
        if (ingredients == null || ingredients.isEmpty()) {
            return ResponseEntity.badRequest().build(); // If no ingredients provided, return bad request
        }

        List<Recipe> recipes = recipeService.findRecipesByIngredients(ingredients);

        if (recipes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // No recipes found
        }

        return ResponseEntity.ok(recipes); // Return recipes as the response
    }


}
