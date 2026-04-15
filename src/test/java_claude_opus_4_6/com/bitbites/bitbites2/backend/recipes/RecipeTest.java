package com.bitbites.bitbites2.backend.recipes;

import com.bitbites.bitbites2.backend.groceries.GroceryItem;
import com.bitbites.bitbites2.backend.groceries.GroceryList;
import com.bitbites.bitbites2.backend.groceries.Ingredient;
import com.bitbites.bitbites2.backend.groceries.Unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Recipe Tests")
class RecipeTest {

    private URL sampleUrl;
    private Recipe recipe;

    @BeforeEach
    void setUp() throws MalformedURLException {
        sampleUrl = new URL("https://example.com/recipe");
        // Use MainCourse as concrete subclass to test abstract Recipe
        recipe = new MainCourse("Pasta Carbonara", "Italian", sampleUrl, 550.0, 4);
    }

    // ==================== Constructor ====================

    @Nested
    @DisplayName("Constructor")
    class ConstructorTests {

        @Test
        @DisplayName("Constructor sets name correctly")
        void nameIsSet() {
            assertEquals("Pasta Carbonara", recipe.getName());
        }

        @Test
        @DisplayName("Constructor sets categoryFood correctly")
        void categoryFoodIsSet() {
            assertEquals("MainCourse", recipe.getCategoryFood());
        }

        @Test
        @DisplayName("Constructor sets kitchenType correctly")
        void kitchenTypeIsSet() {
            assertEquals("Italian", recipe.getKitchenType());
        }

        @Test
        @DisplayName("Constructor sets kilocalories correctly")
        void kilocaloriesIsSet() {
            assertEquals(550.0, recipe.getKilocalories(), 1e-9);
        }

        @Test
        @DisplayName("Constructor sets servings correctly")
        void servingsIsSet() {
            assertEquals(4, recipe.getServings());
        }

        @Test
        @DisplayName("Constructor sets instructions URL correctly")
        void instructionsIsSet() {
            assertEquals(sampleUrl, recipe.getInstructions());
        }

        @Test
        @DisplayName("Constructor initializes empty ingredients list")
        void ingredientsInitialized() {
            assertNotNull(recipe.getIngredients());
            assertTrue(recipe.getIngredients().getItems().isEmpty());
        }

        @Test
        @DisplayName("Constructor with id sets id correctly")
        void idConstructor() throws MalformedURLException {
            Recipe r = new MainCourse(42, "Test", "Italian", sampleUrl, 100.0, 2);
            assertEquals(42, r.getId());
            assertEquals("Test", r.getName());
        }
    }

    // ==================== addIngredient ====================

    @Nested
    @DisplayName("addIngredient()")
    class AddIngredientTests {

        @Test
        @DisplayName("Add single ingredient")
        void addSingleIngredient() {
            Ingredient pasta = new Ingredient("pasta", "dry");
            GroceryItem item = new GroceryItem(500.0, Unit.GRAM, pasta);

            recipe.addIngredient(item);

            assertEquals(1, recipe.getIngredients().getItems().size());
        }

        @Test
        @DisplayName("Add multiple ingredients")
        void addMultipleIngredients() {
            recipe.addIngredient(new GroceryItem(500.0, Unit.GRAM, new Ingredient("pasta", "dry")));
            recipe.addIngredient(new GroceryItem(200.0, Unit.GRAM, new Ingredient("bacon", "meat")));
            recipe.addIngredient(new GroceryItem(4.0, Unit.PIECE, new Ingredient("eggs", "other")));

            assertEquals(3, recipe.getIngredients().getItems().size());
        }

        @Test
        @DisplayName("Add duplicate ingredient merges quantities")
        void addDuplicateIngredientMerges() {
            Ingredient pasta = new Ingredient("pasta", "dry");
            recipe.addIngredient(new GroceryItem(200.0, Unit.GRAM, pasta));
            recipe.addIngredient(new GroceryItem(300.0, Unit.GRAM, pasta));

            assertEquals(1, recipe.getIngredients().getItems().size());
            assertEquals(500.0, recipe.getIngredients().getItems().get(pasta).quantity(), 1e-9);
        }
    }

    // ==================== setDuration with String ====================

    @Nested
    @DisplayName("setDuration(double, String)")
    class SetDurationStringTests {

        @Test
        @DisplayName("Set duration in hours")
        void setDurationHours() {
            recipe.setDuration(2.0, "hours");
            assertEquals(Duration.ofHours(2), recipe.getDuration());
        }

        @Test
        @DisplayName("Set duration with 'hour' singular")
        void setDurationHourSingular() {
            recipe.setDuration(1.0, "hour");
            assertEquals(Duration.ofHours(1), recipe.getDuration());
        }

        @Test
        @DisplayName("Set duration with 'h' abbreviation")
        void setDurationH() {
            recipe.setDuration(3.0, "h");
            assertEquals(Duration.ofHours(3), recipe.getDuration());
        }

        @Test
        @DisplayName("Set duration in minutes")
        void setDurationMinutes() {
            recipe.setDuration(30.0, "minutes");
            assertEquals(Duration.ofMinutes(30), recipe.getDuration());
        }

        @Test
        @DisplayName("Set duration with 'minute' singular")
        void setDurationMinuteSingular() {
            recipe.setDuration(45.0, "minute");
            assertEquals(Duration.ofMinutes(45), recipe.getDuration());
        }

        @Test
        @DisplayName("Set duration with 'm' abbreviation")
        void setDurationM() {
            recipe.setDuration(15.0, "m");
            assertEquals(Duration.ofMinutes(15), recipe.getDuration());
        }

        @Test
        @DisplayName("Set duration in seconds")
        void setDurationSeconds() {
            recipe.setDuration(90.0, "seconds");
            assertEquals(Duration.ofSeconds(90), recipe.getDuration());
        }

        @Test
        @DisplayName("Set duration with 'second' singular")
        void setDurationSecondSingular() {
            recipe.setDuration(30.0, "second");
            assertEquals(Duration.ofSeconds(30), recipe.getDuration());
        }

        @Test
        @DisplayName("Set duration with 's' abbreviation")
        void setDurationS() {
            recipe.setDuration(120.0, "s");
            assertEquals(Duration.ofSeconds(120), recipe.getDuration());
        }

        @Test
        @DisplayName("Set duration with invalid unit sets null")
        void setDurationInvalidUnit() {
            recipe.setDuration(10.0, "days");
            assertNull(recipe.getDuration());
        }

        @Test
        @DisplayName("Set duration with mixed case works (lowercased)")
        void setDurationMixedCase() {
            recipe.setDuration(10.0, "HOURS");
            assertEquals(Duration.ofHours(10), recipe.getDuration());
        }
    }

    // ==================== setDuration with Duration ====================

    @Nested
    @DisplayName("setDuration(Duration)")
    class SetDurationObjectTests {

        @Test
        @DisplayName("Set duration with Duration object")
        void setDurationObject() {
            Duration d = Duration.ofMinutes(45);
            recipe.setDuration(d);
            assertEquals(d, recipe.getDuration());
        }

        @Test
        @DisplayName("Set duration with null Duration")
        void setDurationNull() {
            recipe.setDuration((Duration) null);
            assertNull(recipe.getDuration());
        }
    }

    // ==================== formatDuration ====================

    @Nested
    @DisplayName("formatDuration()")
    class FormatDurationTests {

        @Test
        @DisplayName("Format duration with hours and minutes")
        void formatHoursAndMinutes() {
            recipe.setDuration(Duration.ofMinutes(90));
            assertEquals("1 hour 30 minutes", recipe.formatDuration());
        }

        @Test
        @DisplayName("Format duration with only hours")
        void formatOnlyHours() {
            recipe.setDuration(Duration.ofHours(2));
            assertEquals("2 hours", recipe.formatDuration());
        }

        @Test
        @DisplayName("Format duration with only minutes")
        void formatOnlyMinutes() {
            recipe.setDuration(Duration.ofMinutes(45));
            assertEquals("45 minutes", recipe.formatDuration());
        }

        @Test
        @DisplayName("Format duration with 1 minute (singular)")
        void formatOneMinute() {
            recipe.setDuration(Duration.ofMinutes(1));
            assertEquals("1 minute", recipe.formatDuration());
        }

        @Test
        @DisplayName("Format duration with 1 hour (singular)")
        void formatOneHour() {
            recipe.setDuration(Duration.ofMinutes(60));
            assertEquals("1 hour", recipe.formatDuration());
        }

        @Test
        @DisplayName("Format duration with zero minutes")
        void formatZeroMinutes() {
            recipe.setDuration(Duration.ZERO);
            assertEquals("0 minutes", recipe.formatDuration());
        }
    }

    // ==================== setGroceryList ====================

    @Nested
    @DisplayName("setGroceryList()")
    class SetGroceryListTests {

        @Test
        @DisplayName("Set grocery list replaces ingredients")
        void setGroceryList() {
            GroceryList newList = new GroceryList();
            newList.addItem(new GroceryItem(100.0, Unit.GRAM, new Ingredient("cheese", "dairy")));

            recipe.setGroceryList(newList);

            assertEquals(1, recipe.getIngredients().getItems().size());
            assertSame(newList, recipe.getIngredients());
        }
    }

    // ==================== toString ====================

    @Nested
    @DisplayName("toString()")
    class ToStringTests {

        @Test
        @DisplayName("toString contains name")
        void toStringContainsName() {
            assertTrue(recipe.toString().contains("Pasta Carbonara"));
        }

        @Test
        @DisplayName("toString contains category")
        void toStringContainsCategory() {
            assertTrue(recipe.toString().contains("MainCourse"));
        }

        @Test
        @DisplayName("toString contains kitchen type")
        void toStringContainsKitchenType() {
            assertTrue(recipe.toString().contains("Italian"));
        }

        @Test
        @DisplayName("toString contains servings")
        void toStringContainsServings() {
            assertTrue(recipe.toString().contains("4"));
        }

        @Test
        @DisplayName("toString contains kilocalories")
        void toStringContainsKilocalories() {
            assertTrue(recipe.toString().contains("550.0"));
        }
    }

    // ==================== Concrete subclasses ====================

    @Nested
    @DisplayName("Concrete subclass types")
    class ConcreteSubclassTests {

        @Test
        @DisplayName("Appetizer sets correct category")
        void appetizerCategory() throws MalformedURLException {
            Recipe appetizer = new Appetizer("Bruschetta", "Italian", sampleUrl, 120.0, 2);
            assertEquals("Appetizer", appetizer.getCategoryFood());
        }

        @Test
        @DisplayName("Dessert sets correct category")
        void dessertCategory() throws MalformedURLException {
            Recipe dessert = new Dessert("Tiramisu", "Italian", sampleUrl, 450.0, 6);
            assertEquals("Dessert", dessert.getCategoryFood());
        }

        @Test
        @DisplayName("MainCourse sets correct category")
        void mainCourseCategory() {
            assertEquals("MainCourse", recipe.getCategoryFood());
        }

        @Test
        @DisplayName("Appetizer with id constructor")
        void appetizerWithId() throws MalformedURLException {
            Recipe appetizer = new Appetizer(1, "Bruschetta", "Italian", sampleUrl, 120.0, 2);
            assertEquals(1, appetizer.getId());
            assertEquals("Appetizer", appetizer.getCategoryFood());
        }

        @Test
        @DisplayName("Dessert with id constructor")
        void dessertWithId() throws MalformedURLException {
            Recipe dessert = new Dessert(2, "Tiramisu", "Italian", sampleUrl, 450.0, 6);
            assertEquals(2, dessert.getId());
            assertEquals("Dessert", dessert.getCategoryFood());
        }
    }
}
