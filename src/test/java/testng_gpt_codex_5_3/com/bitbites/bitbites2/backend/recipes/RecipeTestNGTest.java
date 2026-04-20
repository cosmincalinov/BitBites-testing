package testng_gpt_codex_5_3.com.bitbites.bitbites2.backend.recipes;

import com.bitbites.bitbites2.backend.groceries.GroceryItem;
import com.bitbites.bitbites2.backend.groceries.Ingredient;
import com.bitbites.bitbites2.backend.groceries.Unit;
import com.bitbites.bitbites2.backend.recipes.Appetizer;
import com.bitbites.bitbites2.backend.recipes.Recipe;
import org.testng.annotations.Test;

import java.net.URI;
import java.time.Duration;

import static org.testng.Assert.*;

public class RecipeTestNGTest {

    @Test
    public void shouldSetDurationAndIngredients() throws Exception {
        Recipe recipe = new Appetizer("Toast", "romanian", URI.create("https://example.com/a").toURL(), 100, 1);
        recipe.setDuration(90, "minutes");
        recipe.addIngredient(new GroceryItem(2, Unit.PIECE, new Ingredient("Bread", "Bakery")));

        assertEquals(recipe.getDuration(), Duration.ofMinutes(90));
        assertEquals(recipe.formatDuration(), "1 hour 30 minutes");
        assertEquals(recipe.getIngredients().getItems().size(), 1);
    }
}
