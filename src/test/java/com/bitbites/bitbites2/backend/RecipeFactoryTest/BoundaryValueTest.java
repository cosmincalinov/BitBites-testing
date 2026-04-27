package com.bitbites.bitbites2.backend.RecipeFactoryTest;

import com.bitbites.bitbites2.backend.recipes.Recipe;
import com.bitbites.bitbites2.backend.recipes.RecipeFactory;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Analiza valorilor de frontiera pentru
 * RecipeFactory.createCustomRecipe(...)
 *
 * Valori de frontiera:
 *
 *   name (lungime, domeniu [1, 100]):
 *     BV1:  lungime 0 (gol)      -> exceptie
 *     BV2:  lungime 1            -> valid
 *     BV3:  lungime 100          -> valid
 *     BV4:  lungime 101          -> exceptie
 *
 *   kilocalories (domeniu (0, 5000]):
 *     BV5:  0.0                  -> exceptie
 *     BV6:  0.001 (just above 0) -> valid
 *     BV7:  5000.0 (la limita)   -> valid
 *     BV8:  5000.001             -> exceptie
 *
 *   servings (domeniu [1, 50]):
 *     BV9:  0                    -> exceptie
 *     BV10: 1 (minim)            -> valid
 *     BV11: 50 (maxim)           -> valid
 *     BV12: 51                   -> exceptie
 *
 *   Dessert: cap intern la kcal * 1.15 > 5000:
 *     BV13: kcal = 4347 -> 4347 * 1.15 = 4999.05 < 5000  -> fara cap
 *     BV14: kcal = 4348 -> 4348 * 1.15 = 5000.2  > 5000  -> cap la 5000
 */
public class BoundaryValueTest {

    // BV1: name lungime 0 (gol)
    @Test
    public void bv1_nameEmpty_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                RecipeFactory.createCustomRecipe("", "Soup", "Italian", 300, 4));
    }

    // BV2: name lungime 1
    @Test
    public void bv2_nameLength1_recipeCreated() {
        Recipe r = RecipeFactory.createCustomRecipe("A", "Soup", "Italian", 300, 4);
        assertNotNull(r);
        assertEquals("A", r.getName());
    }

    // BV3: name lungime 100
    @Test
    public void bv3_nameLength100_recipeCreated() {
        String name = "A".repeat(100);
        Recipe r = RecipeFactory.createCustomRecipe(name, "Soup", "Italian", 300, 4);
        assertNotNull(r);
        assertEquals(name, r.getName());
    }

    // BV4: name lungime 101
    @Test
    public void bv4_nameLength101_throwsException() {
        String name = "A".repeat(101);
        assertThrows(IllegalArgumentException.class, () ->
                RecipeFactory.createCustomRecipe(name, "Soup", "Italian", 300, 4));
    }

    // BV5: kilocalories = 0
    @Test
    public void bv5_kilocaloriesZero_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                RecipeFactory.createCustomRecipe("Soup", "Soup", "Italian", 0.0, 4));
    }

    // BV6: kilocalories = 0.001 (just above 0)
    @Test
    public void bv6_kilocaloriesJustAboveZero_recipeCreated() {
        Recipe r = RecipeFactory.createCustomRecipe("Soup", "Soup", "Italian", 0.001, 4);
        assertNotNull(r);
        assertTrue(r.getKilocalories() > 0);
    }

    // BV7: kilocalories = 5000 (la limita)
    @Test
    public void bv7_kilocaloriesAtMax_recipeCreated() {
        Recipe r = RecipeFactory.createCustomRecipe("Dish", "MainCourse", "Italian", 5000.0, 2);
        assertNotNull(r);
        assertEquals(5000.0, r.getKilocalories(), 0.001);
    }

    // BV8: kilocalories = 5000.001 (just above 5000)
    @Test
    public void bv8_kilocaloriesJustAboveMax_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                RecipeFactory.createCustomRecipe("Dish", "MainCourse", "Italian", 5000.001, 2));
    }

    // BV9: servings = 0
    @Test
    public void bv9_servingsZero_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                RecipeFactory.createCustomRecipe("Soup", "Soup", "Italian", 300, 0));
    }

    // BV10: servings = 1 (minim valid)
    @Test
    public void bv10_servingsMin_recipeCreated() {
        Recipe r = RecipeFactory.createCustomRecipe("Soup", "Soup", "Italian", 300, 1);
        assertNotNull(r);
        assertEquals(1, r.getServings());
    }

    // BV11: servings = 50 (maxim valid)
    @Test
    public void bv11_servingsMax_recipeCreated() {
        Recipe r = RecipeFactory.createCustomRecipe("Soup", "Soup", "Italian", 300, 50);
        assertNotNull(r);
        assertEquals(50, r.getServings());
    }

    // BV12: servings = 51
    @Test
    public void bv12_servingsAboveMax_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                RecipeFactory.createCustomRecipe("Soup", "Soup", "Italian", 300, 51));
    }

    // BV13: Dessert kcal * 1.15 = 4999.05 → sub 5000, fara cap
    @Test
    public void bv13_dessertKcalJustBelowCap_noCap() {
        // 4347 * 1.15 = 4999.05
        Recipe r = RecipeFactory.createCustomRecipe("Cake", "Dessert", "French", 4347, 4);
        assertNotNull(r);
        assertEquals(4347 * 1.15, r.getKilocalories(), 0.01);
        assertTrue(r.getKilocalories() < 5000);
    }

    // BV14: Dessert kcal * 1.15 = 5000.2 → peste 5000, cap la 5000
    @Test
    public void bv14_dessertKcalJustAboveCap_cappedAt5000() {
        // 4348 * 1.15 = 5000.2
        Recipe r = RecipeFactory.createCustomRecipe("Cake", "Dessert", "French", 4348, 4);
        assertNotNull(r);
        assertEquals(5000.0, r.getKilocalories(), 0.001);
    }
}
