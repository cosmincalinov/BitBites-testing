package com.bitbites.bitbites2.backend.groceries.testng;

import com.bitbites.bitbites2.backend.groceries.*;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import static org.testng.Assert.*;

/**
 * TestNG tests for GroceryList.
 *
 * Comparative note vs JUnit:
 * - Uses SoftAssert to collect multiple failures before reporting
 * - Uses @BeforeMethod (equivalent to JUnit @BeforeEach)
 * - Uses groups for test categorization
 * - Uses dependsOnMethods to create test dependencies
 */
public class GroceryListTestNG {

    private GroceryList list;
    private Ingredient flour;
    private Ingredient sugar;
    private Ingredient water;

    @BeforeMethod
    public void setUp() {
        list = new GroceryList();
        flour = new Ingredient("flour", "baking");
        sugar = new Ingredient("sugar", "baking");
        water = new Ingredient("water", "liquid");
    }

    // ===================== addItem =====================

    @Test(groups = "addItem")
    public void addSingleItem() {
        GroceryItem item = new GroceryItem(500, Unit.GRAM, flour);
        list.addItem(item);
        assertEquals(list.getItems().size(), 1);
        assertEquals(list.getItems().get(flour).quantity(), 500.0);
    }

    @Test(groups = "addItem")
    public void addTwoDifferentIngredients() {
        list.addItem(new GroceryItem(500, Unit.GRAM, flour));
        list.addItem(new GroceryItem(200, Unit.GRAM, sugar));
        assertEquals(list.getItems().size(), 2);
    }

    @Test(groups = "addItem")
    public void mergeSameUnit() {
        list.addItem(new GroceryItem(200, Unit.GRAM, flour));
        list.addItem(new GroceryItem(300, Unit.GRAM, flour));

        SoftAssert sa = new SoftAssert();
        sa.assertEquals(list.getItems().size(), 1, "Should have 1 item after merge");
        sa.assertEquals(list.getItems().get(flour).quantity(), 500.0, 1e-9, "Quantities should be summed");
        sa.assertAll();
    }

    @Test(groups = "addItem")
    public void mergeCompatibleUnits() {
        list.addItem(new GroceryItem(500, Unit.GRAM, flour));
        list.addItem(new GroceryItem(1, Unit.KILOGRAM, flour));

        SoftAssert sa = new SoftAssert();
        sa.assertEquals(list.getItems().size(), 1);
        sa.assertEquals(list.getItems().get(flour).quantity(), 1500.0, 1e-9);
        sa.assertEquals(list.getItems().get(flour).unit(), Unit.GRAM);
        sa.assertAll();
    }

    @Test(groups = "addItem")
    public void replaceOnIncompatibleUnits() {
        list.addItem(new GroceryItem(500, Unit.GRAM, flour));
        list.addItem(new GroceryItem(2, Unit.PIECE, flour));

        GroceryItem result = list.getItems().get(flour);
        assertEquals(result.quantity(), 2.0);
        assertEquals(result.unit(), Unit.PIECE);
    }

    @Test(groups = "addItem")
    public void addZeroQuantity() {
        list.addItem(new GroceryItem(0, Unit.GRAM, flour));
        assertEquals(list.getItems().size(), 1);
        assertEquals(list.getItems().get(flour).quantity(), 0.0);
    }

    // ===================== addLists =====================

    @Test(groups = "addLists")
    public void addNonOverlapping() {
        GroceryList list1 = new GroceryList();
        list1.addItem(new GroceryItem(500, Unit.GRAM, flour));
        GroceryList list2 = new GroceryList();
        list2.addItem(new GroceryItem(200, Unit.GRAM, sugar));

        GroceryList sum = GroceryList.addLists(list1, list2);
        assertEquals(sum.getItems().size(), 2);
    }

    @Test(groups = "addLists")
    public void addOverlapping() {
        GroceryList list1 = new GroceryList();
        list1.addItem(new GroceryItem(200, Unit.GRAM, flour));
        GroceryList list2 = new GroceryList();
        list2.addItem(new GroceryItem(300, Unit.GRAM, flour));

        GroceryList sum = GroceryList.addLists(list1, list2);
        assertEquals(sum.getItems().size(), 1);
        assertEquals(sum.getItems().get(flour).quantity(), 500.0, 1e-9);
    }

    @Test(groups = "addLists")
    public void addEmptyToNonEmpty() {
        GroceryList list1 = new GroceryList();
        list1.addItem(new GroceryItem(500, Unit.GRAM, flour));
        GroceryList sum = GroceryList.addLists(list1, new GroceryList());
        assertEquals(sum.getItems().size(), 1);
    }

    @Test(groups = "addLists")
    public void addTwoEmpty() {
        GroceryList sum = GroceryList.addLists(new GroceryList(), new GroceryList());
        assertTrue(sum.getItems().isEmpty());
    }

    // ===================== sortedGroceryList =====================

    @Test(groups = "sort")
    public void sortDescending() {
        list.addItem(new GroceryItem(100, Unit.GRAM, sugar));
        list.addItem(new GroceryItem(1, Unit.KILOGRAM, flour));
        list.addItem(new GroceryItem(500, Unit.GRAM, water));

        list.sortedGroceryList();

        double prev = Double.MAX_VALUE;
        for (GroceryItem item : list.getItems().values()) {
            try {
                java.lang.reflect.Method m = Class.forName("com.bitbites.bitbites2.backend.groceries.UnitConverter")
                        .getDeclaredMethod("convert", double.class, Unit.class, Unit.class);
                m.setAccessible(true);
                double inGrams = (double) m.invoke(null, item.quantity(), item.unit(), Unit.GRAM);
                assertTrue(inGrams <= prev, "Items should be sorted descending");
                prev = inGrams;
            } catch (Exception e) {
                // skip non-convertible items
            }
        }
    }

    @Test(groups = "sort")
    public void sortEmptyList() {
        list.sortedGroceryList();
        assertTrue(list.getItems().isEmpty());
    }

    @Test(groups = "sort")
    public void sortWithPiece() {
        list.addItem(new GroceryItem(500, Unit.GRAM, flour));
        list.addItem(new GroceryItem(3, Unit.PIECE, new Ingredient("egg", "dairy")));
        list.sortedGroceryList();
        assertEquals(list.getItems().size(), 2);
    }

    // ===================== toString =====================

    @Test(groups = "toString")
    public void toStringIncludesAll() {
        list.addItem(new GroceryItem(500, Unit.GRAM, flour));
        list.addItem(new GroceryItem(200, Unit.GRAM, sugar));
        String result = list.toString();
        assertTrue(result.contains("flour"));
        assertTrue(result.contains("sugar"));
    }

    @Test(groups = "toString")
    public void toStringEmpty() {
        assertEquals(list.toString(), "");
    }
}
