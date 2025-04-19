package com.example.thematicproject.repositories;



import com.example.thematicproject.models.Ingredient;
import com.example.thematicproject.models.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    @Query("SELECT r FROM Recipe r JOIN r.ingredients i WHERE i IN :ingredients")
    List<Recipe> findByIngredientsIn(List<Ingredient> ingredients);

    List<Recipe> findByIngredients_NameIgnoreCase(String ingredientName);

    @Query("SELECT r FROM Recipe r JOIN r.ingredients i WHERE i.name IN :ingredientNames")
    List<Recipe> findRecipesByIngredientNames(@Param("ingredientNames") List<String> ingredientNames);

    List<Recipe> findByIngredientsContaining(Ingredient ingredient);

    List<Recipe>findByIngredients_NameIn(List<String> ingredientNames);

}









