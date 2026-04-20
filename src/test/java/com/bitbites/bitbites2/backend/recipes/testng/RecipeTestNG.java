package com.bitbites.bitbites2.backend.recipes.testng;

import com.bitbites.bitbites2.backend.groceries.*;
import com.bitbites.bitbites2.backend.recipes.*;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.net.URL;
import java.time.Duration;

import static org.testng.Assert.*;

/**
 * TestNG tests for Recipe.
 *
 * Comparative note vs JUnit:
 * - Uses @DataProvider for subclass creation testing
 * - Uses SoftAssert for multi-assertion tests
 * - Uses priority to control execution order
 */
public class RecipeTestNG {

    private URL testUrl;

    @BeforeClass
    public void setUp() throws Exception {
        testUrl = new URL("https://example.com/recipe");
    }

    // ===================== Subclass instantiation via DataProvider =====================

    @DataProvider(name = "subclasses")
    public Object[][] subclasses() {
        return new Object[][]{
                {"Appetizer", Appetizer.class},
                {"Soup", Soup.class},
                {"MainCourse", MainCourse.class},
                {"Dessert", Dessert.class},
                {"Drink", Drink.class},
                {"Salad", Salad.class},
                {"Bread", Bread.class},
        };
    }

    @Test(dataProvider = "subclasses", groups = "subclass")
    public void subclassCategoryFood(String expectedCategory, Class<? extends Recipe> clazz) throws Exception {
        Recipe r = clazz.getConstructor(String.class, String.class, URL.class, double.class, int.class)
                .newInstance("Test", "Romanian", testUrl, 100, 2);
        assertEquals(r.getCategoryFood(), expectedCategory);
    }

    @Test(groups = "subclass")
    public void constructorWithId() {
        Recipe r = new Appetizer(42, "Toast", "Romanian", testUrl, 200, 2);
        SoftAssert sa = new SoftAssert();
        sa.assertEquals(r.getId(), 42);
        sa.assertEquals(r.getCategoryFood(), "Appetizer");
        sa.assertEquals(r.getName(), "Toast");
        sa.assertAll();
    }

    // ===================== Getters =====================

    @Test(groups = "getters")
    public void allGetters() {
        Recipe r = new MainCourse(10, "Pasta", "Italian", testUrl, 450.5, 4);
        SoftAssert sa = new SoftAssert();
        sa.assertEquals(r.getId(), 10);
        sa.assertEquals(r.getName(), "Pasta");
        sa.assertEquals(r.getKitchenType(), "Italian");
        sa.assertEquals(r.getKilocalories(), 450.5);
        sa.assertEquals(r.getServings(), 4);
        sa.assertEquals(r.getInstructions(), testUrl);
        sa.assertNotNull(r.getIngredients());
        sa.assertAll();
    }

    // ===================== addIngredient =====================

    @Test(groups = "ingredient")
    public void addSingleIngredient() {
        Recipe r = new Soup("Borsch", "Romanian", testUrl, 150, 4);
        r.addIngredient(new GroceryItem(200, Unit.GRAM, new Ingredient("carrot", "vegetable")));
        assertEquals(r.getIngredients().getItems().size(), 1);
    }

    @Test(groups = "ingredient")
    public void addMultipleIngredients() {
        Recipe r = new Soup("Borsch", "Romanian", testUrl, 150, 4);
        r.addIngredient(new GroceryItem(200, Unit.GRAM, new Ingredient("carrot", "vegetable")));
        r.addIngredient(new GroceryItem(100, Unit.GRAM, new Ingredient("onion", "vegetable")));
        r.addIngredient(new GroceryItem(500, Unit.MILLILITER, new Ingredient("water", "liquid")));
        assertEquals(r.getIngredients().getItems().size(), 3);
    }

    // ===================== setDuration =====================

    @DataProvider(name = "durationStrings")
    public Object[][] durationStrings() {
        return new Object[][]{
                {2.0, "hours", Duration.ofHours(2)},
                {1.0, "hour", Duration.ofHours(1)},
                {3.0, "h", Duration.ofHours(3)},
                {30.0, "minutes", Duration.ofMinutes(30)},
                {1.0, "minute", Duration.ofMinutes(1)},
                {45.0, "m", Duration.ofMinutes(45)},
                {90.0, "seconds", Duration.ofSeconds(90)},
                {1.0, "second", Duration.ofSeconds(1)},
                {120.0, "s", Duration.ofSeconds(120)},
        };
    }

    @Test(dataProvider = "durationStrings", groups = "duration")
    public void setDurationWithString(double qty, String unit, Duration expected) {
        Recipe r = new Appetizer("Test", "Test", testUrl, 100, 1);
        r.setDuration(qty, unit);
        assertEquals(r.getDuration(), expected);
    }

    @Test(groups = "duration")
    public void setDurationInvalidUnit() {
        Recipe r = new Appetizer("Test", "Test", testUrl, 100, 1);
        r.setDuration(10, "days");
        assertNull(r.getDuration());
    }

    @Test(groups = "duration")
    public void setDurationUpperCase() {
        Recipe r = new Appetizer("Test", "Test", testUrl, 100, 1);
        r.setDuration(2, "HOURS");
        assertEquals(r.getDuration(), Duration.ofHours(2));
    }

    @Test(groups = "duration")
    public void setDurationDirect() {
        Recipe r = new Appetizer("Test", "Test", testUrl, 100, 1);
        r.setDuration(Duration.ofMinutes(45));
        assertEquals(r.getDuration(), Duration.ofMinutes(45));
    }

    // ===================== formatDuration =====================

    @Test(groups = "format")
    public void formatHoursAndMinutes() {
        Recipe r = new Appetizer("Test", "Test", testUrl, 100, 1);
        r.setDuration(Duration.ofMinutes(90));
        assertEquals(r.formatDuration(), "1 hour 30 minutes");
    }

    @Test(groups = "format")
    public void formatOnlyMinutes() {
        Recipe r = new Appetizer("Test", "Test", testUrl, 100, 1);
        r.setDuration(Duration.ofMinutes(45));
        assertEquals(r.formatDuration(), "45 minutes");
    }

    @Test(groups = "format")
    public void formatZero() {
        Recipe r = new Appetizer("Test", "Test", testUrl, 100, 1);
        r.setDuration(Duration.ZERO);
        assertEquals(r.formatDuration(), "0 minutes");
    }

    @Test(groups = "format")
    public void formatExactlyOneHour() {
        Recipe r = new Appetizer("Test", "Test", testUrl, 100, 1);
        r.setDuration(Duration.ofHours(1));
        assertEquals(r.formatDuration(), "1 hour");
    }

    @Test(groups = "format")
    public void formatOneMinute() {
        Recipe r = new Appetizer("Test", "Test", testUrl, 100, 1);
        r.setDuration(Duration.ofMinutes(1));
        assertEquals(r.formatDuration(), "1 minute");
    }

    // ===================== setGroceryList =====================

    @Test(groups = "groceryList")
    public void setGroceryList() {
        Recipe r = new Appetizer("Test", "Test", testUrl, 100, 1);
        GroceryList newList = new GroceryList();
        newList.addItem(new GroceryItem(100, Unit.GRAM, new Ingredient("salt", "spice")));
        r.setGroceryList(newList);
        assertEquals(r.getIngredients().getItems().size(), 1);
    }

    // ===================== toString =====================

    @Test(groups = "toString")
    public void toStringContainsInfo() {
        Recipe r = new Dessert("Tiramisu", "Italian", testUrl, 400, 6);
        r.setDuration(Duration.ofMinutes(30));
        String str = r.toString();
        assertTrue(str.contains("Tiramisu"));
        assertTrue(str.contains("Dessert"));
        assertTrue(str.contains("Italian"));
    }
}
