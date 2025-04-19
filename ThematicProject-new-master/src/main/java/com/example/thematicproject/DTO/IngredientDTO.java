package com.example.thematicproject.DTO;

public class IngredientDTO {

    private String name;  // Ingredient name
    private String quantity;  // Quantity of the ingredient (e.g., "1 cup", "2 tbsp", etc.)

    // Constructor
    public IngredientDTO() {}

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
