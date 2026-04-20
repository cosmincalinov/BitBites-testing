package testng_gpt_codex_5_3.com.bitbites.bitbites2.backend.groceries;

import com.bitbites.bitbites2.backend.groceries.GroceryItem;
import com.bitbites.bitbites2.backend.groceries.GroceryList;
import com.bitbites.bitbites2.backend.groceries.Ingredient;
import com.bitbites.bitbites2.backend.groceries.Unit;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class GroceryListTestNGTest {

    @Test
    public void shouldMergeCompatibleItems() {
        GroceryList list = new GroceryList();
        Ingredient sugar = new Ingredient("Sugar", "Baking");

        list.addItem(new GroceryItem(500, Unit.GRAM, sugar));
        list.addItem(new GroceryItem(0.5, Unit.KILOGRAM, sugar));

        GroceryItem merged = list.getItems().get(sugar);
        assertNotNull(merged);
        assertEquals(merged.unit(), Unit.GRAM);
        assertEquals(merged.quantity(), 1000.0, 1e-9);
    }

    @Test
    public void shouldAddLists() {
        GroceryList first = new GroceryList();
        GroceryList second = new GroceryList();
        Ingredient rice = new Ingredient("Rice", "Grains");
        Ingredient milk = new Ingredient("Milk", "Dairy");

        first.addItem(new GroceryItem(1, Unit.CUP, rice));
        second.addItem(new GroceryItem(1, Unit.LITER, milk));

        GroceryList sum = GroceryList.addLists(first, second);
        assertEquals(sum.getItems().size(), 2);
        assertTrue(sum.getItems().containsKey(rice));
        assertTrue(sum.getItems().containsKey(milk));
    }
}
