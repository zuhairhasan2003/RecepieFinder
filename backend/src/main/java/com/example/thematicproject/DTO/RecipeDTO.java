package com.example.thematicproject.DTO;

import java.util.List;

public class RecipeDTO {

    private String name;
    private List<IngredientDTO> ingredients;  // List of IngredientDTO

    // Constructor, Getters, and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<IngredientDTO> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientDTO> ingredients) {
        this.ingredients = ingredients;
    }
}
