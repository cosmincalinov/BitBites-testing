package com.bitbites.bitbites2.backend.RecipeFactoryTest;

import com.bitbites.bitbites2.backend.recipes.Recipe;
import com.bitbites.bitbites2.backend.recipes.RecipeFactory;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.Duration;

/**
 * Acoperire la nivel de conditie (MC/DC) si circuite independente
 * pentru RecipeFactory.createCustomRecipe(...)
 *
 * ================================================================
 * COMPLEXITATE CICLOMATICA - V(G) = 31
 * (vezi BoundaryValueTest / Mermaid CFG pentru detalii)
 * ================================================================
 *
 * MC/DC (Modified Condition/Decision Coverage):
 *   Fiecare conditie dintr-o decizie compusa trebuie sa influenteze independent
 *   rezultatul deciziei.
 *
 *   C1: name == null || name.trim().isEmpty()
 *       C1a: name == null -> true  (short-circuit, isEmpty nu e evaluata)
 *       C1b: name != null, isEmpty() -> true
 *       C1c: name != null, isEmpty() -> false (decizie globala: false)
 *
 *   C2: categoryFood == null || categoryFood.trim().isEmpty()
 *       C2a: categoryFood == null -> true
 *       C2b: categoryFood != null, isEmpty() -> true
 *       C2c: categoryFood != null, isEmpty() -> false
 *
 *   C3: kitchenType == null || kitchenType.trim().isEmpty()
 *       C3a: kitchenType == null -> true
 *       C3b: kitchenType != null, isEmpty() -> true
 *       C3c: kitchenType != null, isEmpty() -> false
 *
 * CIRCUITE INDEPENDENTE (Independent Paths):
 *   IP1-IP15:  cai care duc la exceptie (validari)
 *   IP16-IP23: cai de succes prin fiecare categorie de reteta
 */
public class ConditionPathTest {

    // ================================================================
    // MC/DC — C1: name == null || name.trim().isEmpty()
    // ================================================================

    // C1a: name == null -> true (short-circuit)
    @Test
    public void mcdcC1a_nameNull_conditionTrue() {
        assertThrows(IllegalArgumentException.class, () ->
                RecipeFactory.createCustomRecipe(null, "Soup", "Italian", 300, 4));
    }

    // C1b: name != null, name.trim().isEmpty() -> true
    @Test
    public void mcdcC1b_nameBlank_secondConditionTrue() {
        assertThrows(IllegalArgumentException.class, () ->
                RecipeFactory.createCustomRecipe("   ", "Soup", "Italian", 300, 4));
    }

    // C1c: name != null, name.trim().isEmpty() -> false -> decizie globală: false
    @Test
    public void mcdcC1c_nameValid_conditionFalse() {
        Recipe r = RecipeFactory.createCustomRecipe("Pasta", "MainCourse", "Italian", 500, 4);
        assertNotNull(r);
    }

    // ================================================================
    // MC/DC — C2: categoryFood == null || categoryFood.trim().isEmpty()
    // ================================================================

    // C2a: categoryFood == null -> true
    @Test
    public void mcdcC2a_categoryNull_conditionTrue() {
        assertThrows(IllegalArgumentException.class, () ->
                RecipeFactory.createCustomRecipe("Pasta", null, "Italian", 500, 4));
    }

    // C2b: categoryFood != null, isEmpty() -> true
    @Test
    public void mcdcC2b_categoryBlank_secondConditionTrue() {
        assertThrows(IllegalArgumentException.class, () ->
                RecipeFactory.createCustomRecipe("Pasta", "  ", "Italian", 500, 4));
    }

    // C2c: categoryFood != null, isEmpty() -> false
    @Test
    public void mcdcC2c_categoryValid_conditionFalse() {
        Recipe r = RecipeFactory.createCustomRecipe("Pasta", "MainCourse", "Italian", 500, 4);
        assertNotNull(r);
    }

    // ================================================================
    // MC/DC — C3: kitchenType == null || kitchenType.trim().isEmpty()
    // ================================================================

    // C3a: kitchenType == null -> true
    @Test
    public void mcdcC3a_kitchenTypeNull_conditionTrue() {
        assertThrows(IllegalArgumentException.class, () ->
                RecipeFactory.createCustomRecipe("Pasta", "MainCourse", null, 500, 4));
    }

    // C3b: kitchenType != null, isEmpty() -> true
    @Test
    public void mcdcC3b_kitchenTypeBlank_secondConditionTrue() {
        assertThrows(IllegalArgumentException.class, () ->
                RecipeFactory.createCustomRecipe("Pasta", "MainCourse", "  ", 500, 4));
    }

    // C3c: kitchenType != null, isEmpty() -> false
    @Test
    public void mcdcC3c_kitchenTypeValid_conditionFalse() {
        Recipe r = RecipeFactory.createCustomRecipe("Pasta", "MainCourse", "Italian", 500, 4);
        assertNotNull(r);
    }

    // ================================================================
    // CIRCUITE INDEPENDENTE (Independent Paths)
    // ================================================================

    // IP1: name null → exceptie
    @Test
    public void ip1_nameNull_exception() {
        assertThrows(IllegalArgumentException.class, () ->
                RecipeFactory.createCustomRecipe(null, "Soup", "Italian", 300, 4));
    }

    // IP2: name blank -> exceptie
    @Test
    public void ip2_nameBlank_exception() {
        assertThrows(IllegalArgumentException.class, () ->
                RecipeFactory.createCustomRecipe("", "Soup", "Italian", 300, 4));
    }

    // IP3: name > 100 caractere -> exceptie
    @Test
    public void ip3_nameTooLong_exception() {
        assertThrows(IllegalArgumentException.class, () ->
                RecipeFactory.createCustomRecipe("A".repeat(101), "Soup", "Italian", 300, 4));
    }

    // IP4: categoryFood null -> exceptie
    @Test
    public void ip4_categoryNull_exception() {
        assertThrows(IllegalArgumentException.class, () ->
                RecipeFactory.createCustomRecipe("Soup", null, "Italian", 300, 4));
    }

    // IP5: categoryFood blank -> exceptie
    @Test
    public void ip5_categoryBlank_exception() {
        assertThrows(IllegalArgumentException.class, () ->
                RecipeFactory.createCustomRecipe("Soup", "", "Italian", 300, 4));
    }

    // IP6: alias "Starter" -> "Appetizer" (calea de alias 1)
    @Test
    public void ip6_aliasStarter_mapsToAppetizer() {
        Recipe r = RecipeFactory.createCustomRecipe("Spring Rolls", "Starter", "Asian", 150, 2);
        assertEquals("Appetizer", r.getCategoryFood());
    }

    // IP7: alias "Main" -> "MainCourse" (calea de alias 2)
    @Test
    public void ip7_aliasMain_mapsToMainCourse() {
        Recipe r = RecipeFactory.createCustomRecipe("Steak", "Main", "French", 700, 2);
        assertEquals("MainCourse", r.getCategoryFood());
    }

    // IP8: alias "Beverage" -> "Drink" (calea de alias 3)
    @Test
    public void ip8_aliasBeverage_mapsToDrink() {
        Recipe r = RecipeFactory.createCustomRecipe("Lemonade", "Beverage", "Universal", 100, 1);
        assertEquals("Drink", r.getCategoryFood());
    }

    // IP9: categoryFood invalida -> exceptie
    @Test
    public void ip9_categoryInvalid_exception() {
        assertThrows(IllegalArgumentException.class, () ->
                RecipeFactory.createCustomRecipe("Tacos", "Mexican", "Mexican", 450, 3));
    }

    // IP10: kitchenType null -> exceptie
    @Test
    public void ip10_kitchenTypeNull_exception() {
        assertThrows(IllegalArgumentException.class, () ->
                RecipeFactory.createCustomRecipe("Pasta", "MainCourse", null, 500, 4));
    }

    // IP11: kitchenType blank -> exceptie
    @Test
    public void ip11_kitchenTypeBlank_exception() {
        assertThrows(IllegalArgumentException.class, () ->
                RecipeFactory.createCustomRecipe("Pasta", "MainCourse", "  ", 500, 4));
    }

    // IP12: kilocalories <= 0 -> exceptie
    @Test
    public void ip12_kcalNegative_exception() {
        assertThrows(IllegalArgumentException.class, () ->
                RecipeFactory.createCustomRecipe("Dish", "MainCourse", "Italian", -100, 4));
    }

    // IP13: kilocalories > 5000 -> exceptie
    @Test
    public void ip13_kcalAbove5000_exception() {
        assertThrows(IllegalArgumentException.class, () ->
                RecipeFactory.createCustomRecipe("Dish", "MainCourse", "Italian", 6000, 4));
    }

    // IP14: servings < 1 -> exceptie
    @Test
    public void ip14_servingsZero_exception() {
        assertThrows(IllegalArgumentException.class, () ->
                RecipeFactory.createCustomRecipe("Dish", "MainCourse", "Italian", 500, 0));
    }

    // IP15: servings > 50 -> exceptie
    @Test
    public void ip15_servingsAbove50_exception() {
        assertThrows(IllegalArgumentException.class, () ->
                RecipeFactory.createCustomRecipe("Dish", "MainCourse", "Italian", 500, 51));
    }

    // IP16: Drink — kcal * 0.8, durata 10 min
    @Test
    public void ip16_drinkPath_kcalAdjustedAndDurationSet() {
        Recipe r = RecipeFactory.createCustomRecipe("Juice", "Drink", "Universal", 500, 2);
        assertEquals(400.0, r.getKilocalories(), 0.001);
        assertEquals(Duration.ofMinutes(10), r.getDuration());
    }

    // IP17: Salad — kcal * 0.9, durata 15 min
    @Test
    public void ip17_saladPath_kcalAdjustedAndDurationSet() {
        Recipe r = RecipeFactory.createCustomRecipe("Caesar", "Salad", "American", 200, 2);
        assertEquals(180.0, r.getKilocalories(), 0.001);
        assertEquals(Duration.ofMinutes(15), r.getDuration());
    }

    // IP18: Dessert — kcal * 1.15, fara cap, durata 60 min
    @Test
    public void ip18_dessertNoCap_kcalAdjustedAndDurationSet() {
        Recipe r = RecipeFactory.createCustomRecipe("Cake", "Dessert", "French", 400, 4);
        assertEquals(460.0, r.getKilocalories(), 0.001);
        assertEquals(Duration.ofMinutes(60), r.getDuration());
    }

    // IP19: Dessert — kcal * 1.15 > 5000, cap aplicat
    @Test
    public void ip19_dessertWithCap_kcalCappedAt5000() {
        Recipe r = RecipeFactory.createCustomRecipe("Cake", "Dessert", "French", 4400, 4);
        assertEquals(5000.0, r.getKilocalories(), 0.001);
    }

    // IP20: Appetizer — fara ajustare kcal, durata 20 min
    @Test
    public void ip20_appetizerPath_noKcalAdjustAndDurationSet() {
        Recipe r = RecipeFactory.createCustomRecipe("Bruschetta", "Appetizer", "Italian", 150, 4);
        assertEquals(150.0, r.getKilocalories(), 0.001);
        assertEquals(Duration.ofMinutes(20), r.getDuration());
    }

    // IP21: Bread — fara ajustare kcal, durata 90 min
    @Test
    public void ip21_breadPath_noKcalAdjustAndDurationSet() {
        Recipe r = RecipeFactory.createCustomRecipe("Baguette", "Bread", "French", 250, 8);
        assertEquals(250.0, r.getKilocalories(), 0.001);
        assertEquals(Duration.ofMinutes(90), r.getDuration());
    }

    // IP22: MainCourse — fara ajustare kcal, durata 40 min
    @Test
    public void ip22_mainCoursePath_noKcalAdjustAndDurationSet() {
        Recipe r = RecipeFactory.createCustomRecipe("Steak", "MainCourse", "French", 700, 2);
        assertEquals(700.0, r.getKilocalories(), 0.001);
        assertEquals(Duration.ofMinutes(40), r.getDuration());
    }

    // IP23: Soup — fara ajustare kcal, durata 45 min
    @Test
    public void ip23_soupPath_noKcalAdjustAndDurationSet() {
        Recipe r = RecipeFactory.createCustomRecipe("Minestrone", "Soup", "Italian", 300, 4);
        assertEquals(300.0, r.getKilocalories(), 0.001);
        assertEquals(Duration.ofMinutes(45), r.getDuration());
    }
}
