package gpt_codex_5_3.com.bitbites.bitbites2.backend.recipes;

import com.bitbites.bitbites2.backend.groceries.GroceryItem;
import com.bitbites.bitbites2.backend.groceries.GroceryList;
import com.bitbites.bitbites2.backend.groceries.Ingredient;
import com.bitbites.bitbites2.backend.groceries.Unit;
import com.bitbites.bitbites2.backend.recipes.Appetizer;
import com.bitbites.bitbites2.backend.recipes.Recipe;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class RecipeTest {

    @Test
    void constructorShouldInitializeFields() throws Exception {
        URL instructions = new URL("https://example.com/video");
        Recipe recipe = new Appetizer("Bruschetta", "italian", instructions, 180.5, 2);

        assertEquals("Bruschetta", recipe.getName());
        assertEquals("Appetizer", recipe.getCategoryFood());
        assertEquals("italian", recipe.getKitchenType());
        assertEquals(180.5, recipe.getKilocalories(), 1e-9);
        assertEquals(2, recipe.getServings());
        assertEquals(instructions, recipe.getInstructions());
        assertNotNull(recipe.getIngredients());
    }

    @Test
    void addIngredientShouldStoreItemInGroceryList() throws Exception {
        Recipe recipe = new Appetizer("Toast", "romanian", new URL("https://example.com/a"), 100, 1);
        Ingredient bread = new Ingredient("Bread", "Bakery");

        recipe.addIngredient(new GroceryItem(2, Unit.PIECE, bread));

        assertEquals(1, recipe.getIngredients().getItems().size());
        assertEquals(2, recipe.getIngredients().getItems().get(bread).quantity(), 1e-9);
    }

    @Test
    void setDurationShouldHandleKnownUnits() throws Exception {
        Recipe recipe = new Appetizer("Toast", "romanian", new URL("https://example.com/a"), 100, 1);

        recipe.setDuration(2, "hours");
        assertEquals(Duration.ofHours(2), recipe.getDuration());

        recipe.setDuration(30, "minute");
        assertEquals(Duration.ofMinutes(30), recipe.getDuration());

        recipe.setDuration(45, "s");
        assertEquals(Duration.ofSeconds(45), recipe.getDuration());
    }

    @Test
    void setDurationShouldSetNullForUnknownUnits() throws Exception {
        Recipe recipe = new Appetizer("Toast", "romanian", new URL("https://example.com/a"), 100, 1);

        recipe.setDuration(3, "days");

        assertNull(recipe.getDuration());
    }

    @Test
    void formatDurationShouldReturnHumanReadableValue() throws Exception {
        Recipe recipe = new Appetizer("Toast", "romanian", new URL("https://example.com/a"), 100, 1);
        recipe.setDuration(Duration.ofMinutes(90));

        assertEquals("1 hour 30 minutes", recipe.formatDuration());
    }

    @Test
    void setGroceryListShouldReplaceIngredientsList() throws Exception {
        Recipe recipe = new Appetizer("Toast", "romanian", new URL("https://example.com/a"), 100, 1);
        GroceryList custom = new GroceryList();
        custom.addItem(new GroceryItem(300, Unit.GRAM, new Ingredient("Flour", "Baking")));

        recipe.setGroceryList(custom);

        assertSame(custom, recipe.getIngredients());
    }
}
