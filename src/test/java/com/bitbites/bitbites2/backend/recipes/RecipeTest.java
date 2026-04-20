package com.bitbites.bitbites2.backend.recipes;

import com.bitbites.bitbites2.backend.groceries.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 tests for Recipe (using concrete subclasses).
 *
 * Strategies:
 * - Equivalence partitioning for setDuration (hours, minutes, seconds, invalid)
 * - Boundary analysis for formatDuration (0 minutes, exactly 1 hour, etc.)
 * - Statement coverage for all subclass constructors
 */
class RecipeTest {

    private URL testUrl;

    @BeforeEach
    void setUp() throws MalformedURLException {
        testUrl = new URL("https://example.com/recipe");
    }

    // ===================== Subclass instantiation =====================

    @Nested
    @DisplayName("Subclass instantiation")
    class SubclassTests {

        @Test
        void appetizerCategory() {
            Recipe r = new Appetizer("Toast", "Romanian", testUrl, 200, 2);
            assertEquals("Appetizer", r.getCategoryFood());
            assertEquals("Toast", r.getName());
        }

        @Test
        void soupCategory() {
            Recipe r = new Soup("Borsch", "Romanian", testUrl, 150, 4);
            assertEquals("Soup", r.getCategoryFood());
        }

        @Test
        void mainCourseCategory() {
            Recipe r = new MainCourse("Steak", "Italian", testUrl, 500, 2);
            assertEquals("MainCourse", r.getCategoryFood());
        }

        @Test
        void dessertCategory() {
            Recipe r = new Dessert("Cake", "French", testUrl, 300, 8);
            assertEquals("Dessert", r.getCategoryFood());
        }

        @Test
        void drinkCategory() {
            Recipe r = new Drink("Smoothie", "American", testUrl, 100, 1);
            assertEquals("Drink", r.getCategoryFood());
        }

        @Test
        void saladCategory() {
            Recipe r = new Salad("Caesar", "Italian", testUrl, 250, 3);
            assertEquals("Salad", r.getCategoryFood());
        }

        @Test
        void breadCategory() {
            Recipe r = new Bread("Baguette", "French", testUrl, 280, 6);
            assertEquals("Bread", r.getCategoryFood());
        }

        @Test
        void constructorWithId() {
            Recipe r = new Appetizer(42, "Toast", "Romanian", testUrl, 200, 2);
            assertEquals(42, r.getId());
            assertEquals("Appetizer", r.getCategoryFood());
        }
    }

    // ===================== Getters =====================

    @Test
    @DisplayName("All getters return correct values")
    void gettersReturnCorrectValues() {
        Recipe r = new MainCourse(10, "Pasta", "Italian", testUrl, 450.5, 4);
        assertEquals(10, r.getId());
        assertEquals("Pasta", r.getName());
        assertEquals("Italian", r.getKitchenType());
        assertEquals(450.5, r.getKilocalories());
        assertEquals(4, r.getServings());
        assertEquals(testUrl, r.getInstructions());
        assertNotNull(r.getIngredients());
    }

    // ===================== addIngredient =====================

    @Nested
    @DisplayName("addIngredient")
    class AddIngredientTests {

        @Test
        void addSingleIngredient() {
            Recipe r = new Soup("Borsch", "Romanian", testUrl, 150, 4);
            Ingredient carrot = new Ingredient("carrot", "vegetable");
            r.addIngredient(new GroceryItem(200, Unit.GRAM, carrot));

            assertEquals(1, r.getIngredients().getItems().size());
        }

        @Test
        void addMultipleIngredients() {
            Recipe r = new Soup("Borsch", "Romanian", testUrl, 150, 4);
            r.addIngredient(new GroceryItem(200, Unit.GRAM, new Ingredient("carrot", "vegetable")));
            r.addIngredient(new GroceryItem(100, Unit.GRAM, new Ingredient("onion", "vegetable")));
            r.addIngredient(new GroceryItem(500, Unit.MILLILITER, new Ingredient("water", "liquid")));

            assertEquals(3, r.getIngredients().getItems().size());
        }
    }

    // ===================== setDuration =====================

    @Nested
    @DisplayName("setDuration with string unit")
    class SetDurationTests {

        @Test
        void setDurationHours() {
            Recipe r = new Appetizer("Test", "Test", testUrl, 100, 1);
            r.setDuration(2, "hours");
            assertEquals(Duration.ofHours(2), r.getDuration());
        }

        @Test
        void setDurationHour() {
            Recipe r = new Appetizer("Test", "Test", testUrl, 100, 1);
            r.setDuration(1, "hour");
            assertEquals(Duration.ofHours(1), r.getDuration());
        }

        @Test
        void setDurationH() {
            Recipe r = new Appetizer("Test", "Test", testUrl, 100, 1);
            r.setDuration(3, "h");
            assertEquals(Duration.ofHours(3), r.getDuration());
        }

        @Test
        void setDurationMinutes() {
            Recipe r = new Appetizer("Test", "Test", testUrl, 100, 1);
            r.setDuration(30, "minutes");
            assertEquals(Duration.ofMinutes(30), r.getDuration());
        }

        @Test
        void setDurationMinute() {
            Recipe r = new Appetizer("Test", "Test", testUrl, 100, 1);
            r.setDuration(1, "minute");
            assertEquals(Duration.ofMinutes(1), r.getDuration());
        }

        @Test
        void setDurationM() {
            Recipe r = new Appetizer("Test", "Test", testUrl, 100, 1);
            r.setDuration(45, "m");
            assertEquals(Duration.ofMinutes(45), r.getDuration());
        }

        @Test
        void setDurationSeconds() {
            Recipe r = new Appetizer("Test", "Test", testUrl, 100, 1);
            r.setDuration(90, "seconds");
            assertEquals(Duration.ofSeconds(90), r.getDuration());
        }

        @Test
        void setDurationSecond() {
            Recipe r = new Appetizer("Test", "Test", testUrl, 100, 1);
            r.setDuration(1, "second");
            assertEquals(Duration.ofSeconds(1), r.getDuration());
        }

        @Test
        void setDurationS() {
            Recipe r = new Appetizer("Test", "Test", testUrl, 100, 1);
            r.setDuration(120, "s");
            assertEquals(Duration.ofSeconds(120), r.getDuration());
        }

        @Test
        void setDurationInvalidUnit() {
            Recipe r = new Appetizer("Test", "Test", testUrl, 100, 1);
            r.setDuration(10, "days");
            assertNull(r.getDuration());
        }

        @Test
        void setDurationUpperCase() {
            Recipe r = new Appetizer("Test", "Test", testUrl, 100, 1);
            r.setDuration(2, "HOURS");
            assertEquals(Duration.ofHours(2), r.getDuration());
        }

        @Test
        void setDurationDirectDuration() {
            Recipe r = new Appetizer("Test", "Test", testUrl, 100, 1);
            r.setDuration(Duration.ofMinutes(45));
            assertEquals(Duration.ofMinutes(45), r.getDuration());
        }
    }

    // ===================== formatDuration =====================

    @Nested
    @DisplayName("formatDuration")
    class FormatDurationTests {

        @Test
        void formatHoursAndMinutes() {
            Recipe r = new Appetizer("Test", "Test", testUrl, 100, 1);
            r.setDuration(Duration.ofMinutes(90));
            assertEquals("1 hour 30 minutes", r.formatDuration());
        }

        @Test
        void formatOnlyMinutes() {
            Recipe r = new Appetizer("Test", "Test", testUrl, 100, 1);
            r.setDuration(Duration.ofMinutes(45));
            assertEquals("45 minutes", r.formatDuration());
        }

        @Test
        void formatZeroMinutes() {
            Recipe r = new Appetizer("Test", "Test", testUrl, 100, 1);
            r.setDuration(Duration.ZERO);
            assertEquals("0 minutes", r.formatDuration());
        }

        @Test
        void formatExactlyOneHour() {
            Recipe r = new Appetizer("Test", "Test", testUrl, 100, 1);
            r.setDuration(Duration.ofHours(1));
            assertEquals("1 hour", r.formatDuration());
        }

        @Test
        void formatMultipleHours() {
            Recipe r = new Appetizer("Test", "Test", testUrl, 100, 1);
            r.setDuration(Duration.ofHours(3));
            assertEquals("3 hours", r.formatDuration());
        }

        @Test
        void formatOneMinute() {
            Recipe r = new Appetizer("Test", "Test", testUrl, 100, 1);
            r.setDuration(Duration.ofMinutes(1));
            assertEquals("1 minute", r.formatDuration());
        }
    }

    // ===================== setGroceryList =====================

    @Test
    @DisplayName("setGroceryList replaces ingredients")
    void setGroceryList() {
        Recipe r = new Appetizer("Test", "Test", testUrl, 100, 1);
        GroceryList newList = new GroceryList();
        newList.addItem(new GroceryItem(100, Unit.GRAM, new Ingredient("salt", "spice")));
        r.setGroceryList(newList);
        assertEquals(1, r.getIngredients().getItems().size());
    }

    // ===================== toString =====================

    @Test
    @DisplayName("toString contains name and category")
    void toStringContainsInfo() {
        Recipe r = new Dessert("Tiramisu", "Italian", testUrl, 400, 6);
        r.setDuration(Duration.ofMinutes(30));
        String str = r.toString();
        assertTrue(str.contains("Tiramisu"));
        assertTrue(str.contains("Dessert"));
        assertTrue(str.contains("Italian"));
    }
}
