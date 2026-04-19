package com.bitbites.bitbites2.backend.RecipeTest;

import com.bitbites.bitbites2.backend.groceries.GroceryItem;
import com.bitbites.bitbites2.backend.groceries.Ingredient;
import com.bitbites.bitbites2.backend.groceries.Unit;
import com.bitbites.bitbites2.backend.recipes.Recipe;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

// Clasa concreta dummy pt a testa clasa abstracta Recipe
class TestRecipe extends Recipe {
    public TestRecipe(String name, String category, String kitchen, URL url, double kcal, int servings) {
        super(name, category, kitchen, url, kcal, servings);
    }
}

public class RecipeTest {

    private URL getTestUrl() {
        try {
            return new URL("https://bitbites.com/recipe");
        } catch (MalformedURLException e) {
            return null;
        }
    }

    // a) Partitionare de echivalenta
    /*
       Partitii:
       P1: Unitati de timp valide
       P2: Unitati de timp invalide
     */

    @Test
    public void testSetDurationValidUnits() {
        TestRecipe recipe = new TestRecipe("Pasta", "Main", "Italian", getTestUrl(), 500, 2);

        recipe.setDuration(1, "h");
        assertEquals(Duration.ofHours(1), recipe.getDuration());

        recipe.setDuration(30, "minutes");
        assertEquals(Duration.ofMinutes(30), recipe.getDuration());
    }

    @Test
    public void testSetDurationInvalidUnits() {
        TestRecipe recipe = new TestRecipe("Pasta", "Main", "Italian", getTestUrl(), 500, 2);
        recipe.setDuration(10, "days"); // P2
        assertNull(recipe.getDuration());
    }

    // b) Boundary Value Analysis
    /*
       Limite:
       - Cantitate 0 pentru durata
       - Case sensitivity (H vs h)
     */

    @Test
    public void testSetDurationZeroQtty() {
        TestRecipe recipe = new TestRecipe("Pasta", "Main", "Italian", getTestUrl(), 500, 2);
        recipe.setDuration(0, "h");
        assertEquals(Duration.ZERO, recipe.getDuration());
    }

    @Test
    public void testSetDurationUpperCase() {
        TestRecipe recipe = new TestRecipe("Pasta", "Main", "Italian", getTestUrl(), 500, 2);
        recipe.setDuration(1, "HOURS");
        assertEquals(Duration.ofHours(1), recipe.getDuration());
    }

    // c) Category Partitioning
    /*
       Parametru 'unit':
       - "hours", "hour", "h"
       - "minutes", "minute", "m"
       - "seconds", "second", "s"
       - Altele
     */

    @Test
    public void testSetDurationAllCats() {
        TestRecipe recipe = new TestRecipe("Pasta", "Main", "Italian", getTestUrl(), 500, 2);

        recipe.setDuration(1, "hour");
        assertTrue(recipe.getDuration().toHours() == 1);

        recipe.setDuration(1, "minute");
        assertTrue(recipe.getDuration().toMinutes() == 1);

        recipe.setDuration(1, "s");
        assertTrue(recipe.getDuration().toSeconds() == 1);
    }

    @Test
    public void testAddIngredientAddToIngredients() {
        TestRecipe recipe = new TestRecipe("Pasta", "Main", "Italian", getTestUrl(), 500, 2);
        Ingredient i = new Ingredient("Sare", "Condiment");
        recipe.addIngredient(new GroceryItem(10, Unit.GRAM, i));

        assertEquals(1, recipe.getIngredients().getItems().size());
    }

    @Test
    public void testToStringFormatting() {
        TestRecipe recipe = new TestRecipe("Oua", "Mic Dejun", "Universal", getTestUrl(), 200, 1);
        String out = recipe.toString();

        assertTrue(out.contains("Name: Oua"));
        assertTrue(out.contains("Category: Mic Dejun"));
    }

    // Statement Coverage
    @Test
    public void TestStmt_setDuration() {
        TestRecipe recipe = new TestRecipe("Test", "Cat", "Type", getTestUrl(), 100, 1);
        recipe.setDuration(1, "h"); // Covers Case 1
        recipe.setDuration(1, "m"); // Covers Case 2
        recipe.setDuration(1, "s"); // Covers Case 3
        recipe.setDuration(1, "x"); // Covers Default
    }

    // Decision Coverage
    @Test
    public void TestDec_setDuration() {
        TestRecipe recipe = new TestRecipe("Test", "Cat", "Type", getTestUrl(), 100, 1);
        // Decizia switch-ului: intra pe o ramura valida
        recipe.setDuration(1, "h");
        assertNotNull(recipe.getDuration());

        // Decizia switch-ului: intra pe default
        recipe.setDuration(1, "unknown");
        assertNull(recipe.getDuration());
    }

    // Cyclomatic Complexity pentru setDuration(double quantity, String unit)
    /*
       V(G) = P + 1
       Puncte de decizie:
       1. Switch-ul evalueaza 'unit'
       Ramuri:
       - Case 1 ("hours", "hour", "h")
       - Case 2 ("minutes", "minute", "m")
       - Case 3 ("seconds", "second", "s")
       - Default

       V(G) = 3 + 1 = 4
     */
    @Test
    public void TestCirc_setDuration() {
        TestRecipe recipe = new TestRecipe("Test", "Cat", "Type", getTestUrl(), 100, 1);
        // C1: Case Hours
        recipe.setDuration(1, "h");
        // C2: Case Minutes
        recipe.setDuration(1, "m");
        // C3: Case Seconds
        recipe.setDuration(1, "s");
        // C4: Default
        recipe.setDuration(1, "invalid");
    }
}
