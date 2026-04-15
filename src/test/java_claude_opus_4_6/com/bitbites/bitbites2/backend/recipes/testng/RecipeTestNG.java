package com.bitbites.bitbites2.backend.recipes.testng;

import com.bitbites.bitbites2.backend.groceries.*;
import com.bitbites.bitbites2.backend.recipes.*;

import org.testng.Assert;
import org.testng.annotations.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

/**
 * TestNG tests for Recipe (abstract class, tested via concrete subclasses).
 * Uses @BeforeMethod for setup, DataProvider for parameterized duration tests,
 * and groups for test organization.
 */
public class RecipeTestNG {

    private URL sampleUrl;
    private Recipe recipe;

    @BeforeMethod
    public void setUp() throws MalformedURLException {
        sampleUrl = new URL("https://example.com/recipe");
        recipe = new MainCourse("Pasta Carbonara", "Italian", sampleUrl, 550.0, 4);
    }

    // ==================== Constructor ====================

    @Test(groups = {"constructor"})
    public void constructor_setsName() {
        Assert.assertEquals(recipe.getName(), "Pasta Carbonara");
    }

    @Test(groups = {"constructor"})
    public void constructor_setsCategoryFood() {
        Assert.assertEquals(recipe.getCategoryFood(), "MainCourse");
    }

    @Test(groups = {"constructor"})
    public void constructor_setsKitchenType() {
        Assert.assertEquals(recipe.getKitchenType(), "Italian");
    }

    @Test(groups = {"constructor"})
    public void constructor_setsKilocalories() {
        Assert.assertEquals(recipe.getKilocalories(), 550.0, 1e-9);
    }

    @Test(groups = {"constructor"})
    public void constructor_setsServings() {
        Assert.assertEquals(recipe.getServings(), 4);
    }

    @Test(groups = {"constructor"})
    public void constructor_setsInstructionsUrl() {
        Assert.assertEquals(recipe.getInstructions(), sampleUrl);
    }

    @Test(groups = {"constructor"})
    public void constructor_initializesEmptyIngredients() {
        Assert.assertNotNull(recipe.getIngredients());
        Assert.assertTrue(recipe.getIngredients().getItems().isEmpty());
    }

    @Test(groups = {"constructor"})
    public void constructor_withId_setsIdCorrectly() throws MalformedURLException {
        Recipe r = new MainCourse(42, "Test", "Italian", sampleUrl, 100.0, 2);
        Assert.assertEquals(r.getId(), 42);
        Assert.assertEquals(r.getName(), "Test");
    }

    // ==================== addIngredient ====================

    @Test(groups = {"addIngredient"})
    public void addIngredient_singleItem() {
        recipe.addIngredient(new GroceryItem(500.0, Unit.GRAM, new Ingredient("pasta", "dry")));
        Assert.assertEquals(recipe.getIngredients().getItems().size(), 1);
    }

    @Test(groups = {"addIngredient"})
    public void addIngredient_multipleItems() {
        recipe.addIngredient(new GroceryItem(500.0, Unit.GRAM, new Ingredient("pasta", "dry")));
        recipe.addIngredient(new GroceryItem(200.0, Unit.GRAM, new Ingredient("bacon", "meat")));
        recipe.addIngredient(new GroceryItem(4.0, Unit.PIECE, new Ingredient("eggs", "other")));

        Assert.assertEquals(recipe.getIngredients().getItems().size(), 3);
    }

    @Test(groups = {"addIngredient"})
    public void addIngredient_duplicateMerges() {
        Ingredient pasta = new Ingredient("pasta", "dry");
        recipe.addIngredient(new GroceryItem(200.0, Unit.GRAM, pasta));
        recipe.addIngredient(new GroceryItem(300.0, Unit.GRAM, pasta));

        Assert.assertEquals(recipe.getIngredients().getItems().size(), 1);
        Assert.assertEquals(recipe.getIngredients().getItems().get(pasta).quantity(), 500.0, 1e-9);
    }

    // ==================== setDuration(String) with DataProvider ====================

    @DataProvider(name = "validDurationUnits")
    public Object[][] validDurationUnits() {
        return new Object[][]{
                {2.0, "hours", Duration.ofHours(2)},
                {1.0, "hour", Duration.ofHours(1)},
                {3.0, "h", Duration.ofHours(3)},
                {30.0, "minutes", Duration.ofMinutes(30)},
                {45.0, "minute", Duration.ofMinutes(45)},
                {15.0, "m", Duration.ofMinutes(15)},
                {90.0, "seconds", Duration.ofSeconds(90)},
                {30.0, "second", Duration.ofSeconds(30)},
                {120.0, "s", Duration.ofSeconds(120)},
                {10.0, "HOURS", Duration.ofHours(10)},   // mixed case
                {5.0, "Minutes", Duration.ofMinutes(5)},  // mixed case
        };
    }

    @Test(dataProvider = "validDurationUnits", groups = {"duration"})
    public void setDuration_validUnit_setsCorrectly(double quantity, String unit, Duration expected) {
        recipe.setDuration(quantity, unit);
        Assert.assertEquals(recipe.getDuration(), expected);
    }

    @DataProvider(name = "invalidDurationUnits")
    public Object[][] invalidDurationUnits() {
        return new Object[][]{
                {"days"},
                {"weeks"},
                {"years"},
                {""},
                {"xyz"},
        };
    }

    @Test(dataProvider = "invalidDurationUnits", groups = {"duration"})
    public void setDuration_invalidUnit_setsNull(String unit) {
        recipe.setDuration(10.0, unit);
        Assert.assertNull(recipe.getDuration());
    }

    @Test(groups = {"duration"})
    public void setDuration_withDurationObject_setsCorrectly() {
        Duration d = Duration.ofMinutes(45);
        recipe.setDuration(d);
        Assert.assertEquals(recipe.getDuration(), d);
    }

    @Test(groups = {"duration"})
    public void setDuration_withNullDuration_setsNull() {
        recipe.setDuration((Duration) null);
        Assert.assertNull(recipe.getDuration());
    }

    // ==================== formatDuration ====================

    @DataProvider(name = "durationFormats")
    public Object[][] durationFormats() {
        return new Object[][]{
                {Duration.ofMinutes(90), "1 hour 30 minutes"},
                {Duration.ofMinutes(45), "45 minutes"},
                {Duration.ofMinutes(1), "1 minute"},
                {Duration.ZERO, "0 minutes"},
                {Duration.ofHours(2), "2 hours"},
                {Duration.ofMinutes(60), "1 hour"},
                {Duration.ofMinutes(121), "2 hours 1 minute"},
                {Duration.ofMinutes(150), "2 hours 30 minutes"},
        };
    }

    @Test(dataProvider = "durationFormats", groups = {"formatDuration"})
    public void formatDuration_formatsCorrectly(Duration duration, String expected) {
        recipe.setDuration(duration);
        Assert.assertEquals(recipe.formatDuration(), expected);
    }

    // ==================== setGroceryList ====================

    @Test(groups = {"setGroceryList"})
    public void setGroceryList_replacesIngredients() {
        GroceryList newList = new GroceryList();
        newList.addItem(new GroceryItem(100.0, Unit.GRAM, new Ingredient("cheese", "dairy")));

        recipe.setGroceryList(newList);

        Assert.assertEquals(recipe.getIngredients().getItems().size(), 1);
        Assert.assertSame(recipe.getIngredients(), newList);
    }

    // ==================== toString ====================

    @Test(groups = {"toString"})
    public void toString_containsName() {
        Assert.assertTrue(recipe.toString().contains("Pasta Carbonara"));
    }

    @Test(groups = {"toString"})
    public void toString_containsCategory() {
        Assert.assertTrue(recipe.toString().contains("MainCourse"));
    }

    @Test(groups = {"toString"})
    public void toString_containsKitchenType() {
        Assert.assertTrue(recipe.toString().contains("Italian"));
    }

    @Test(groups = {"toString"})
    public void toString_containsServings() {
        Assert.assertTrue(recipe.toString().contains("4"));
    }

    @Test(groups = {"toString"})
    public void toString_containsKilocalories() {
        Assert.assertTrue(recipe.toString().contains("550.0"));
    }

    // ==================== Concrete subclass categories ====================

    @DataProvider(name = "subclassCategories")
    public Object[][] subclassCategories() throws MalformedURLException {
        URL url = new URL("https://example.com/recipe");
        return new Object[][]{
                {new Appetizer("Bruschetta", "Italian", url, 120.0, 2), "Appetizer"},
                {new Dessert("Tiramisu", "Italian", url, 450.0, 6), "Dessert"},
                {new MainCourse("Steak", "French", url, 700.0, 2), "MainCourse"},
                {new Appetizer(1, "Bruschetta", "Italian", url, 120.0, 2), "Appetizer"},
                {new Dessert(2, "Tiramisu", "Italian", url, 450.0, 6), "Dessert"},
                {new MainCourse(3, "Steak", "French", url, 700.0, 2), "MainCourse"},
        };
    }

    @Test(dataProvider = "subclassCategories", groups = {"subclasses"})
    public void subclass_setsCorrectCategory(Recipe recipe, String expectedCategory) {
        Assert.assertEquals(recipe.getCategoryFood(), expectedCategory);
    }

    @Test(groups = {"subclasses"})
    public void subclassWithId_setsIdCorrectly() throws MalformedURLException {
        Recipe appetizer = new Appetizer(1, "Bruschetta", "Italian", sampleUrl, 120.0, 2);
        Assert.assertEquals(appetizer.getId(), 1);

        Recipe dessert = new Dessert(2, "Tiramisu", "Italian", sampleUrl, 450.0, 6);
        Assert.assertEquals(dessert.getId(), 2);
    }
}
