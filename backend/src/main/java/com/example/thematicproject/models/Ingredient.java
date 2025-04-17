package com.example.thematicproject.models;

import jakarta.persistence.*;

@Entity
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // ✅ Nødvendig for Spring (Jackson)
    public Ingredient() {}

    public Ingredient(String name) {
        this.name = name;
    }

    // ✅ Getters og Setters er nødvendige
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
