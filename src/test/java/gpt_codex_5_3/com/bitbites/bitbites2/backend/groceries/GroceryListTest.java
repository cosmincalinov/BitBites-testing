package gpt_codex_5_3.com.bitbites.bitbites2.backend.groceries;

import com.bitbites.bitbites2.backend.groceries.GroceryItem;
import com.bitbites.bitbites2.backend.groceries.GroceryList;
import com.bitbites.bitbites2.backend.groceries.Ingredient;
import com.bitbites.bitbites2.backend.groceries.Unit;

import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class GroceryListTest {

    @Test
    void constructorShouldCreateEmptyList() {
        GroceryList list = new GroceryList();

        assertTrue(list.getItems().isEmpty());
    }

    @Test
    void addItemShouldInsertNewIngredient() {
        GroceryList list = new GroceryList();
        Ingredient sugar = new Ingredient("Sugar", "Baking");

        list.addItem(new GroceryItem(100, Unit.GRAM, sugar));

        assertEquals(1, list.getItems().size());
        assertEquals(100, list.getItems().get(sugar).quantity(), 1e-9);
    }

    @Test
    void addItemShouldMergeWhenUnitsAreCompatible() {
        GroceryList list = new GroceryList();
        Ingredient sugar = new Ingredient("Sugar", "Baking");

        list.addItem(new GroceryItem(500, Unit.GRAM, sugar));
        list.addItem(new GroceryItem(0.5, Unit.KILOGRAM, sugar));

        GroceryItem merged = list.getItems().get(sugar);
        assertNotNull(merged);
        assertEquals(Unit.GRAM, merged.unit());
        assertEquals(1000.0, merged.quantity(), 1e-9);
    }

    @Test
    void addItemShouldReplaceWhenConversionFails() {
        GroceryList list = new GroceryList();
        Ingredient eggs = new Ingredient("Eggs", "Dairy");

        list.addItem(new GroceryItem(200, Unit.GRAM, eggs));
        list.addItem(new GroceryItem(6, Unit.PIECE, eggs));

        GroceryItem result = list.getItems().get(eggs);
        assertNotNull(result);
        assertEquals(Unit.PIECE, result.unit());
        assertEquals(6.0, result.quantity(), 1e-9);
    }

    @Test
    void addListsShouldContainItemsFromBothLists() {
        Ingredient sugar = new Ingredient("Sugar", "Baking");
        Ingredient milk = new Ingredient("Milk", "Dairy");

        GroceryList first = new GroceryList();
        first.addItem(new GroceryItem(100, Unit.GRAM, sugar));

        GroceryList second = new GroceryList();
        second.addItem(new GroceryItem(1, Unit.LITER, milk));

        GroceryList sum = GroceryList.addLists(first, second);

        assertEquals(2, sum.getItems().size());
        assertTrue(sum.getItems().containsKey(sugar));
        assertTrue(sum.getItems().containsKey(milk));
    }

    @Test
    void sortedGroceryListShouldSortByConvertedGramQuantityDescending() {
        GroceryList list = new GroceryList();
        Ingredient flour = new Ingredient("Flour", "Baking");
        Ingredient sugar = new Ingredient("Sugar", "Baking");
        Ingredient rice = new Ingredient("Rice", "Grains");

        list.addItem(new GroceryItem(100, Unit.GRAM, flour));
        list.addItem(new GroceryItem(1, Unit.KILOGRAM, sugar));
        list.addItem(new GroceryItem(1, Unit.CUP, rice));

        list.sortedGroceryList();

        Iterator<Ingredient> iterator = list.getItems().keySet().iterator();
        assertEquals(sugar, iterator.next());
        assertEquals(rice, iterator.next());
        assertEquals(flour, iterator.next());
    }
}
