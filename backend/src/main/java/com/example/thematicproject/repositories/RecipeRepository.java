package com.example.thematicproject.repositories;



import com.example.thematicproject.models.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    @Query("SELECT r FROM Recipe r JOIN r.ingredients i WHERE LOWER(i.name) IN :ingredientNames GROUP BY r.id HAVING COUNT(DISTINCT i.name) = :ingredientCount")
    List<Recipe> findRecipesByIngredientNames(@Param("ingredientNames") List<String> ingredientNames,
                                              @Param("ingredientCount") long ingredientCount);

    @Query("SELECT r FROM Recipe r JOIN r.ingredients i WHERE i.name IN :ingredientNames GROUP BY r.id HAVING COUNT(DISTINCT i.name) = :ingredientCount")
    List<Recipe> findRecipesByAllIngredientNames(@Param("ingredientNames") List<String> ingredientNames, @Param("ingredientCount") long ingredientCount);
}




