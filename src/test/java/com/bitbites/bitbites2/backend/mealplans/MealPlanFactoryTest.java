package com.bitbites.bitbites2.backend.mealplans;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MealPlanFactoryTest {

    // a) Partitionare de echivalenta
    /*
     * Partitii:
     * P1: type = "daily"  -> DailyMealPlan
     * P2: type = "family" -> FamilyMealPlan cu members setat
     * P3: type = "self"   -> SelfMealPlan
     * P4: type = "weekly" -> WeeklyMealPlan
     * P5: type = altceva  -> null
     */

    @Test
    public void testCreateMealPlan_Daily() {
        // P1
        MealPlan plan = MealPlanFactory.createMealPlan("daily", 0);
        assertNotNull(plan);
        assertInstanceOf(DailyMealPlan.class, plan);
    }

    @Test
    public void testCreateMealPlan_Family() {
        // P2
        MealPlan plan = MealPlanFactory.createMealPlan("family", 4);
        assertNotNull(plan);
        assertInstanceOf(FamilyMealPlan.class, plan);
        assertEquals(4, ((FamilyMealPlan) plan).getMembersNumber());
    }

    @Test
    public void testCreateMealPlan_Self() {
        // P3
        MealPlan plan = MealPlanFactory.createMealPlan("self", 0);
        assertNotNull(plan);
        assertInstanceOf(SelfMealPlan.class, plan);
    }

    @Test
    public void testCreateMealPlan_Weekly() {
        // P4
        MealPlan plan = MealPlanFactory.createMealPlan("weekly", 0);
        assertNotNull(plan);
        assertInstanceOf(WeeklyMealPlan.class, plan);
    }

    @Test
    public void testCreateMealPlan_TipNecunoscut_ReturneazaNull() {
        // P5
        MealPlan plan = MealPlanFactory.createMealPlan("unknown", 0);
        assertNull(plan);
    }

    // b) Boundary Value Analysis

    @Test
    public void testCreateMealPlan_Family_MinimMembri() {
        // Limita inferioara pentru membri: 1
        MealPlan plan = MealPlanFactory.createMealPlan("family", 1);
        assertNotNull(plan);
        assertEquals(1, ((FamilyMealPlan) plan).getMembersNumber());
    }

    @Test
    public void testCreateMealPlan_StringVid_ReturneazaNull() {
        // Limita: string gol -> default -> null
        assertNull(MealPlanFactory.createMealPlan("", 0));
    }

    @Test
    public void testCreateMealPlan_GetType_CorespondentaTipuri() {
        // Fiecare plan returneaza tipul corect
        assertEquals("daily",  MealPlanFactory.createMealPlan("daily", 0).getType());
        assertEquals("family", MealPlanFactory.createMealPlan("family", 2).getType());
        assertEquals("self",   MealPlanFactory.createMealPlan("self", 0).getType());
        assertEquals("weekly", MealPlanFactory.createMealPlan("weekly", 0).getType());
    }

    // c) Category Partitioning
    /*
     * Parametri: type, members
     * - type: valid ("daily"|"family"|"self"|"weekly") / invalid (orice altceva)
     * - members: relevant doar pentru "family" (1, 2, n > 1)
     *   pentru celelalte tipuri parametrul este ignorat
     */

    @Test
    public void testCreateMealPlan_Category_TipuriValide_NonNull() {
        String[] validTypes = {"daily", "family", "self", "weekly"};
        for (String type : validTypes) {
            assertNotNull(
                    MealPlanFactory.createMealPlan(type, 2),
                    "Asteptat non-null pentru tipul: " + type
            );
        }
    }

    @Test
    public void testCreateMealPlan_Category_TipuriInvalide_Null() {
        String[] invalidTypes = {"", "DAILY", "Daily", " daily", "random", "week"};
        for (String type : invalidTypes) {
            assertNull(
                    MealPlanFactory.createMealPlan(type, 0),
                    "Asteptat null pentru tipul: " + type
            );
        }
    }

    @Test
    public void testCreateMealPlan_Category_Family_DiversiMembri() {
        // members se transmite corect indiferent de valoare
        int[] memberCounts = {1, 2, 5, 10};
        for (int n : memberCounts) {
            FamilyMealPlan plan = (FamilyMealPlan) MealPlanFactory.createMealPlan("family", n);
            assertEquals(n, plan.getMembersNumber(),
                    "Members number incorect pentru n=" + n);
        }
    }

    // Statement Coverage
    @Test
    public void TestStmt() {
        // Acopera toate ramurile din switch
        assertInstanceOf(DailyMealPlan.class,  MealPlanFactory.createMealPlan("daily", 0));
        assertInstanceOf(FamilyMealPlan.class, MealPlanFactory.createMealPlan("family", 3));
        assertInstanceOf(SelfMealPlan.class,   MealPlanFactory.createMealPlan("self", 0));
        assertInstanceOf(WeeklyMealPlan.class, MealPlanFactory.createMealPlan("weekly", 0));
        assertNull(MealPlanFactory.createMealPlan("invalid", 0));
    }

    // Decision Coverage
    @Test
    public void TestDec() {
        // Fiecare case din switch reprezinta un punct de decizie
        assertNotNull(MealPlanFactory.createMealPlan("daily", 0));    // case "daily"
        assertNotNull(MealPlanFactory.createMealPlan("family", 2));   // case "family"
        assertNotNull(MealPlanFactory.createMealPlan("self", 0));     // case "self"
        assertNotNull(MealPlanFactory.createMealPlan("weekly", 0));   // case "weekly"
        assertNull(MealPlanFactory.createMealPlan("none", 0));        // default
    }

    // Cyclomatic Complexity pentru createMealPlan()
    // n = 8 noduri, e = 11 arce => V(G) = e - n + 2 = 11 - 8 + 2 = 5
    // C1: type = "daily"  -> new DailyMealPlan()
    // C2: type = "family" -> new FamilyMealPlan(members)
    // C3: type = "self"   -> new SelfMealPlan()
    // C4: type = "weekly" -> new WeeklyMealPlan()
    // C5: type = altceva  -> null
    @Test
    public void TestCirc() {
        // C1
        MealPlan p1 = MealPlanFactory.createMealPlan("daily", 0);
        assertInstanceOf(DailyMealPlan.class, p1);

        // C2
        MealPlan p2 = MealPlanFactory.createMealPlan("family", 4);
        assertInstanceOf(FamilyMealPlan.class, p2);
        assertEquals(4, ((FamilyMealPlan) p2).getMembersNumber());

        // C3
        MealPlan p3 = MealPlanFactory.createMealPlan("self", 0);
        assertInstanceOf(SelfMealPlan.class, p3);

        // C4
        MealPlan p4 = MealPlanFactory.createMealPlan("weekly", 0);
        assertInstanceOf(WeeklyMealPlan.class, p4);

        // C5
        MealPlan p5 = MealPlanFactory.createMealPlan("xyz", 0);
        assertNull(p5);
    }
}
