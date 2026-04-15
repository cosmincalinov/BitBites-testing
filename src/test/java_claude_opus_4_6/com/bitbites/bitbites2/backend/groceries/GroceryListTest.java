package com.bitbites.bitbites2.backend.groceries;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GroceryList Tests")
class GroceryListTest {

    private GroceryList groceryList;

    private final Ingredient flour = new Ingredient("flour", "dry");
    private final Ingredient sugar = new Ingredient("sugar", "dry");
    private final Ingredient milk = new Ingredient("milk", "liquid");
    private final Ingredient eggs = new Ingredient("eggs", "other");
    private final Ingredient butter = new Ingredient("butter", "dairy");

    @BeforeEach
    void setUp() {
        groceryList = new GroceryList();
    }

    // ==================== Constructor ====================

    @Nested
    @DisplayName("Constructor")
    class ConstructorTests {

        @Test
        @DisplayName("New GroceryList is empty")
        void newListIsEmpty() {
            assertTrue(groceryList.getItems().isEmpty());
        }

        @Test
        @DisplayName("New GroceryList items map is not null")
        void newListItemsNotNull() {
            assertNotNull(groceryList.getItems());
        }
    }

    // ==================== addItem ====================

    @Nested
    @DisplayName("addItem()")
    class AddItemTests {

        @Test
        @DisplayName("Add single item to empty list")
        void addSingleItem() {
            GroceryItem item = new GroceryItem(500.0, Unit.GRAM, flour);
            groceryList.addItem(item);

            assertEquals(1, groceryList.getItems().size());
            assertEquals(item, groceryList.getItems().get(flour));
        }

        @Test
        @DisplayName("Add multiple different items")
        void addMultipleDifferentItems() {
            groceryList.addItem(new GroceryItem(500.0, Unit.GRAM, flour));
            groceryList.addItem(new GroceryItem(200.0, Unit.GRAM, sugar));
            groceryList.addItem(new GroceryItem(1.0, Unit.LITER, milk));

            assertEquals(3, groceryList.getItems().size());
        }

        @Test
        @DisplayName("Add duplicate ingredient with same unit merges quantities")
        void addDuplicateSameUnitMerges() {
            groceryList.addItem(new GroceryItem(200.0, Unit.GRAM, flour));
            groceryList.addItem(new GroceryItem(300.0, Unit.GRAM, flour));

            assertEquals(1, groceryList.getItems().size());
            assertEquals(500.0, groceryList.getItems().get(flour).quantity(), 1e-9);
        }

        @Test
        @DisplayName("Add duplicate ingredient with compatible unit merges after conversion")
        void addDuplicateCompatibleUnitMerges() {
            groceryList.addItem(new GroceryItem(500.0, Unit.GRAM, flour));
            groceryList.addItem(new GroceryItem(1.0, Unit.KILOGRAM, flour));

            assertEquals(1, groceryList.getItems().size());
            // 1 KG converted to GRAM = 1000g + 500g = 1500g
            assertEquals(1500.0, groceryList.getItems().get(flour).quantity(), 1e-9);
        }

        @Test
        @DisplayName("Add duplicate ingredient with incompatible unit replaces item")
        void addDuplicateIncompatibleUnitReplaces() {
            groceryList.addItem(new GroceryItem(500.0, Unit.GRAM, flour));
            groceryList.addItem(new GroceryItem(2.0, Unit.LITER, flour));

            assertEquals(1, groceryList.getItems().size());
            // incompatible (mass vs volume), so new item replaces old
            assertEquals(2.0, groceryList.getItems().get(flour).quantity(), 1e-9);
            assertEquals(Unit.LITER, groceryList.getItems().get(flour).unit());
        }

        @Test
        @DisplayName("Add item with CUP unit merges with GRAM (compatible via count)")
        void addItemCupWithGramMerges() {
            groceryList.addItem(new GroceryItem(200.0, Unit.GRAM, sugar));
            groceryList.addItem(new GroceryItem(1.0, Unit.CUP, sugar));

            assertEquals(1, groceryList.getItems().size());
            // 1 CUP -> 200 GRAM, so total = 200 + 200 = 400 GRAM
            assertEquals(400.0, groceryList.getItems().get(sugar).quantity(), 1e-9);
        }

        @Test
        @DisplayName("Add item with PIECE unit to empty list")
        void addPieceItem() {
            GroceryItem item = new GroceryItem(6.0, Unit.PIECE, eggs);
            groceryList.addItem(item);

            assertEquals(1, groceryList.getItems().size());
            assertEquals(6.0, groceryList.getItems().get(eggs).quantity(), 1e-9);
        }

        @Test
        @DisplayName("Add two PIECE items merges")
        void addTwoPieceItemsMerge() {
            groceryList.addItem(new GroceryItem(6.0, Unit.PIECE, eggs));
            groceryList.addItem(new GroceryItem(4.0, Unit.PIECE, eggs));

            assertEquals(1, groceryList.getItems().size());
            assertEquals(10.0, groceryList.getItems().get(eggs).quantity(), 1e-9);
        }
    }

    // ==================== addLists ====================

    @Nested
    @DisplayName("addLists()")
    class AddListsTests {

        @Test
        @DisplayName("Add two empty lists returns empty list")
        void addTwoEmptyLists() {
            GroceryList list1 = new GroceryList();
            GroceryList list2 = new GroceryList();

            GroceryList result = GroceryList.addLists(list1, list2);
            assertTrue(result.getItems().isEmpty());
        }

        @Test
        @DisplayName("Add non-empty list with empty list returns copy of non-empty")
        void addNonEmptyWithEmpty() {
            GroceryList list1 = new GroceryList();
            list1.addItem(new GroceryItem(100.0, Unit.GRAM, flour));

            GroceryList list2 = new GroceryList();

            GroceryList result = GroceryList.addLists(list1, list2);
            assertEquals(1, result.getItems().size());
            assertEquals(100.0, result.getItems().get(flour).quantity(), 1e-9);
        }

        @Test
        @DisplayName("Add two lists with different ingredients")
        void addListsDifferentIngredients() {
            GroceryList list1 = new GroceryList();
            list1.addItem(new GroceryItem(100.0, Unit.GRAM, flour));

            GroceryList list2 = new GroceryList();
            list2.addItem(new GroceryItem(200.0, Unit.GRAM, sugar));

            GroceryList result = GroceryList.addLists(list1, list2);
            assertEquals(2, result.getItems().size());
        }

        @Test
        @DisplayName("Add two lists with same ingredient merges quantities")
        void addListsSameIngredientMerges() {
            GroceryList list1 = new GroceryList();
            list1.addItem(new GroceryItem(200.0, Unit.GRAM, flour));

            GroceryList list2 = new GroceryList();
            list2.addItem(new GroceryItem(300.0, Unit.GRAM, flour));

            GroceryList result = GroceryList.addLists(list1, list2);
            assertEquals(1, result.getItems().size());
            assertEquals(500.0, result.getItems().get(flour).quantity(), 1e-9);
        }

        @Test
        @DisplayName("Add two lists with overlapping and unique ingredients")
        void addListsMixed() {
            GroceryList list1 = new GroceryList();
            list1.addItem(new GroceryItem(100.0, Unit.GRAM, flour));
            list1.addItem(new GroceryItem(50.0, Unit.GRAM, sugar));

            GroceryList list2 = new GroceryList();
            list2.addItem(new GroceryItem(200.0, Unit.GRAM, flour));
            list2.addItem(new GroceryItem(1.0, Unit.LITER, milk));

            GroceryList result = GroceryList.addLists(list1, list2);
            assertEquals(3, result.getItems().size());
            assertEquals(300.0, result.getItems().get(flour).quantity(), 1e-9);
        }

        @Test
        @DisplayName("addLists does not modify original lists")
        void addListsDoesNotModifyOriginals() {
            GroceryList list1 = new GroceryList();
            list1.addItem(new GroceryItem(100.0, Unit.GRAM, flour));

            GroceryList list2 = new GroceryList();
            list2.addItem(new GroceryItem(200.0, Unit.GRAM, sugar));

            GroceryList.addLists(list1, list2);

            assertEquals(1, list1.getItems().size());
            assertEquals(1, list2.getItems().size());
        }
    }

    // ==================== sortedGroceryList ====================

    @Nested
    @DisplayName("sortedGroceryList()")
    class SortedGroceryListTests {

        @Test
        @DisplayName("Sorting list orders items by quantity descending (converted to grams)")
        void sortedByQuantityDescending() {
            groceryList.addItem(new GroceryItem(100.0, Unit.GRAM, sugar));
            groceryList.addItem(new GroceryItem(1.0, Unit.KILOGRAM, flour));
            groceryList.addItem(new GroceryItem(50.0, Unit.GRAM, butter));

            groceryList.sortedGroceryList();
            Map<Ingredient, GroceryItem> items = groceryList.getItems();

            // flour (1 KG = 1000g) > sugar (100g) > butter (50g)
            Ingredient[] keys = items.keySet().toArray(new Ingredient[0]);
            assertEquals(flour, keys[0]);
            assertEquals(sugar, keys[1]);
            assertEquals(butter, keys[2]);
        }

        @Test
        @DisplayName("Sorting empty list does not throw")
        void sortEmptyList() {
            assertDoesNotThrow(() -> groceryList.sortedGroceryList());
            assertTrue(groceryList.getItems().isEmpty());
        }

        @Test
        @DisplayName("Sorting single item list keeps item")
        void sortSingleItem() {
            groceryList.addItem(new GroceryItem(500.0, Unit.GRAM, flour));

            assertDoesNotThrow(() -> groceryList.sortedGroceryList());
            assertEquals(1, groceryList.getItems().size());
        }
    }

    // ==================== toString ====================

    @Nested
    @DisplayName("toString()")
    class ToStringTests {

        @Test
        @DisplayName("Empty list toString returns empty string")
        void emptyListToString() {
            assertEquals("", groceryList.toString());
        }

        @Test
        @DisplayName("Single item list toString contains item details")
        void singleItemToString() {
            groceryList.addItem(new GroceryItem(500.0, Unit.GRAM, flour));

            String result = groceryList.toString();
            assertTrue(result.contains("500.0"));
            assertTrue(result.contains("gram"));
            assertTrue(result.contains("flour"));
        }
    }

    // ==================== getItems ====================

    @Nested
    @DisplayName("getItems()")
    class GetItemsTests {

        @Test
        @DisplayName("getItems returns the items map")
        void getItemsReturnsMap() {
            groceryList.addItem(new GroceryItem(500.0, Unit.GRAM, flour));
            groceryList.addItem(new GroceryItem(200.0, Unit.GRAM, sugar));

            Map<Ingredient, GroceryItem> items = groceryList.getItems();
            assertEquals(2, items.size());
            assertTrue(items.containsKey(flour));
            assertTrue(items.containsKey(sugar));
        }
    }
}
