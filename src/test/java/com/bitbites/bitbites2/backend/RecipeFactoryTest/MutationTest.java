package com.bitbites.bitbites2.backend.RecipeFactoryTest;

import com.bitbites.bitbites2.backend.recipes.Recipe;
import com.bitbites.bitbites2.backend.recipes.RecipeFactory;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Teste de mutanti pentru RecipeFactory.createCustomRecipe(...)
 *
 * Raport mutanti - mutanti neechivalenti vizati:
 *
 * M1: kilocalories <= 0  ->  kilocalories < 0
 *     Efect: kcal=0 ar trece validarea (GRESIT).
 *     Test killer: verifica ca kcal=0.0 arunca exceptie.
 *
 * M2: kilocalories > 5000  ->  kilocalories >= 5000
 *     Efect: kcal=5000 ar arunca exceptie (GRESIT).
 *     Test killer: verifica ca kcal=5000 este valid.
 *
 * M3: servings < 1  ->  servings <= 1
 *     Efect: servings=1 ar arunca exceptie (GRESIT).
 *     Test killer: verifica ca servings=1 este valid.
 *
 * M4: servings > 50  ->  servings >= 50
 *     Efect: servings=50 ar arunca exceptie (GRESIT).
 *     Test killer: verifica ca servings=50 este valid.
 *
 * M5: name.length() > 100  ->  name.length() >= 100
 *     Efect: name de exact 100 caractere ar arunca exceptie (GRESIT).
 *     Test killer: verifica ca name cu 100 caractere este valid.
 *
 * M6: kilocalories * 0.8  ->  kilocalories * 0.9  (Drink)
 *     Efect: kcal pentru Drink ar fi calculat gresit.
 *     Test killer: verifica valoarea exacta 1000 * 0.8 = 800.
 *
 * M7: kilocalories * 0.9  ->  kilocalories * 0.8  (Salad)
 *     Efect: kcal pentru Salad ar fi calculat gresit.
 *     Test killer: verifica valoarea exacta 1000 * 0.9 = 900.
 *
 * M8: kilocalories * 1.15  ->  kilocalories * 1.0  (Dessert)
 *     Efect: kcal pentru Dessert nu ar fi majorat.
 *     Test killer: verifica ca 1000 * 1.15 = 1150, nu 1000.
 *
 * M9: adjustedKcal > 5000  ->  adjustedKcal >= 5000  (cap Dessert)
 *     Efect: cap aplicat si cand kcal*1.15 == exact 5000.
 *     Test killer: verifica ca kcal sub prag nu este capsat.
 *
 * M10: Eliminarea alias-ului "Starter" -> "Appetizer"
 *      Efect: "Starter" ar produce exceptie in loc sa fie mapata.
 *      Test killer: verifica ca "Starter" produce un Appetizer valid.
 *
 * M11 (suplimentar): Eliminarea trim() pentru name
 *      Efect: name-ul ar retine spatiile.
 *      Test killer: verifica ca " Pasta " devine "Pasta".
 *
 * M12 (suplimentar): Eliminarea alias-ului "Entree" -> "MainCourse"
 *      Efect: "Entree" ar produce exceptie.
 *      Test killer: verifica ca "Entree" produce un MainCourse valid.
 */
public class MutationTest {

    // M1: Ucide mutantul <= -> < pentru kilocalories (valoarea 0 trebuie sa arunce exceptie)
    @Test
    public void mt1_killMutant_kcalZeroMustThrow() {
        assertThrows(IllegalArgumentException.class, () ->
                RecipeFactory.createCustomRecipe("Dish", "MainCourse", "Italian", 0.0, 4));
    }

    // M2: Ucide mutantul > -> >= pentru kilocalories max (valoarea 5000 trebuie sa fie valida)
    @Test
    public void mt2_killMutant_kcal5000MustBeValid() {
        Recipe r = RecipeFactory.createCustomRecipe("Dish", "MainCourse", "Italian", 5000.0, 4);
        assertNotNull(r);
        assertEquals(5000.0, r.getKilocalories(), 0.001);
    }

    // M3: Ucide mutantul < -> <= pentru servings min (servings=1 trebuie sa fie valid)
    @Test
    public void mt3_killMutant_servings1MustBeValid() {
        Recipe r = RecipeFactory.createCustomRecipe("Dish", "MainCourse", "Italian", 500, 1);
        assertNotNull(r);
        assertEquals(1, r.getServings());
    }

    // M4: Ucide mutantul > -> >= pentru servings max (servings=50 trebuie sa fie valid)
    @Test
    public void mt4_killMutant_servings50MustBeValid() {
        Recipe r = RecipeFactory.createCustomRecipe("Dish", "MainCourse", "Italian", 500, 50);
        assertNotNull(r);
        assertEquals(50, r.getServings());
    }

    // M5: Ucide mutantul > -> >= pentru name.length() (lungime exacta 100 trebuie sa fie valida)
    @Test
    public void mt5_killMutant_nameLength100MustBeValid() {
        String name = "A".repeat(100);
        Recipe r = RecipeFactory.createCustomRecipe(name, "Soup", "Italian", 300, 4);
        assertNotNull(r);
        assertEquals(name, r.getName());
    }

    // M6: Ucide mutantul * 0.8 -> * 0.9 pentru Drink
    @Test
    public void mt6_killMutant_drinkKcalMultiplierIs08() {
        Recipe r = RecipeFactory.createCustomRecipe("Tea", "Drink", "Universal", 1000, 2);
        // Trebuie 800 (0.8), nu 900 (0.9)
        assertEquals(800.0, r.getKilocalories(), 0.001);
        assertNotEquals(900.0, r.getKilocalories(), 0.001);
    }

    // M7: Ucide mutantul * 0.9 -> * 0.8 pentru Salad
    @Test
    public void mt7_killMutant_saladKcalMultiplierIs09() {
        Recipe r = RecipeFactory.createCustomRecipe("Salata", "Salad", "Romanian", 1000, 2);
        // Trebuie 900 (0.9), nu 800 (0.8)
        assertEquals(900.0, r.getKilocalories(), 0.001);
        assertNotEquals(800.0, r.getKilocalories(), 0.001);
    }

    // M8: Ucide mutantul * 1.15 -> * 1.0 pentru Dessert
    @Test
    public void mt8_killMutant_dessertKcalMultiplierIs115() {
        Recipe r = RecipeFactory.createCustomRecipe("Cake", "Dessert", "French", 1000, 4);
        // Trebuie 1150 (1.15), nu 1000 (1.0)
        assertEquals(1150.0, r.getKilocalories(), 0.001);
        assertNotEquals(1000.0, r.getKilocalories(), 0.001);
    }

    // M9: Ucide mutantul > -> >= pentru capul Dessert la 5000
    // (4347 * 1.15 = 4999.05 trebuie să nu fie capsat)
    @Test
    public void mt9_killMutant_dessertCapBoundaryIsStrictlyGreater() {
        // 4347 * 1.15 = 4999.05 — sub 5000, nu trebuie capsat
        Recipe r = RecipeFactory.createCustomRecipe("Cake", "Dessert", "French", 4347, 4);
        double expected = 4347 * 1.15;
        assertEquals(expected, r.getKilocalories(), 0.01);
        // Dacă mutantul schimbă > în >=, ar capsa la 5000 (greșit)
        assertTrue(r.getKilocalories() < 5000.0);
    }

    // M10: Ucide mutantul care elimina alias-ul "Starter" → "Appetizer"
    @Test
    public void mt10_killMutant_aliasStarterMapsToAppetizer() {
        Recipe r = RecipeFactory.createCustomRecipe("Bruschetta", "Starter", "Italian", 150, 4);
        assertNotNull(r);
        assertEquals("Appetizer", r.getCategoryFood());
    }

    // M11 (suplimentar): Ucide mutantul care elimina trim() pentru name
    @Test
    public void mt11_killMutant_nameTrimIsApplied() {
        Recipe r = RecipeFactory.createCustomRecipe("  Pasta  ", "MainCourse", "Italian", 500, 4);
        assertNotNull(r);
        assertEquals("Pasta", r.getName()); // Spațiile trebuie eliminate
    }

    // M12 (suplimentar): Ucide mutantul care elimină alias-ul "Entree" -> "MainCourse"
    @Test
    public void mt12_killMutant_aliasEntreeMapsToMainCourse() {
        Recipe r = RecipeFactory.createCustomRecipe("Steak", "Entree", "French", 700, 2);
        assertNotNull(r);
        assertEquals("MainCourse", r.getCategoryFood());
    }
}