package com.bitbites.bitbites2.backend.RecipeFactoryTest;

import com.bitbites.bitbites2.backend.recipes.Recipe;
import com.bitbites.bitbites2.backend.recipes.RecipeFactory;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Acoperire la nivel de instructiune (Statement Coverage) si decizie (Decision Coverage)
 * pentru RecipeFactory.createCustomRecipe(...)
 *
 * Statement Coverage - fiecare instructiune trebuie executata cel putin o data:
 *   SC1:  Calea fericita (Soup, parametri valizi, fara ajustare kcal)
 *   SC2:  Ramura exceptie name == null
 *   SC3:  Ramura exceptie name.length() > 100
 *   SC4:  Ramura exceptie categoryFood == null
 *   SC5:  Ramura alias "Main" -> "MainCourse"
 *   SC6:  Ramura alias "Beverage" -> "Drink"
 *   SC7:  Ramura exceptie categorie invalida
 *   SC8:  Ramura exceptie kitchenType == null
 *   SC9:  Ramura exceptie kilocalories <= 0
 *   SC10: Ramura exceptie kilocalories > 5000
 *   SC11: Ramura exceptie servings < 1
 *   SC12: Ramura exceptie servings > 50
 *   SC13: Ramura ajustare kcal pentru Drink (kcal * 0.8)
 *   SC14: Ramura ajustare kcal pentru Salad (kcal * 0.9)
 *   SC15: Ramura ajustare kcal pentru Dessert fara cap (kcal * 1.15 <= 5000)
 *   SC16: Ramura ajustare kcal pentru Dessert cu cap (kcal * 1.15 > 5000)
 *
 * Decision Coverage - fiecare decizie trebuie evaluata atat true cat si false:
 *   DC1-DC2:   if (name == null || ...) -> true / false
 *   DC3-DC4:   if (name.length() > 100) -> true / false
 *   DC5-DC6:   if (kilocalories <= 0) -> true / false
 *   DC7-DC8:   if (kilocalories > 5000) -> true / false
 *   DC9-DC10:  if (servings < 1) -> true / false
 *   DC11-DC12: if (servings > 50) -> true / false
 *   DC13-DC14: if (adjustedKcal > 5000) in ramura Dessert -> true / false
 */
public class CoverageTest {

    // === STATEMENT COVERAGE ===

    // SC1: Calea fericita — Soup
    @Test
    public void sc1_happyPathSoup_allMainStatementsExecuted() {
        Recipe r = RecipeFactory.createCustomRecipe("Minestrone", "Soup", "Italian", 300, 4);
        assertNotNull(r);
        assertEquals("Minestrone", r.getName());
        assertEquals("Soup", r.getCategoryFood());
        assertEquals("Italian", r.getKitchenType());
        assertEquals(300.0, r.getKilocalories(), 0.001);
        assertEquals(4, r.getServings());
        assertNotNull(r.getDuration());
    }

    // SC2: Ramura name null
    @Test
    public void sc2_nameNull_exceptionBranch() {
        assertThrows(IllegalArgumentException.class, () ->
                RecipeFactory.createCustomRecipe(null, "Soup", "Italian", 300, 4));
    }

    // SC3: Ramura name > 100 caractere
    @Test
    public void sc3_nameTooLong_exceptionBranch() {
        assertThrows(IllegalArgumentException.class, () ->
                RecipeFactory.createCustomRecipe("A".repeat(101), "Soup", "Italian", 300, 4));
    }

    // SC4: Ramura categoryFood null
    @Test
    public void sc4_categoryNull_exceptionBranch() {
        assertThrows(IllegalArgumentException.class, () ->
                RecipeFactory.createCustomRecipe("Soup", null, "Italian", 300, 4));
    }

    // SC5: Alias "Main" -> "MainCourse"
    @Test
    public void sc5_aliasMain_mappingExecuted() {
        Recipe r = RecipeFactory.createCustomRecipe("Steak", "Main", "French", 700, 2);
        assertNotNull(r);
        assertEquals("MainCourse", r.getCategoryFood());
    }

    // SC6: Alias "Beverage" -> "Drink"
    @Test
    public void sc6_aliasBeverage_mappingExecuted() {
        Recipe r = RecipeFactory.createCustomRecipe("Lemonade", "Beverage", "Universal", 100, 1);
        assertNotNull(r);
        assertEquals("Drink", r.getCategoryFood());
    }

    // SC7: Ramura categorie invalida
    @Test
    public void sc7_categoryInvalid_exceptionBranch() {
        assertThrows(IllegalArgumentException.class, () ->
                RecipeFactory.createCustomRecipe("Test", "Burger", "American", 500, 2));
    }

    // SC8: Ramura kitchenType null
    @Test
    public void sc8_kitchenTypeNull_exceptionBranch() {
        assertThrows(IllegalArgumentException.class, () ->
                RecipeFactory.createCustomRecipe("Soup", "Soup", null, 300, 4));
    }

    // SC9: Ramura kilocalories <= 0
    @Test
    public void sc9_kilocaloriesZero_exceptionBranch() {
        assertThrows(IllegalArgumentException.class, () ->
                RecipeFactory.createCustomRecipe("Soup", "Soup", "Italian", 0, 4));
    }

    // SC10: Ramura kilocalories > 5000
    @Test
    public void sc10_kilocaloriesTooHigh_exceptionBranch() {
        assertThrows(IllegalArgumentException.class, () ->
                RecipeFactory.createCustomRecipe("Soup", "Soup", "Italian", 5001, 4));
    }

    // SC11: Ramura servings < 1
    @Test
    public void sc11_servingsZero_exceptionBranch() {
        assertThrows(IllegalArgumentException.class, () ->
                RecipeFactory.createCustomRecipe("Soup", "Soup", "Italian", 300, 0));
    }

    // SC12: Ramura servings > 50
    @Test
    public void sc12_servingsTooHigh_exceptionBranch() {
        assertThrows(IllegalArgumentException.class, () ->
                RecipeFactory.createCustomRecipe("Soup", "Soup", "Italian", 300, 51));
    }

    // SC13: Ajustare kcal Drink (kcal * 0.8)
    @Test
    public void sc13_drinkKcalAdjustmentBranch() {
        Recipe r = RecipeFactory.createCustomRecipe("Juice", "Drink", "Universal", 500, 2);
        assertNotNull(r);
        assertEquals(400.0, r.getKilocalories(), 0.001); // 500 * 0.8
    }

    // SC14: Ajustare kcal Salad (kcal * 0.9)
    @Test
    public void sc14_saladKcalAdjustmentBranch() {
        Recipe r = RecipeFactory.createCustomRecipe("Caesar", "Salad", "American", 200, 2);
        assertNotNull(r);
        assertEquals(180.0, r.getKilocalories(), 0.001); // 200 * 0.9
    }

    // SC15: Ajustare kcal Dessert fara cap (kcal * 1.15 ≤ 5000)
    @Test
    public void sc15_dessertKcalAdjustedNoCap() {
        Recipe r = RecipeFactory.createCustomRecipe("Cake", "Dessert", "French", 400, 4);
        assertNotNull(r);
        assertEquals(460.0, r.getKilocalories(), 0.001); // 400 * 1.15
    }

    // SC16: Ajustare kcal Dessert cu cap la 5000 (kcal * 1.15 > 5000)
    @Test
    public void sc16_dessertKcalCappedAt5000() {
        Recipe r = RecipeFactory.createCustomRecipe("Cake", "Dessert", "French", 4400, 4);
        assertNotNull(r);
        assertEquals(5000.0, r.getKilocalories(), 0.001); // 4400 * 1.15 = 5060 → cap
    }

    // === DECISION COVERAGE ===

    // DC1: name null -> decizie true (short-circuit)
    @Test
    public void dc1_nameNullDecisionTrue() {
        assertThrows(IllegalArgumentException.class, () ->
                RecipeFactory.createCustomRecipe(null, "Soup", "Italian", 300, 4));
    }

    // DC2: name valid -> decizie false
    @Test
    public void dc2_nameValidDecisionFalse() {
        assertNotNull(RecipeFactory.createCustomRecipe("Pasta", "MainCourse", "Italian", 500, 2));
    }

    // DC3: name.length() > 100 -> decizie true
    @Test
    public void dc3_nameLengthDecisionTrue() {
        assertThrows(IllegalArgumentException.class, () ->
                RecipeFactory.createCustomRecipe("A".repeat(101), "Soup", "Italian", 300, 4));
    }

    // DC4: name.length() <= 100 -> decizie false
    @Test
    public void dc4_nameLengthDecisionFalse() {
        assertNotNull(RecipeFactory.createCustomRecipe("A".repeat(100), "Soup", "Italian", 300, 4));
    }

    // DC5: kilocalories <= 0 -> decizie true
    @Test
    public void dc5_kcalNegativeDecisionTrue() {
        assertThrows(IllegalArgumentException.class, () ->
                RecipeFactory.createCustomRecipe("Dish", "MainCourse", "Italian", -1, 4));
    }

    // DC6: kilocalories > 0 -> decizie false (prima condiție)
    @Test
    public void dc6_kcalPositiveDecisionFalse() {
        assertNotNull(RecipeFactory.createCustomRecipe("Dish", "MainCourse", "Italian", 500, 4));
    }

    // DC7: kilocalories > 5000 -> decizie true
    @Test
    public void dc7_kcalAbove5000DecisionTrue() {
        assertThrows(IllegalArgumentException.class, () ->
                RecipeFactory.createCustomRecipe("Dish", "MainCourse", "Italian", 5001, 4));
    }

    // DC8: kilocalories <= 5000 -> decizie false
    @Test
    public void dc8_kcalAtLimitDecisionFalse() {
        assertNotNull(RecipeFactory.createCustomRecipe("Dish", "MainCourse", "Italian", 5000, 4));
    }

    // DC9: servings < 1 -> decizie true
    @Test
    public void dc9_servingsZeroDecisionTrue() {
        assertThrows(IllegalArgumentException.class, () ->
                RecipeFactory.createCustomRecipe("Dish", "MainCourse", "Italian", 500, 0));
    }

    // DC10: servings >= 1 -> decizie false
    @Test
    public void dc10_servingsValidDecisionFalse() {
        assertNotNull(RecipeFactory.createCustomRecipe("Dish", "MainCourse", "Italian", 500, 1));
    }

    // DC11: servings > 50 -> decizie true
    @Test
    public void dc11_servingsAbove50DecisionTrue() {
        assertThrows(IllegalArgumentException.class, () ->
                RecipeFactory.createCustomRecipe("Dish", "MainCourse", "Italian", 500, 51));
    }

    // DC12: servings ≤ 50 → decizie false
    @Test
    public void dc12_servingsAtLimitDecisionFalse() {
        assertNotNull(RecipeFactory.createCustomRecipe("Dish", "MainCourse", "Italian", 500, 50));
    }

    // DC13: adjustedKcal > 5000 în Dessert -> decizie true -> cap aplicat
    @Test
    public void dc13_dessertCapDecisionTrue() {
        Recipe r = RecipeFactory.createCustomRecipe("Cake", "Dessert", "French", 4400, 4);
        assertEquals(5000.0, r.getKilocalories(), 0.001);
    }

    // DC14: adjustedKcal <= 5000 în Dessert -> decizie false -> fără cap
    @Test
    public void dc14_dessertCapDecisionFalse() {
        Recipe r = RecipeFactory.createCustomRecipe("Cake", "Dessert", "French", 400, 4);
        assertEquals(460.0, r.getKilocalories(), 0.001);
    }
}