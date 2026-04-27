package com.bitbites.bitbites2.backend.RecipeFactoryTest;

import com.bitbites.bitbites2.backend.recipes.Recipe;
import com.bitbites.bitbites2.backend.recipes.RecipeFactory;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testare prin partitionare in clase de echivalenta pentru
 * RecipeFactory.createCustomRecipe(String name, String categoryFood,
 *                                   String kitchenType, double kilocalories, int servings)
 *
 * Clase de echivalenta:
 *
 *   name:
 *     EP1: null                           -> exceptie
 *     EP2: sir gol / spatii              -> exceptie
 *     EP3: sir valid (1-100 caractere)   -> valid
 *     EP4: sir > 100 caractere           -> exceptie
 *
 *   categoryFood:
 *     EP5:  null                          -> exceptie
 *     EP6:  sir gol / spatii             -> exceptie
 *     EP7:  categorie valida (ex. "Soup")-> valid
 *     EP8:  alias valid (ex. "Starter")  -> mapat la "Appetizer"
 *     EP9:  categorie invalida           -> exceptie
 *
 *   kitchenType:
 *     EP10: null                          -> exceptie
 *     EP11: sir gol / spatii             -> exceptie
 *     EP12: sir valid                    -> valid
 *
 *   kilocalories:
 *     EP13: <= 0                          -> exceptie
 *     EP14: (0, 5000]                     -> valid
 *     EP15: > 5000                        -> exceptie
 *
 *   servings:
 *     EP16: < 1                           -> exceptie
 *     EP17: [1, 50]                       -> valid
 *     EP18: > 50                          -> exceptie
 */
public class EquivalencePartitioningTest {

    // EP1: name null
    @Test
    public void ep1_nameNull_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                RecipeFactory.createCustomRecipe(null, "Soup", "Italian", 300, 4));
    }

    // EP2: name blank
    @Test
    public void ep2_nameBlank_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                RecipeFactory.createCustomRecipe("   ", "Soup", "Italian", 300, 4));
    }

    // EP3: name valid
    @Test
    public void ep3_nameValid_recipeCreated() {
        Recipe r = RecipeFactory.createCustomRecipe("Minestrone", "Soup", "Italian", 300, 4);
        assertNotNull(r);
        assertEquals("Minestrone", r.getName());
    }

    // EP4: name > 100 caractere
    @Test
    public void ep4_nameTooLong_throwsException() {
        String longName = "A".repeat(101);
        assertThrows(IllegalArgumentException.class, () ->
                RecipeFactory.createCustomRecipe(longName, "Soup", "Italian", 300, 4));
    }

    // EP5: categoryFood null
    @Test
    public void ep5_categoryNull_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                RecipeFactory.createCustomRecipe("Soup", null, "Italian", 300, 4));
    }

    // EP6: categoryFood blank
    @Test
    public void ep6_categoryBlank_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                RecipeFactory.createCustomRecipe("Soup", "  ", "Italian", 300, 4));
    }

    // EP7: categoryFood valida
    @Test
    public void ep7_categoryValid_recipeCreated() {
        Recipe r = RecipeFactory.createCustomRecipe("Tomato Soup", "Soup", "Italian", 300, 4);
        assertNotNull(r);
        assertEquals("Soup", r.getCategoryFood());
    }

    // EP8: categoryFood alias -> mapat la "Appetizer"
    @Test
    public void ep8_categoryAlias_mappedCorrectly() {
        Recipe r = RecipeFactory.createCustomRecipe("Spring Rolls", "Starter", "Asian", 150, 2);
        assertNotNull(r);
        assertEquals("Appetizer", r.getCategoryFood());
    }

    // EP9: categoryFood invalida
    @Test
    public void ep9_categoryInvalid_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                RecipeFactory.createCustomRecipe("Tacos", "Pizza", "Italian", 600, 2));
    }

    // EP10: kitchenType null
    @Test
    public void ep10_kitchenTypeNull_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                RecipeFactory.createCustomRecipe("Pasta", "MainCourse", null, 500, 2));
    }

    // EP11: kitchenType blank
    @Test
    public void ep11_kitchenTypeBlank_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                RecipeFactory.createCustomRecipe("Pasta", "MainCourse", "  ", 500, 2));
    }

    // EP12: kitchenType valid
    @Test
    public void ep12_kitchenTypeValid_recipeCreated() {
        Recipe r = RecipeFactory.createCustomRecipe("Pasta", "MainCourse", "Italian", 500, 2);
        assertNotNull(r);
        assertEquals("Italian", r.getKitchenType());
    }

    // EP13: kilocalories = 0
    @Test
    public void ep13_kilocaloriesZero_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                RecipeFactory.createCustomRecipe("Pasta", "MainCourse", "Italian", 0, 2));
    }

    // EP14: kilocalories valid
    @Test
    public void ep14_kilocaloriesValid_recipeCreated() {
        Recipe r = RecipeFactory.createCustomRecipe("Pasta", "MainCourse", "Italian", 500, 2);
        assertNotNull(r);
        assertEquals(500.0, r.getKilocalories(), 0.001);
    }

    // EP15: kilocalories > 5000
    @Test
    public void ep15_kilocaloriesTooHigh_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                RecipeFactory.createCustomRecipe("Pasta", "MainCourse", "Italian", 5001, 2));
    }

    // EP16: servings = 0
    @Test
    public void ep16_servingsZero_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                RecipeFactory.createCustomRecipe("Pasta", "MainCourse", "Italian", 500, 0));
    }

    // EP17: servings valid
    @Test
    public void ep17_servingsValid_recipeCreated() {
        Recipe r = RecipeFactory.createCustomRecipe("Pasta", "MainCourse", "Italian", 500, 4);
        assertNotNull(r);
        assertEquals(4, r.getServings());
    }

    // EP18: servings > 50
    @Test
    public void ep18_servingsTooHigh_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                RecipeFactory.createCustomRecipe("Pasta", "MainCourse", "Italian", 500, 51));
    }
}