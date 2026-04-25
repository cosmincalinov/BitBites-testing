package com.bitbites.bitbites2.backend.recipes;

import org.junit.jupiter.api.*;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link RecipeFactory#createCustomRecipe(String, String, String, double, int)}.
 *
 * Techniques applied:
 * 1. Equivalence Partitioning  – divide each parameter's domain into valid/invalid classes
 * 2. Boundary Value Analysis   – test exact boundary values for numeric parameters
 * 3. Category Partitioning     – partition the categoryFood parameter by alias groups,
 *                                 valid categories, and invalid categories
 *
 * See cause-effect graph in: cause_effect_graph.md
 */
class CreateCustomRecipeTest {

    // =========================================================================
    // 1. EQUIVALENCE PARTITIONING
    // =========================================================================

    @Nested
    @DisplayName("Equivalence Partitioning")
    class EquivalencePartitioning {

        // ----- name -----

        @Test
        @DisplayName("EP-name: valid name => recipe created")
        void validName() {
            Recipe r = RecipeFactory.createCustomRecipe("Pasta", "MainCourse", "Italian", 500, 4);
            assertEquals("Pasta", r.getName());
        }

        @Test
        @DisplayName("EP-name: null name => exception")
        void nullName() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe(null, "Soup", "Romanian", 200, 2));
        }

        @Test
        @DisplayName("EP-name: blank name => exception")
        void blankName() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe("   ", "Soup", "Romanian", 200, 2));
        }

        @Test
        @DisplayName("EP-name: name with leading/trailing spaces => trimmed")
        void nameWithSpaces() {
            Recipe r = RecipeFactory.createCustomRecipe("  Pasta  ", "MainCourse", "Italian", 500, 4);
            assertEquals("Pasta", r.getName());
        }

        @Test
        @DisplayName("EP-name: name exceeding 100 chars => exception")
        void nameTooLong() {
            String longName = "A".repeat(101);
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe(longName, "Soup", "Romanian", 200, 2));
        }

        // ----- categoryFood -----

        @Test
        @DisplayName("EP-category: valid category 'Soup' => Soup instance")
        void validCategory() {
            Recipe r = RecipeFactory.createCustomRecipe("Borsch", "Soup", "Romanian", 150, 4);
            assertInstanceOf(Soup.class, r);
        }

        @Test
        @DisplayName("EP-category: null category => exception")
        void nullCategory() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe("Borsch", null, "Romanian", 150, 4));
        }

        @Test
        @DisplayName("EP-category: invalid category => exception")
        void invalidCategory() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe("Pizza", "Pizza", "Italian", 800, 2));
        }

        // ----- kitchenType -----

        @Test
        @DisplayName("EP-kitchen: valid kitchen type => recipe created")
        void validKitchenType() {
            Recipe r = RecipeFactory.createCustomRecipe("Pasta", "MainCourse", "Italian", 500, 4);
            assertEquals("Italian", r.getKitchenType());
        }

        @Test
        @DisplayName("EP-kitchen: null kitchen type => exception")
        void nullKitchenType() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe("Pasta", "MainCourse", null, 500, 4));
        }

        @Test
        @DisplayName("EP-kitchen: blank kitchen type => exception")
        void blankKitchenType() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe("Pasta", "MainCourse", "  ", 500, 4));
        }

        // ----- kilocalories -----

        @Test
        @DisplayName("EP-kcal: valid kcal (mid-range) => recipe created")
        void validKcal() {
            Recipe r = RecipeFactory.createCustomRecipe("Pasta", "MainCourse", "Italian", 500, 4);
            assertEquals(500, r.getKilocalories(), 0.01);
        }

        @Test
        @DisplayName("EP-kcal: zero kcal => exception")
        void zeroKcal() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe("Pasta", "MainCourse", "Italian", 0, 4));
        }

        @Test
        @DisplayName("EP-kcal: negative kcal => exception")
        void negativeKcal() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe("Pasta", "MainCourse", "Italian", -100, 4));
        }

        @Test
        @DisplayName("EP-kcal: kcal above 5000 => exception")
        void kcalTooHigh() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe("Pasta", "MainCourse", "Italian", 5001, 4));
        }

        // ----- servings -----

        @Test
        @DisplayName("EP-servings: valid servings (mid-range) => recipe created")
        void validServings() {
            Recipe r = RecipeFactory.createCustomRecipe("Pasta", "MainCourse", "Italian", 500, 4);
            assertEquals(4, r.getServings());
        }

        @Test
        @DisplayName("EP-servings: zero servings => exception")
        void zeroServings() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe("Pasta", "MainCourse", "Italian", 500, 0));
        }

        @Test
        @DisplayName("EP-servings: negative servings => exception")
        void negativeServings() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe("Pasta", "MainCourse", "Italian", 500, -3));
        }

        @Test
        @DisplayName("EP-servings: servings above 50 => exception")
        void servingsTooHigh() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe("Pasta", "MainCourse", "Italian", 500, 51));
        }

        // ----- kcal adjustment by category (partition on category behaviour) -----

        @Test
        @DisplayName("EP-adjust: Drink kcal multiplied by 0.8")
        void drinkKcalAdjustment() {
            Recipe r = RecipeFactory.createCustomRecipe("Juice", "Drink", "Italian", 100, 1);
            assertEquals(80, r.getKilocalories(), 0.01);
        }

        @Test
        @DisplayName("EP-adjust: Salad kcal multiplied by 0.9")
        void saladKcalAdjustment() {
            Recipe r = RecipeFactory.createCustomRecipe("Caesar", "Salad", "Italian", 200, 2);
            assertEquals(180, r.getKilocalories(), 0.01);
        }

        @Test
        @DisplayName("EP-adjust: Dessert kcal multiplied by 1.15")
        void dessertKcalAdjustment() {
            Recipe r = RecipeFactory.createCustomRecipe("Cake", "Dessert", "French", 400, 4);
            assertEquals(460, r.getKilocalories(), 0.01);
        }

        @Test
        @DisplayName("EP-adjust: MainCourse kcal unchanged")
        void mainCourseKcalUnchanged() {
            Recipe r = RecipeFactory.createCustomRecipe("Steak", "MainCourse", "American", 700, 2);
            assertEquals(700, r.getKilocalories(), 0.01);
        }
    }

    // =========================================================================
    // 2. BOUNDARY VALUE ANALYSIS
    // =========================================================================

    @Nested
    @DisplayName("Boundary Value Analysis")
    class BoundaryValueAnalysis {

        // ----- name length boundaries: max 100 -----

        @Test
        @DisplayName("BVA-name: exactly 100 chars => valid")
        void nameExactly100() {
            String name100 = "A".repeat(100);
            Recipe r = RecipeFactory.createCustomRecipe(name100, "Soup", "Romanian", 200, 2);
            assertEquals(name100, r.getName());
        }

        @Test
        @DisplayName("BVA-name: 101 chars => exception")
        void name101() {
            String name101 = "A".repeat(101);
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe(name101, "Soup", "Romanian", 200, 2));
        }

        @Test
        @DisplayName("BVA-name: 1 char => valid")
        void nameSingleChar() {
            Recipe r = RecipeFactory.createCustomRecipe("X", "Soup", "Romanian", 200, 2);
            assertEquals("X", r.getName());
        }

        // ----- kilocalories boundaries: (0, 5000] -----

        @Test
        @DisplayName("BVA-kcal: 0.01 (just above zero) => valid")
        void kcalJustAboveZero() {
            Recipe r = RecipeFactory.createCustomRecipe("Light", "Salad", "Italian", 0.01, 1);
            assertNotNull(r);
        }

        @Test
        @DisplayName("BVA-kcal: exactly 0 => exception")
        void kcalExactlyZero() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe("Light", "Salad", "Italian", 0, 1));
        }

        @Test
        @DisplayName("BVA-kcal: exactly 5000 => valid")
        void kcalExactly5000() {
            Recipe r = RecipeFactory.createCustomRecipe("Heavy", "MainCourse", "American", 5000, 1);
            assertEquals(5000, r.getKilocalories(), 0.01);
        }

        @Test
        @DisplayName("BVA-kcal: 5000.01 => exception")
        void kcalJustAbove5000() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe("Heavy", "MainCourse", "American", 5000.01, 1));
        }

        @Test
        @DisplayName("BVA-kcal: Dessert at 4348 => adjusted 4348*1.15=5000.2 capped to 5000")
        void dessertKcalCappedAt5000() {
            // 4348 * 1.15 = 5000.2 which should be capped at 5000
            Recipe r = RecipeFactory.createCustomRecipe("RichCake", "Dessert", "French", 4348, 2);
            assertEquals(5000, r.getKilocalories(), 0.01);
        }

        @Test
        @DisplayName("BVA-kcal: Dessert at 4347 => adjusted 4347*1.15=4999.05 not capped")
        void dessertKcalNotCapped() {
            Recipe r = RecipeFactory.createCustomRecipe("Cake", "Dessert", "French", 4347, 2);
            assertEquals(4347 * 1.15, r.getKilocalories(), 0.01);
        }

        // ----- servings boundaries: [1, 50] -----

        @Test
        @DisplayName("BVA-servings: exactly 1 => valid")
        void servingsExactly1() {
            Recipe r = RecipeFactory.createCustomRecipe("Solo", "Soup", "Romanian", 200, 1);
            assertEquals(1, r.getServings());
        }

        @Test
        @DisplayName("BVA-servings: exactly 50 => valid")
        void servingsExactly50() {
            Recipe r = RecipeFactory.createCustomRecipe("Banquet", "MainCourse", "Italian", 500, 50);
            assertEquals(50, r.getServings());
        }

        @Test
        @DisplayName("BVA-servings: 0 => exception")
        void servingsZero() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe("Solo", "Soup", "Romanian", 200, 0));
        }

        @Test
        @DisplayName("BVA-servings: 51 => exception")
        void servings51() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe("Banquet", "MainCourse", "Italian", 500, 51));
        }
    }

    // =========================================================================
    // 3. CATEGORY PARTITIONING
    // =========================================================================

    @Nested
    @DisplayName("Category Partitioning")
    class CategoryPartitioning {

        // ----- Partition 1: All 7 valid categories => correct subclass + duration -----

        @Test
        @DisplayName("CP: Appetizer => Appetizer instance, duration 20min")
        void appetizer() {
            Recipe r = RecipeFactory.createCustomRecipe("Bruschetta", "Appetizer", "Italian", 150, 2);
            assertAll(
                    () -> assertInstanceOf(Appetizer.class, r),
                    () -> assertEquals("Appetizer", r.getCategoryFood()),
                    () -> assertEquals(Duration.ofMinutes(20), r.getDuration())
            );
        }

        @Test
        @DisplayName("CP: Bread => Bread instance, duration 90min")
        void bread() {
            Recipe r = RecipeFactory.createCustomRecipe("Focaccia", "Bread", "Italian", 300, 6);
            assertAll(
                    () -> assertInstanceOf(Bread.class, r),
                    () -> assertEquals(Duration.ofMinutes(90), r.getDuration())
            );
        }

        @Test
        @DisplayName("CP: Dessert => Dessert instance, duration 60min")
        void dessert() {
            Recipe r = RecipeFactory.createCustomRecipe("Tiramisu", "Dessert", "Italian", 400, 4);
            assertAll(
                    () -> assertInstanceOf(Dessert.class, r),
                    () -> assertEquals(Duration.ofMinutes(60), r.getDuration())
            );
        }

        @Test
        @DisplayName("CP: Drink => Drink instance, duration 10min")
        void drink() {
            Recipe r = RecipeFactory.createCustomRecipe("Lemonade", "Drink", "American", 80, 2);
            assertAll(
                    () -> assertInstanceOf(Drink.class, r),
                    () -> assertEquals(Duration.ofMinutes(10), r.getDuration())
            );
        }

        @Test
        @DisplayName("CP: MainCourse => MainCourse instance, duration 40min")
        void mainCourse() {
            Recipe r = RecipeFactory.createCustomRecipe("Steak", "MainCourse", "American", 700, 2);
            assertAll(
                    () -> assertInstanceOf(MainCourse.class, r),
                    () -> assertEquals(Duration.ofMinutes(40), r.getDuration())
            );
        }

        @Test
        @DisplayName("CP: Salad => Salad instance, duration 15min")
        void salad() {
            Recipe r = RecipeFactory.createCustomRecipe("Caesar", "Salad", "Italian", 200, 2);
            assertAll(
                    () -> assertInstanceOf(Salad.class, r),
                    () -> assertEquals(Duration.ofMinutes(15), r.getDuration())
            );
        }

        @Test
        @DisplayName("CP: Soup => Soup instance, duration 45min")
        void soup() {
            Recipe r = RecipeFactory.createCustomRecipe("Borsch", "Soup", "Romanian", 150, 4);
            assertAll(
                    () -> assertInstanceOf(Soup.class, r),
                    () -> assertEquals(Duration.ofMinutes(45), r.getDuration())
            );
        }

        // ----- Partition 2: Alias categories => mapped to correct canonical category -----

        @Test
        @DisplayName("CP-alias: 'Starter' => Appetizer")
        void aliasStarter() {
            Recipe r = RecipeFactory.createCustomRecipe("Dip", "Starter", "Mexican", 120, 4);
            assertInstanceOf(Appetizer.class, r);
        }

        @Test
        @DisplayName("CP-alias: 'Appetiser' => Appetizer")
        void aliasAppetiser() {
            Recipe r = RecipeFactory.createCustomRecipe("Dip", "Appetiser", "Mexican", 120, 4);
            assertInstanceOf(Appetizer.class, r);
        }

        @Test
        @DisplayName("CP-alias: 'Main' => MainCourse")
        void aliasMain() {
            Recipe r = RecipeFactory.createCustomRecipe("Roast", "Main", "British", 600, 4);
            assertInstanceOf(MainCourse.class, r);
        }

        @Test
        @DisplayName("CP-alias: 'Entree' => MainCourse")
        void aliasEntree() {
            Recipe r = RecipeFactory.createCustomRecipe("Roast", "Entree", "French", 600, 4);
            assertInstanceOf(MainCourse.class, r);
        }

        @Test
        @DisplayName("CP-alias: 'Beverage' => Drink")
        void aliasBeverage() {
            Recipe r = RecipeFactory.createCustomRecipe("Tea", "Beverage", "British", 5, 1);
            assertInstanceOf(Drink.class, r);
        }

        // ----- Partition 3: Case-insensitive input => normalised -----

        @Test
        @DisplayName("CP-case: 'soup' (lowercase) => Soup")
        void lowercaseCategory() {
            Recipe r = RecipeFactory.createCustomRecipe("Borsch", "soup", "Romanian", 150, 4);
            assertInstanceOf(Soup.class, r);
        }

        @Test
        @DisplayName("CP-case: 'BREAD' (uppercase) => Bread")
        void uppercaseCategory() {
            Recipe r = RecipeFactory.createCustomRecipe("Baguette", "BREAD", "French", 250, 6);
            assertInstanceOf(Bread.class, r);
        }

        @Test
        @DisplayName("CP-case: 'dEsSeRt' (mixed) => Dessert")
        void mixedCaseCategory() {
            Recipe r = RecipeFactory.createCustomRecipe("Eclair", "dEsSeRt", "French", 350, 2);
            assertInstanceOf(Dessert.class, r);
        }

        // ----- Partition 4: Invalid categories => exception -----

        @Test
        @DisplayName("CP-invalid: completely unknown category => exception")
        void unknownCategory() {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe("Sushi", "Sushi", "Japanese", 300, 2));
            assertTrue(ex.getMessage().contains("Invalid category"));
        }

        @Test
        @DisplayName("CP-invalid: empty string category => exception")
        void emptyCategory() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe("Pasta", "", "Italian", 500, 4));
        }

        @Test
        @DisplayName("CP-invalid: null category => exception")
        void nullCategory() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe("Pasta", null, "Italian", 500, 4));
        }
    }

    // =========================================================================
    // 4. STATEMENT COVERAGE
    // =========================================================================
    //
    // Goal: execute every statement at least once.
    // Statements to hit:
    //   S1  – name==null branch                     (throw)
    //   S2  – name.trim().isEmpty() branch          (throw)
    //   S3  – name.length()>100 branch              (throw)
    //   S4  – categoryFood==null branch             (throw)
    //   S5  – categoryFood.trim().isEmpty() branch  (throw)
    //   S6  – alias "Starter"/"Appetiser" branch
    //   S7  – alias "Main"/"Maincourse"/"Entree"
    //   S8  – alias "Beverage"
    //   S9  – alias default (no mapping)
    //   S10 – invalid category after alias           (throw)
    //   S11 – kitchenType==null branch               (throw)
    //   S12 – kitchenType blank branch               (throw)
    //   S13 – kcal<=0                                (throw)
    //   S14 – kcal>5000                              (throw)
    //   S15 – servings<1                             (throw)
    //   S16 – servings>50                            (throw)
    //   S17 – adjust Drink branch (kcal*0.8)
    //   S18 – adjust Salad branch (kcal*0.9)
    //   S19 – adjust Dessert branch (kcal*1.15)
    //   S20 – adjust Dessert cap (adjustedKcal>5000 → 5000)
    //   S21 – adjust else (no change)
    //   S22–S28 – switch create: each of 7 categories
    //   S29 – switch create: default
    //   S30–S36 – switch duration: each of 7 categories
    //   S37 – switch duration: default
    //   S38 – return recipe

    @Nested
    @DisplayName("Statement Coverage")
    class StatementCoverage {

        @Test @DisplayName("SC-S1: name==null hits throw")
        void s1() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe(null, "Soup", "Ro", 100, 1));
        }

        @Test @DisplayName("SC-S2: blank name hits throw")
        void s2() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe("", "Soup", "Ro", 100, 1));
        }

        @Test @DisplayName("SC-S3: name>100 hits throw")
        void s3() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe("A".repeat(101), "Soup", "Ro", 100, 1));
        }

        @Test @DisplayName("SC-S4: category null hits throw")
        void s4() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe("X", null, "Ro", 100, 1));
        }

        @Test @DisplayName("SC-S5: category blank hits throw")
        void s5() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe("X", " ", "Ro", 100, 1));
        }

        @Test @DisplayName("SC-S6: alias Starter → Appetizer")
        void s6() {
            assertInstanceOf(Appetizer.class,
                    RecipeFactory.createCustomRecipe("X", "Starter", "Ro", 100, 1));
        }

        @Test @DisplayName("SC-S7: alias Entree → MainCourse")
        void s7() {
            assertInstanceOf(MainCourse.class,
                    RecipeFactory.createCustomRecipe("X", "Entree", "Fr", 100, 1));
        }

        @Test @DisplayName("SC-S8: alias Beverage → Drink")
        void s8() {
            assertInstanceOf(Drink.class,
                    RecipeFactory.createCustomRecipe("X", "Beverage", "Ro", 100, 1));
        }

        @Test @DisplayName("SC-S9: alias default (Soup stays Soup)")
        void s9() {
            assertInstanceOf(Soup.class,
                    RecipeFactory.createCustomRecipe("X", "Soup", "Ro", 100, 1));
        }

        @Test @DisplayName("SC-S10: invalid category after alias")
        void s10() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe("X", "Sushi", "Jp", 100, 1));
        }

        @Test @DisplayName("SC-S11: kitchenType null")
        void s11() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe("X", "Soup", null, 100, 1));
        }

        @Test @DisplayName("SC-S12: kitchenType blank")
        void s12() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe("X", "Soup", "  ", 100, 1));
        }

        @Test @DisplayName("SC-S13: kcal<=0")
        void s13() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe("X", "Soup", "Ro", 0, 1));
        }

        @Test @DisplayName("SC-S14: kcal>5000")
        void s14() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe("X", "Soup", "Ro", 5001, 1));
        }

        @Test @DisplayName("SC-S15: servings<1")
        void s15() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe("X", "Soup", "Ro", 100, 0));
        }

        @Test @DisplayName("SC-S16: servings>50")
        void s16() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe("X", "Soup", "Ro", 100, 51));
        }

        @Test @DisplayName("SC-S17: Drink adjusts kcal*0.8")
        void s17() {
            assertEquals(80, RecipeFactory.createCustomRecipe("X", "Drink", "Ro", 100, 1).getKilocalories(), 0.01);
        }

        @Test @DisplayName("SC-S18: Salad adjusts kcal*0.9")
        void s18() {
            assertEquals(90, RecipeFactory.createCustomRecipe("X", "Salad", "Ro", 100, 1).getKilocalories(), 0.01);
        }

        @Test @DisplayName("SC-S19: Dessert adjusts kcal*1.15")
        void s19() {
            assertEquals(115, RecipeFactory.createCustomRecipe("X", "Dessert", "Ro", 100, 1).getKilocalories(), 0.01);
        }

        @Test @DisplayName("SC-S20: Dessert kcal cap at 5000")
        void s20() {
            assertEquals(5000, RecipeFactory.createCustomRecipe("X", "Dessert", "Ro", 4500, 1).getKilocalories(), 0.01);
        }

        @Test @DisplayName("SC-S21: MainCourse kcal unchanged (else branch)")
        void s21() {
            assertEquals(300, RecipeFactory.createCustomRecipe("X", "MainCourse", "Ro", 300, 1).getKilocalories(), 0.01);
        }

        @Test @DisplayName("SC-S22–S28: all 7 categories create correct subclass")
        void s22to28() {
            assertAll(
                    () -> assertInstanceOf(Appetizer.class, RecipeFactory.createCustomRecipe("X", "Appetizer", "Ro", 100, 1)),
                    () -> assertInstanceOf(Bread.class, RecipeFactory.createCustomRecipe("X", "Bread", "Ro", 100, 1)),
                    () -> assertInstanceOf(Dessert.class, RecipeFactory.createCustomRecipe("X", "Dessert", "Ro", 100, 1)),
                    () -> assertInstanceOf(Drink.class, RecipeFactory.createCustomRecipe("X", "Drink", "Ro", 100, 1)),
                    () -> assertInstanceOf(MainCourse.class, RecipeFactory.createCustomRecipe("X", "MainCourse", "Ro", 100, 1)),
                    () -> assertInstanceOf(Salad.class, RecipeFactory.createCustomRecipe("X", "Salad", "Ro", 100, 1)),
                    () -> assertInstanceOf(Soup.class, RecipeFactory.createCustomRecipe("X", "Soup", "Ro", 100, 1))
            );
        }

        @Test @DisplayName("SC-S30–S36: all 7 durations set correctly")
        void s30to36() {
            assertAll(
                    () -> assertEquals(Duration.ofMinutes(20), RecipeFactory.createCustomRecipe("X", "Appetizer", "Ro", 100, 1).getDuration()),
                    () -> assertEquals(Duration.ofMinutes(90), RecipeFactory.createCustomRecipe("X", "Bread", "Ro", 100, 1).getDuration()),
                    () -> assertEquals(Duration.ofMinutes(60), RecipeFactory.createCustomRecipe("X", "Dessert", "Ro", 100, 1).getDuration()),
                    () -> assertEquals(Duration.ofMinutes(10), RecipeFactory.createCustomRecipe("X", "Drink", "Ro", 100, 1).getDuration()),
                    () -> assertEquals(Duration.ofMinutes(40), RecipeFactory.createCustomRecipe("X", "MainCourse", "Ro", 100, 1).getDuration()),
                    () -> assertEquals(Duration.ofMinutes(15), RecipeFactory.createCustomRecipe("X", "Salad", "Ro", 100, 1).getDuration()),
                    () -> assertEquals(Duration.ofMinutes(45), RecipeFactory.createCustomRecipe("X", "Soup", "Ro", 100, 1).getDuration())
            );
        }

        @Test @DisplayName("SC-S38: return recipe is not null")
        void s38() {
            assertNotNull(RecipeFactory.createCustomRecipe("X", "Soup", "Ro", 100, 1));
        }
    }

    // =========================================================================
    // 5. BRANCH COVERAGE
    // =========================================================================
    //
    // Every if/else and switch arm must be taken at least once (T and F).
    // Branches:
    //   B1  if(name==null || blank)            → T / F
    //   B2  if(name.length()>100)              → T / F
    //   B3  if(category==null || blank)        → T / F
    //   B4  alias switch: Starter|Appetiser, Main|Maincourse|Entree, Beverage, default
    //   B5  if(!VALID_CATEGORIES.contains)     → T / F
    //   B6  if(kitchenType==null || blank)     → T / F
    //   B7  if(kcal<=0)                        → T / F
    //   B8  if(kcal>5000)                      → T / F
    //   B9  if(servings<1)                     → T / F
    //   B10 if(servings>50)                    → T / F
    //   B11 if(Drink) / else if(Salad) / else if(Dessert) / else → 4 arms
    //   B12 if(adjustedKcal>5000) inside Dessert → T / F
    //   B13 create switch: 7 cases + default
    //   B14 duration switch: 7 cases + default

    @Nested
    @DisplayName("Branch Coverage")
    class BranchCoverage {

        // B1-T: name null
        @Test @DisplayName("BC-B1T: name==null → true branch")
        void b1True() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe(null, "Soup", "Ro", 100, 1));
        }

        // B1-F: name valid
        @Test @DisplayName("BC-B1F: name valid → false branch")
        void b1False() {
            assertNotNull(RecipeFactory.createCustomRecipe("Pasta", "Soup", "Ro", 100, 1));
        }

        // B2-T: name>100
        @Test @DisplayName("BC-B2T: name>100 → true branch")
        void b2True() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe("A".repeat(101), "Soup", "Ro", 100, 1));
        }

        // B2-F: name<=100
        @Test @DisplayName("BC-B2F: name<=100 → false branch")
        void b2False() {
            assertNotNull(RecipeFactory.createCustomRecipe("A".repeat(100), "Soup", "Ro", 100, 1));
        }

        // B3-T: category null
        @Test @DisplayName("BC-B3T: category null → true branch")
        void b3True() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe("X", null, "Ro", 100, 1));
        }

        // B3-F: category valid
        @Test @DisplayName("BC-B3F: category valid → false branch")
        void b3False() {
            assertNotNull(RecipeFactory.createCustomRecipe("X", "Soup", "Ro", 100, 1));
        }

        // B4: alias switch – all 4 arms
        @Test @DisplayName("BC-B4a: alias Starter")
        void b4a() { assertInstanceOf(Appetizer.class, RecipeFactory.createCustomRecipe("X", "Starter", "Ro", 100, 1)); }

        @Test @DisplayName("BC-B4b: alias Appetiser")
        void b4b() { assertInstanceOf(Appetizer.class, RecipeFactory.createCustomRecipe("X", "Appetiser", "Ro", 100, 1)); }

        @Test @DisplayName("BC-B4c: alias Main")
        void b4c() { assertInstanceOf(MainCourse.class, RecipeFactory.createCustomRecipe("X", "Main", "Ro", 100, 1)); }

        @Test @DisplayName("BC-B4d: alias Maincourse")
        void b4d() { assertInstanceOf(MainCourse.class, RecipeFactory.createCustomRecipe("X", "Maincourse", "Ro", 100, 1)); }

        @Test @DisplayName("BC-B4e: alias Entree")
        void b4e() { assertInstanceOf(MainCourse.class, RecipeFactory.createCustomRecipe("X", "Entree", "Ro", 100, 1)); }

        @Test @DisplayName("BC-B4f: alias Beverage")
        void b4f() { assertInstanceOf(Drink.class, RecipeFactory.createCustomRecipe("X", "Beverage", "Ro", 100, 1)); }

        @Test @DisplayName("BC-B4g: alias default (Soup stays)")
        void b4g() { assertInstanceOf(Soup.class, RecipeFactory.createCustomRecipe("X", "Soup", "Ro", 100, 1)); }

        // B5-T: invalid category
        @Test @DisplayName("BC-B5T: invalid category → true branch")
        void b5True() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe("X", "Sushi", "Jp", 100, 1));
        }

        // B5-F: valid category
        @Test @DisplayName("BC-B5F: valid category → false branch")
        void b5False() { assertNotNull(RecipeFactory.createCustomRecipe("X", "Soup", "Ro", 100, 1)); }

        // B6-T: kitchenType null
        @Test @DisplayName("BC-B6T: kitchenType null → true")
        void b6True() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe("X", "Soup", null, 100, 1));
        }

        // B6-F: valid kitchen
        @Test @DisplayName("BC-B6F: kitchenType valid → false")
        void b6False() { assertNotNull(RecipeFactory.createCustomRecipe("X", "Soup", "Ro", 100, 1)); }

        // B7-T: kcal<=0
        @Test @DisplayName("BC-B7T: kcal=0 → true")
        void b7True() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe("X", "Soup", "Ro", 0, 1));
        }

        // B7-F: kcal>0
        @Test @DisplayName("BC-B7F: kcal=1 → false")
        void b7False() { assertNotNull(RecipeFactory.createCustomRecipe("X", "Soup", "Ro", 1, 1)); }

        // B8-T: kcal>5000
        @Test @DisplayName("BC-B8T: kcal=5001 → true")
        void b8True() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe("X", "Soup", "Ro", 5001, 1));
        }

        // B8-F: kcal<=5000
        @Test @DisplayName("BC-B8F: kcal=5000 → false")
        void b8False() { assertNotNull(RecipeFactory.createCustomRecipe("X", "Soup", "Ro", 5000, 1)); }

        // B9-T: servings<1
        @Test @DisplayName("BC-B9T: servings=0 → true")
        void b9True() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe("X", "Soup", "Ro", 100, 0));
        }

        // B9-F: servings>=1
        @Test @DisplayName("BC-B9F: servings=1 → false")
        void b9False() { assertNotNull(RecipeFactory.createCustomRecipe("X", "Soup", "Ro", 100, 1)); }

        // B10-T: servings>50
        @Test @DisplayName("BC-B10T: servings=51 → true")
        void b10True() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe("X", "Soup", "Ro", 100, 51));
        }

        // B10-F: servings<=50
        @Test @DisplayName("BC-B10F: servings=50 → false")
        void b10False() { assertNotNull(RecipeFactory.createCustomRecipe("X", "Soup", "Ro", 100, 50)); }

        // B11: kcal adjustment – 4 arms
        @Test @DisplayName("BC-B11a: Drink arm (kcal*0.8)")
        void b11Drink() { assertEquals(80, RecipeFactory.createCustomRecipe("X", "Drink", "Ro", 100, 1).getKilocalories(), 0.01); }

        @Test @DisplayName("BC-B11b: Salad arm (kcal*0.9)")
        void b11Salad() { assertEquals(90, RecipeFactory.createCustomRecipe("X", "Salad", "Ro", 100, 1).getKilocalories(), 0.01); }

        @Test @DisplayName("BC-B11c: Dessert arm (kcal*1.15)")
        void b11Dessert() { assertEquals(115, RecipeFactory.createCustomRecipe("X", "Dessert", "Ro", 100, 1).getKilocalories(), 0.01); }

        @Test @DisplayName("BC-B11d: else arm (unchanged)")
        void b11Else() { assertEquals(100, RecipeFactory.createCustomRecipe("X", "Soup", "Ro", 100, 1).getKilocalories(), 0.01); }

        // B12-T: Dessert adjustedKcal > 5000 → cap
        @Test @DisplayName("BC-B12T: Dessert kcal capped")
        void b12True() { assertEquals(5000, RecipeFactory.createCustomRecipe("X", "Dessert", "Ro", 4500, 1).getKilocalories(), 0.01); }

        // B12-F: Dessert adjustedKcal <= 5000
        @Test @DisplayName("BC-B12F: Dessert kcal not capped")
        void b12False() { assertEquals(115, RecipeFactory.createCustomRecipe("X", "Dessert", "Ro", 100, 1).getKilocalories(), 0.01); }
    }

    // =========================================================================
    // 6. CONDITION COVERAGE
    // =========================================================================
    //
    // Each atomic condition in compound predicates is tested T and F independently.
    // Compound conditions:
    //   CC1: (name == null) || (name.trim().isEmpty())
    //         → name==null T/F, name.trim().isEmpty() T/F
    //   CC2: (categoryFood == null) || (categoryFood.trim().isEmpty())
    //         → categoryFood==null T/F, categoryFood.trim().isEmpty() T/F
    //   CC3: (kitchenType == null) || (kitchenType.trim().isEmpty())
    //         → kitchenType==null T/F, kitchenType.trim().isEmpty() T/F

    @Nested
    @DisplayName("Condition Coverage")
    class ConditionCoverage {

        // CC1: name==null (T), short-circuit so isEmpty not evaluated
        @Test @DisplayName("CC1a: name==null → T (short-circuit)")
        void cc1a() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe(null, "Soup", "Ro", 100, 1));
        }

        // CC1: name!=null (F) but isEmpty (T)
        @Test @DisplayName("CC1b: name not null but empty → isEmpty T")
        void cc1b() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe("", "Soup", "Ro", 100, 1));
        }

        // CC1: name!=null (F) and not empty (F) → whole predicate F
        @Test @DisplayName("CC1c: name not null and not empty → both F")
        void cc1c() {
            assertNotNull(RecipeFactory.createCustomRecipe("Pasta", "Soup", "Ro", 100, 1));
        }

        // CC2: categoryFood==null (T)
        @Test @DisplayName("CC2a: category==null → T")
        void cc2a() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe("X", null, "Ro", 100, 1));
        }

        // CC2: categoryFood!=null (F) but isEmpty (T)
        @Test @DisplayName("CC2b: category not null but blank → isEmpty T")
        void cc2b() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe("X", "  ", "Ro", 100, 1));
        }

        // CC2: categoryFood!=null (F) and not empty (F)
        @Test @DisplayName("CC2c: category valid → both F")
        void cc2c() {
            assertNotNull(RecipeFactory.createCustomRecipe("X", "Soup", "Ro", 100, 1));
        }

        // CC3: kitchenType==null (T)
        @Test @DisplayName("CC3a: kitchen==null → T")
        void cc3a() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe("X", "Soup", null, 100, 1));
        }

        // CC3: kitchenType!=null (F) but isEmpty (T)
        @Test @DisplayName("CC3b: kitchen not null but blank → isEmpty T")
        void cc3b() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe("X", "Soup", "  ", 100, 1));
        }

        // CC3: kitchenType!=null (F) and not empty (F)
        @Test @DisplayName("CC3c: kitchen valid → both F")
        void cc3c() {
            assertNotNull(RecipeFactory.createCustomRecipe("X", "Soup", "Ro", 100, 1));
        }
    }

    // =========================================================================
    // 7. CIRCUIT COVERAGE  (Multiple Condition Coverage)
    // =========================================================================
    //
    // Every combination of truth values for all atomic conditions in each
    // compound predicate. For a||b with short-circuit:
    //   (T,-)  (F,T)  (F,F)  — 3 reachable combos per compound predicate.

    @Nested
    @DisplayName("Circuit Coverage (Multiple Condition)")
    class CircuitCoverage {

        // --- name == null || name.trim().isEmpty() ---
        @Test @DisplayName("CIRC-name: (T,-) name==null")
        void nameNull() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe(null, "Soup", "Ro", 100, 1));
        }

        @Test @DisplayName("CIRC-name: (F,T) name not null but blank")
        void nameBlank() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe("   ", "Soup", "Ro", 100, 1));
        }

        @Test @DisplayName("CIRC-name: (F,F) name valid")
        void nameValid() {
            assertNotNull(RecipeFactory.createCustomRecipe("Pasta", "Soup", "Ro", 100, 1));
        }

        // --- categoryFood == null || categoryFood.trim().isEmpty() ---
        @Test @DisplayName("CIRC-cat: (T,-) category==null")
        void catNull() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe("X", null, "Ro", 100, 1));
        }

        @Test @DisplayName("CIRC-cat: (F,T) category blank")
        void catBlank() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe("X", " ", "Ro", 100, 1));
        }

        @Test @DisplayName("CIRC-cat: (F,F) category valid")
        void catValid() {
            assertNotNull(RecipeFactory.createCustomRecipe("X", "Soup", "Ro", 100, 1));
        }

        // --- kitchenType == null || kitchenType.trim().isEmpty() ---
        @Test @DisplayName("CIRC-kit: (T,-) kitchen==null")
        void kitNull() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe("X", "Soup", null, 100, 1));
        }

        @Test @DisplayName("CIRC-kit: (F,T) kitchen blank")
        void kitBlank() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe("X", "Soup", "  ", 100, 1));
        }

        @Test @DisplayName("CIRC-kit: (F,F) kitchen valid")
        void kitValid() {
            assertNotNull(RecipeFactory.createCustomRecipe("X", "Soup", "Ro", 100, 1));
        }

        // --- kcal adjustment chain: if(Drink) / else if(Salad) / else if(Dessert) / else ---
        // This is a chain of conditions; circuit coverage = all 4 outcomes
        @Test @DisplayName("CIRC-adj: Drink=T (first if true)")
        void adjDrink() {
            assertEquals(80, RecipeFactory.createCustomRecipe("X", "Drink", "Ro", 100, 1).getKilocalories(), 0.01);
        }

        @Test @DisplayName("CIRC-adj: Drink=F, Salad=T")
        void adjSalad() {
            assertEquals(90, RecipeFactory.createCustomRecipe("X", "Salad", "Ro", 100, 1).getKilocalories(), 0.01);
        }

        @Test @DisplayName("CIRC-adj: Drink=F, Salad=F, Dessert=T, cap=F")
        void adjDessertNoCap() {
            assertEquals(115, RecipeFactory.createCustomRecipe("X", "Dessert", "Ro", 100, 1).getKilocalories(), 0.01);
        }

        @Test @DisplayName("CIRC-adj: Drink=F, Salad=F, Dessert=T, cap=T")
        void adjDessertCap() {
            assertEquals(5000, RecipeFactory.createCustomRecipe("X", "Dessert", "Ro", 4500, 1).getKilocalories(), 0.01);
        }

        @Test @DisplayName("CIRC-adj: Drink=F, Salad=F, Dessert=F (else)")
        void adjElse() {
            assertEquals(100, RecipeFactory.createCustomRecipe("X", "Soup", "Ro", 100, 1).getKilocalories(), 0.01);
        }
    }

    // =========================================================================
    // 8. LCSAJ COVERAGE
    // =========================================================================
    //
    // An LCSAJ (Linear Code Sequence And Jump) is a maximal sequence of
    // statements starting after a jump target, executing linearly, and ending
    // with a jump (branch/return/throw).
    //
    // LCSAJs identified in createCustomRecipe (line numbers approximate):
    //
    // LCSAJ-1: entry → name==null check → THROW            (name null)
    // LCSAJ-2: entry → name valid → name.length()>100 → THROW
    // LCSAJ-3: entry → name valid → name<=100 → cat==null → THROW
    // LCSAJ-4: entry → name OK → cat OK → alias switch → !VALID → THROW
    // LCSAJ-5: entry → name OK → cat OK → alias OK → kit==null → THROW
    // LCSAJ-6: entry → ... → kit OK → kcal<=0 → THROW
    // LCSAJ-7: entry → ... → kcal OK(>0) → kcal>5000 → THROW
    // LCSAJ-8: entry → ... → kcal OK → servings<1 → THROW
    // LCSAJ-9: entry → ... → servings OK(>=1) → servings>50 → THROW
    // LCSAJ-10: entry → all valid → Drink adjust → create Drink → duration → RETURN
    // LCSAJ-11: entry → all valid → Salad adjust → create Salad → duration → RETURN
    // LCSAJ-12: entry → all valid → Dessert adjust (no cap) → create → dur → RETURN
    // LCSAJ-13: entry → all valid → Dessert adjust (cap) → create → dur → RETURN
    // LCSAJ-14: entry → all valid → else adjust → create MainCourse → dur → RETURN
    // LCSAJ-15: entry → all valid → else adjust → create Soup → dur → RETURN
    // LCSAJ-16: entry → all valid → else adjust → create Appetizer → dur → RETURN
    // LCSAJ-17: entry → all valid → else adjust → create Bread → dur → RETURN
    // LCSAJ-18: entry → all valid → alias Starter → create Appetizer → dur → RETURN

    @Nested
    @DisplayName("LCSAJ Coverage")
    class LcsajCoverage {

        @Test @DisplayName("LCSAJ-1: entry → name null → throw")
        void lcsaj1() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe(null, "Soup", "Ro", 100, 1));
        }

        @Test @DisplayName("LCSAJ-2: entry → name>100 → throw")
        void lcsaj2() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe("A".repeat(101), "Soup", "Ro", 100, 1));
        }

        @Test @DisplayName("LCSAJ-3: entry → name OK → cat null → throw")
        void lcsaj3() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe("Pasta", null, "Ro", 100, 1));
        }

        @Test @DisplayName("LCSAJ-4: entry → name OK → cat OK → invalid cat → throw")
        void lcsaj4() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe("Pasta", "Sushi", "Jp", 100, 1));
        }

        @Test @DisplayName("LCSAJ-5: entry → cat OK → kit null → throw")
        void lcsaj5() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe("Pasta", "Soup", null, 100, 1));
        }

        @Test @DisplayName("LCSAJ-6: entry → kit OK → kcal<=0 → throw")
        void lcsaj6() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe("Pasta", "Soup", "Ro", -5, 1));
        }

        @Test @DisplayName("LCSAJ-7: entry → kcal>0 → kcal>5000 → throw")
        void lcsaj7() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe("Pasta", "Soup", "Ro", 9999, 1));
        }

        @Test @DisplayName("LCSAJ-8: entry → kcal OK → servings<1 → throw")
        void lcsaj8() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe("Pasta", "Soup", "Ro", 100, 0));
        }

        @Test @DisplayName("LCSAJ-9: entry → servings>=1 → servings>50 → throw")
        void lcsaj9() {
            assertThrows(IllegalArgumentException.class,
                    () -> RecipeFactory.createCustomRecipe("Pasta", "Soup", "Ro", 100, 51));
        }

        @Test @DisplayName("LCSAJ-10: happy path → Drink → adjust*0.8 → create → dur 10 → return")
        void lcsaj10() {
            Recipe r = RecipeFactory.createCustomRecipe("Juice", "Drink", "Italian", 200, 2);
            assertAll(
                    () -> assertInstanceOf(Drink.class, r),
                    () -> assertEquals(160, r.getKilocalories(), 0.01),
                    () -> assertEquals(Duration.ofMinutes(10), r.getDuration())
            );
        }

        @Test @DisplayName("LCSAJ-11: happy path → Salad → adjust*0.9 → create → dur 15 → return")
        void lcsaj11() {
            Recipe r = RecipeFactory.createCustomRecipe("Greek", "Salad", "Greek", 200, 2);
            assertAll(
                    () -> assertInstanceOf(Salad.class, r),
                    () -> assertEquals(180, r.getKilocalories(), 0.01),
                    () -> assertEquals(Duration.ofMinutes(15), r.getDuration())
            );
        }

        @Test @DisplayName("LCSAJ-12: happy path → Dessert no cap → create → dur 60 → return")
        void lcsaj12() {
            Recipe r = RecipeFactory.createCustomRecipe("Tart", "Dessert", "French", 200, 4);
            assertAll(
                    () -> assertInstanceOf(Dessert.class, r),
                    () -> assertEquals(230, r.getKilocalories(), 0.01),
                    () -> assertEquals(Duration.ofMinutes(60), r.getDuration())
            );
        }

        @Test @DisplayName("LCSAJ-13: happy path → Dessert with cap → create → dur 60 → return")
        void lcsaj13() {
            Recipe r = RecipeFactory.createCustomRecipe("Mega", "Dessert", "French", 4500, 1);
            assertAll(
                    () -> assertInstanceOf(Dessert.class, r),
                    () -> assertEquals(5000, r.getKilocalories(), 0.01),
                    () -> assertEquals(Duration.ofMinutes(60), r.getDuration())
            );
        }

        @Test @DisplayName("LCSAJ-14: happy path → MainCourse → no adjust → dur 40 → return")
        void lcsaj14() {
            Recipe r = RecipeFactory.createCustomRecipe("Steak", "MainCourse", "American", 700, 2);
            assertAll(
                    () -> assertInstanceOf(MainCourse.class, r),
                    () -> assertEquals(700, r.getKilocalories(), 0.01),
                    () -> assertEquals(Duration.ofMinutes(40), r.getDuration())
            );
        }

        @Test @DisplayName("LCSAJ-15: happy path → Soup → no adjust → dur 45 → return")
        void lcsaj15() {
            Recipe r = RecipeFactory.createCustomRecipe("Borsch", "Soup", "Romanian", 150, 4);
            assertAll(
                    () -> assertInstanceOf(Soup.class, r),
                    () -> assertEquals(150, r.getKilocalories(), 0.01),
                    () -> assertEquals(Duration.ofMinutes(45), r.getDuration())
            );
        }

        @Test @DisplayName("LCSAJ-16: happy path → Appetizer → no adjust → dur 20 → return")
        void lcsaj16() {
            Recipe r = RecipeFactory.createCustomRecipe("Dip", "Appetizer", "Mexican", 120, 4);
            assertAll(
                    () -> assertInstanceOf(Appetizer.class, r),
                    () -> assertEquals(120, r.getKilocalories(), 0.01),
                    () -> assertEquals(Duration.ofMinutes(20), r.getDuration())
            );
        }

        @Test @DisplayName("LCSAJ-17: happy path → Bread → no adjust → dur 90 → return")
        void lcsaj17() {
            Recipe r = RecipeFactory.createCustomRecipe("Baguette", "Bread", "French", 300, 6);
            assertAll(
                    () -> assertInstanceOf(Bread.class, r),
                    () -> assertEquals(300, r.getKilocalories(), 0.01),
                    () -> assertEquals(Duration.ofMinutes(90), r.getDuration())
            );
        }

        @Test @DisplayName("LCSAJ-18: happy path → alias Starter → Appetizer → dur 20 → return")
        void lcsaj18() {
            Recipe r = RecipeFactory.createCustomRecipe("Dip", "Starter", "Mexican", 120, 4);
            assertAll(
                    () -> assertInstanceOf(Appetizer.class, r),
                    () -> assertEquals(120, r.getKilocalories(), 0.01),
                    () -> assertEquals(Duration.ofMinutes(20), r.getDuration())
            );
        }
    }
}
