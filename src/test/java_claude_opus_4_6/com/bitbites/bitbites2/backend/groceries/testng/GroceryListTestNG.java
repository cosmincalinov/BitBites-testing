package com.bitbites.bitbites2.backend.groceries.testng;

import com.bitbites.bitbites2.backend.groceries.*;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.Map;

/**
 * TestNG tests for GroceryList.
 * Uses @BeforeMethod for setup, DataProvider for parameterized tests,
 * and groups for test organization.
 */
public class GroceryListTestNG {

    private GroceryList groceryList;

    private final Ingredient flour = new Ingredient("flour", "dry");
    private final Ingredient sugar = new Ingredient("sugar", "dry");
    private final Ingredient milk = new Ingredient("milk", "liquid");
    private final Ingredient eggs = new Ingredient("eggs", "other");
    private final Ingredient butter = new Ingredient("butter", "dairy");

    @BeforeMethod
    public void setUp() {
        groceryList = new GroceryList();
    }

    // ==================== Constructor ====================

    @Test(groups = {"constructor"})
    public void constructor_createsEmptyList() {
        Assert.assertTrue(groceryList.getItems().isEmpty(), "New GroceryList should be empty");
    }

    @Test(groups = {"constructor"})
    public void constructor_itemsMapIsNotNull() {
        Assert.assertNotNull(groceryList.getItems(), "Items map should not be null");
    }

    // ==================== addItem ====================

    @Test(groups = {"addItem"})
    public void addItem_singleItemToEmptyList() {
        GroceryItem item = new GroceryItem(500.0, Unit.GRAM, flour);
        groceryList.addItem(item);

        Assert.assertEquals(groceryList.getItems().size(), 1);
        Assert.assertEquals(groceryList.getItems().get(flour), item);
    }

    @Test(groups = {"addItem"})
    public void addItem_multipleDifferentItems() {
        groceryList.addItem(new GroceryItem(500.0, Unit.GRAM, flour));
        groceryList.addItem(new GroceryItem(200.0, Unit.GRAM, sugar));
        groceryList.addItem(new GroceryItem(1.0, Unit.LITER, milk));

        Assert.assertEquals(groceryList.getItems().size(), 3);
    }

    @Test(groups = {"addItem"})
    public void addItem_duplicateSameUnit_mergesQuantities() {
        groceryList.addItem(new GroceryItem(200.0, Unit.GRAM, flour));
        groceryList.addItem(new GroceryItem(300.0, Unit.GRAM, flour));

        Assert.assertEquals(groceryList.getItems().size(), 1);
        Assert.assertEquals(groceryList.getItems().get(flour).quantity(), 500.0, 1e-9);
    }

    @Test(groups = {"addItem"})
    public void addItem_duplicateCompatibleUnit_mergesAfterConversion() {
        groceryList.addItem(new GroceryItem(500.0, Unit.GRAM, flour));
        groceryList.addItem(new GroceryItem(1.0, Unit.KILOGRAM, flour));

        Assert.assertEquals(groceryList.getItems().size(), 1);
        // 1 KG -> 1000 GRAM + 500 GRAM = 1500 GRAM
        Assert.assertEquals(groceryList.getItems().get(flour).quantity(), 1500.0, 1e-9);
    }

    @Test(groups = {"addItem"})
    public void addItem_duplicateIncompatibleUnit_replacesItem() {
        groceryList.addItem(new GroceryItem(500.0, Unit.GRAM, flour));
        groceryList.addItem(new GroceryItem(2.0, Unit.LITER, flour));

        Assert.assertEquals(groceryList.getItems().size(), 1);
        Assert.assertEquals(groceryList.getItems().get(flour).quantity(), 2.0, 1e-9);
        Assert.assertEquals(groceryList.getItems().get(flour).unit(), Unit.LITER);
    }

    @Test(groups = {"addItem"})
    public void addItem_cupWithGram_mergesViaConversion() {
        groceryList.addItem(new GroceryItem(200.0, Unit.GRAM, sugar));
        groceryList.addItem(new GroceryItem(1.0, Unit.CUP, sugar));

        Assert.assertEquals(groceryList.getItems().size(), 1);
        // 1 CUP -> 200 GRAM + 200 GRAM = 400 GRAM
        Assert.assertEquals(groceryList.getItems().get(sugar).quantity(), 400.0, 1e-9);
    }

    @Test(groups = {"addItem"})
    public void addItem_pieceItems_merge() {
        groceryList.addItem(new GroceryItem(6.0, Unit.PIECE, eggs));
        groceryList.addItem(new GroceryItem(4.0, Unit.PIECE, eggs));

        Assert.assertEquals(groceryList.getItems().size(), 1);
        Assert.assertEquals(groceryList.getItems().get(eggs).quantity(), 10.0, 1e-9);
    }

    @Test(groups = {"addItem"})
    public void addItem_pieceItemToEmptyList() {
        groceryList.addItem(new GroceryItem(6.0, Unit.PIECE, eggs));

        Assert.assertEquals(groceryList.getItems().size(), 1);
        Assert.assertEquals(groceryList.getItems().get(eggs).quantity(), 6.0, 1e-9);
    }

    // ==================== addLists ====================

    @Test(groups = {"addLists"})
    public void addLists_twoEmptyLists_returnsEmpty() {
        GroceryList result = GroceryList.addLists(new GroceryList(), new GroceryList());
        Assert.assertTrue(result.getItems().isEmpty());
    }

    @Test(groups = {"addLists"})
    public void addLists_nonEmptyWithEmpty_returnsCopy() {
        GroceryList list1 = new GroceryList();
        list1.addItem(new GroceryItem(100.0, Unit.GRAM, flour));

        GroceryList result = GroceryList.addLists(list1, new GroceryList());
        Assert.assertEquals(result.getItems().size(), 1);
        Assert.assertEquals(result.getItems().get(flour).quantity(), 100.0, 1e-9);
    }

    @Test(groups = {"addLists"})
    public void addLists_differentIngredients_combinesBoth() {
        GroceryList list1 = new GroceryList();
        list1.addItem(new GroceryItem(100.0, Unit.GRAM, flour));

        GroceryList list2 = new GroceryList();
        list2.addItem(new GroceryItem(200.0, Unit.GRAM, sugar));

        GroceryList result = GroceryList.addLists(list1, list2);
        Assert.assertEquals(result.getItems().size(), 2);
    }

    @Test(groups = {"addLists"})
    public void addLists_sameIngredient_mergesQuantities() {
        GroceryList list1 = new GroceryList();
        list1.addItem(new GroceryItem(200.0, Unit.GRAM, flour));

        GroceryList list2 = new GroceryList();
        list2.addItem(new GroceryItem(300.0, Unit.GRAM, flour));

        GroceryList result = GroceryList.addLists(list1, list2);
        Assert.assertEquals(result.getItems().size(), 1);
        Assert.assertEquals(result.getItems().get(flour).quantity(), 500.0, 1e-9);
    }

    @Test(groups = {"addLists"})
    public void addLists_mixedIngredients_correctResult() {
        GroceryList list1 = new GroceryList();
        list1.addItem(new GroceryItem(100.0, Unit.GRAM, flour));
        list1.addItem(new GroceryItem(50.0, Unit.GRAM, sugar));

        GroceryList list2 = new GroceryList();
        list2.addItem(new GroceryItem(200.0, Unit.GRAM, flour));
        list2.addItem(new GroceryItem(1.0, Unit.LITER, milk));

        GroceryList result = GroceryList.addLists(list1, list2);
        Assert.assertEquals(result.getItems().size(), 3);
        Assert.assertEquals(result.getItems().get(flour).quantity(), 300.0, 1e-9);
    }

    @Test(groups = {"addLists"})
    public void addLists_doesNotModifyOriginals() {
        GroceryList list1 = new GroceryList();
        list1.addItem(new GroceryItem(100.0, Unit.GRAM, flour));

        GroceryList list2 = new GroceryList();
        list2.addItem(new GroceryItem(200.0, Unit.GRAM, sugar));

        GroceryList.addLists(list1, list2);

        Assert.assertEquals(list1.getItems().size(), 1, "Original list1 should not be modified");
        Assert.assertEquals(list2.getItems().size(), 1, "Original list2 should not be modified");
    }

    // ==================== sortedGroceryList ====================

    @Test(groups = {"sorting"})
    public void sortedGroceryList_ordersByQuantityDescending() {
        groceryList.addItem(new GroceryItem(100.0, Unit.GRAM, sugar));
        groceryList.addItem(new GroceryItem(1.0, Unit.KILOGRAM, flour));
        groceryList.addItem(new GroceryItem(50.0, Unit.GRAM, butter));

        groceryList.sortedGroceryList();
        Ingredient[] keys = groceryList.getItems().keySet().toArray(new Ingredient[0]);

        // flour (1 KG = 1000g) > sugar (100g) > butter (50g)
        Assert.assertEquals(keys[0], flour);
        Assert.assertEquals(keys[1], sugar);
        Assert.assertEquals(keys[2], butter);
    }

    @Test(groups = {"sorting"})
    public void sortedGroceryList_emptyListDoesNotThrow() {
        groceryList.sortedGroceryList(); // should not throw
        Assert.assertTrue(groceryList.getItems().isEmpty());
    }

    @Test(groups = {"sorting"})
    public void sortedGroceryList_singleItem_keepsItem() {
        groceryList.addItem(new GroceryItem(500.0, Unit.GRAM, flour));
        groceryList.sortedGroceryList();
        Assert.assertEquals(groceryList.getItems().size(), 1);
    }

    // ==================== toString ====================

    @Test(groups = {"toString"})
    public void toString_emptyList_returnsEmptyString() {
        Assert.assertEquals(groceryList.toString(), "");
    }

    @Test(groups = {"toString"})
    public void toString_singleItem_containsDetails() {
        groceryList.addItem(new GroceryItem(500.0, Unit.GRAM, flour));
        String result = groceryList.toString();

        Assert.assertTrue(result.contains("500.0"), "Should contain quantity");
        Assert.assertTrue(result.contains("gram"), "Should contain unit");
        Assert.assertTrue(result.contains("flour"), "Should contain ingredient name");
    }

    // ==================== getItems ====================

    @Test(groups = {"getItems"})
    public void getItems_returnsCorrectMap() {
        groceryList.addItem(new GroceryItem(500.0, Unit.GRAM, flour));
        groceryList.addItem(new GroceryItem(200.0, Unit.GRAM, sugar));

        Map<Ingredient, GroceryItem> items = groceryList.getItems();
        Assert.assertEquals(items.size(), 2);
        Assert.assertTrue(items.containsKey(flour));
        Assert.assertTrue(items.containsKey(sugar));
    }
}
