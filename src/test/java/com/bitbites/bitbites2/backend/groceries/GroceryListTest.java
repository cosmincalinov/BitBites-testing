package com.bitbites.bitbites2.backend.groceries;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 tests for GroceryList.
 *
 * Strategies:
 * - Equivalence partitioning (empty list, single item, duplicate ingredient compatible/incompatible)
 * - Boundary analysis (empty lists in addLists)
 * - Statement/decision coverage for addItem merge logic
 */
class GroceryListTest {

    private GroceryList list;
    private Ingredient flour;
    private Ingredient sugar;
    private Ingredient water;

    @BeforeEach
    void setUp() {
        list = new GroceryList();
        flour = new Ingredient("flour", "baking");
        sugar = new Ingredient("sugar", "baking");
        water = new Ingredient("water", "liquid");
    }

    // ===================== addItem =====================

    @Nested
    @DisplayName("addItem tests")
    class AddItemTests {

        @Test
        @DisplayName("Add single item to empty list")
        void addSingleItem() {
            GroceryItem item = new GroceryItem(500, Unit.GRAM, flour);
            list.addItem(item);

            assertEquals(1, list.getItems().size());
            assertEquals(500, list.getItems().get(flour).quantity());
        }

        @Test
        @DisplayName("Add two different ingredients")
        void addTwoDifferentIngredients() {
            list.addItem(new GroceryItem(500, Unit.GRAM, flour));
            list.addItem(new GroceryItem(200, Unit.GRAM, sugar));

            assertEquals(2, list.getItems().size());
        }

        @Test
        @DisplayName("Merge compatible units - same unit")
        void mergeSameUnit() {
            list.addItem(new GroceryItem(200, Unit.GRAM, flour));
            list.addItem(new GroceryItem(300, Unit.GRAM, flour));

            assertEquals(1, list.getItems().size());
            assertEquals(500.0, list.getItems().get(flour).quantity(), 1e-9);
        }

        @Test
        @DisplayName("Merge compatible units - GRAM + KILOGRAM")
        void mergeCompatibleUnits() {
            list.addItem(new GroceryItem(500, Unit.GRAM, flour));
            list.addItem(new GroceryItem(1, Unit.KILOGRAM, flour));

            assertEquals(1, list.getItems().size());
            // 1 KG → 1000 GRAM, total = 1500 GRAM
            assertEquals(1500.0, list.getItems().get(flour).quantity(), 1e-9);
            assertEquals(Unit.GRAM, list.getItems().get(flour).unit());
        }

        @Test
        @DisplayName("Replace on incompatible units")
        void replaceOnIncompatibleUnits() {
            list.addItem(new GroceryItem(500, Unit.GRAM, flour));
            list.addItem(new GroceryItem(2, Unit.PIECE, flour));

            assertEquals(1, list.getItems().size());
            // should be replaced (PIECE not compatible with GRAM for conversion via mass)
            GroceryItem result = list.getItems().get(flour);
            // After the catch block, it replaces with the new item
            assertEquals(2, result.quantity());
            assertEquals(Unit.PIECE, result.unit());
        }

        @Test
        @DisplayName("Add item with zero quantity")
        void addZeroQuantity() {
            list.addItem(new GroceryItem(0, Unit.GRAM, flour));
            assertEquals(1, list.getItems().size());
            assertEquals(0.0, list.getItems().get(flour).quantity());
        }
    }

    // ===================== addLists =====================

    @Nested
    @DisplayName("addLists tests")
    class AddListsTests {

        @Test
        @DisplayName("Add two non-overlapping lists")
        void addNonOverlapping() {
            GroceryList list1 = new GroceryList();
            list1.addItem(new GroceryItem(500, Unit.GRAM, flour));

            GroceryList list2 = new GroceryList();
            list2.addItem(new GroceryItem(200, Unit.GRAM, sugar));

            GroceryList sum = GroceryList.addLists(list1, list2);
            assertEquals(2, sum.getItems().size());
        }

        @Test
        @DisplayName("Add two overlapping lists merges quantities")
        void addOverlapping() {
            GroceryList list1 = new GroceryList();
            list1.addItem(new GroceryItem(200, Unit.GRAM, flour));

            GroceryList list2 = new GroceryList();
            list2.addItem(new GroceryItem(300, Unit.GRAM, flour));

            GroceryList sum = GroceryList.addLists(list1, list2);
            assertEquals(1, sum.getItems().size());
            assertEquals(500.0, sum.getItems().get(flour).quantity(), 1e-9);
        }

        @Test
        @DisplayName("Add empty list to non-empty")
        void addEmptyToNonEmpty() {
            GroceryList list1 = new GroceryList();
            list1.addItem(new GroceryItem(500, Unit.GRAM, flour));

            GroceryList empty = new GroceryList();
            GroceryList sum = GroceryList.addLists(list1, empty);

            assertEquals(1, sum.getItems().size());
        }

        @Test
        @DisplayName("Add two empty lists")
        void addTwoEmpty() {
            GroceryList sum = GroceryList.addLists(new GroceryList(), new GroceryList());
            assertTrue(sum.getItems().isEmpty());
        }
    }

    // ===================== sortedGroceryList =====================

    @Nested
    @DisplayName("sortedGroceryList tests")
    class SortedTests {

        @Test
        @DisplayName("Sort by quantity descending (in grams)")
        void sortDescending() {
            list.addItem(new GroceryItem(100, Unit.GRAM, sugar));
            list.addItem(new GroceryItem(1, Unit.KILOGRAM, flour));
            list.addItem(new GroceryItem(500, Unit.GRAM, water));

            list.sortedGroceryList();

            Map<Ingredient, GroceryItem> items = list.getItems();
            double prev = Double.MAX_VALUE;
            for (GroceryItem item : items.values()) {
                double inGrams;
                try {
                    inGrams = UnitConverter.convert(item.quantity(), item.unit(), Unit.GRAM);
                } catch (IllegalArgumentException e) {
                    continue;
                }
                assertTrue(inGrams <= prev, "Items should be sorted descending by gram equivalent");
                prev = inGrams;
            }
        }

        @Test
        @DisplayName("Sort empty list does not throw")
        void sortEmptyList() {
            assertDoesNotThrow(() -> list.sortedGroceryList());
            assertTrue(list.getItems().isEmpty());
        }

        @Test
        @DisplayName("Sort with PIECE items (incompatible with GRAM)")
        void sortWithPiece() {
            list.addItem(new GroceryItem(500, Unit.GRAM, flour));
            list.addItem(new GroceryItem(3, Unit.PIECE, new Ingredient("egg", "dairy")));
            assertDoesNotThrow(() -> list.sortedGroceryList());
            assertEquals(2, list.getItems().size());
        }
    }

    // ===================== toString =====================

    @Test
    @DisplayName("toString includes all items")
    void toStringTest() {
        list.addItem(new GroceryItem(500, Unit.GRAM, flour));
        list.addItem(new GroceryItem(200, Unit.GRAM, sugar));

        String result = list.toString();
        assertTrue(result.contains("flour"));
        assertTrue(result.contains("sugar"));
    }

    @Test
    @DisplayName("toString of empty list is empty string")
    void toStringEmpty() {
        assertEquals("", list.toString());
    }
}
