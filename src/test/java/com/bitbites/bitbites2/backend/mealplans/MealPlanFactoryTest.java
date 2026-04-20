package com.bitbites.bitbites2.backend.mealplans;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 tests for MealPlanFactory.
 *
 * Strategies:
 * - Equivalence partitioning (valid types: daily, weekly, family, self; invalid type)
 * - Boundary analysis (null/empty/unknown type strings)
 * - Decision coverage (all switch branches including default)
 */
class MealPlanFactoryTest {

    @Test
    @DisplayName("Create daily meal plan")
    void createDaily() {
        MealPlan plan = MealPlanFactory.createMealPlan("daily", 1);
        assertNotNull(plan);
        assertInstanceOf(DailyMealPlan.class, plan);
        assertEquals("daily", plan.getType());
    }

    @Test
    @DisplayName("Create weekly meal plan")
    void createWeekly() {
        MealPlan plan = MealPlanFactory.createMealPlan("weekly", 1);
        assertNotNull(plan);
        assertInstanceOf(WeeklyMealPlan.class, plan);
        assertEquals("weekly", plan.getType());
    }

    @Test
    @DisplayName("Create family meal plan")
    void createFamily() {
        MealPlan plan = MealPlanFactory.createMealPlan("family", 4);
        assertNotNull(plan);
        assertInstanceOf(FamilyMealPlan.class, plan);
        assertEquals("family", plan.getType());
        assertEquals(4, ((FamilyMealPlan) plan).getMembersNumber());
    }

    @Test
    @DisplayName("Create self meal plan")
    void createSelf() {
        MealPlan plan = MealPlanFactory.createMealPlan("self", 1);
        assertNotNull(plan);
        assertInstanceOf(SelfMealPlan.class, plan);
        assertEquals("self", plan.getType());
    }

    @Test
    @DisplayName("Unknown type returns null")
    void unknownTypeReturnsNull() {
        assertNull(MealPlanFactory.createMealPlan("unknown", 1));
    }

    @Test
    @DisplayName("Empty string returns null")
    void emptyStringReturnsNull() {
        assertNull(MealPlanFactory.createMealPlan("", 1));
    }

    @Test
    @DisplayName("Case-sensitive: 'Daily' returns null")
    void caseSensitive() {
        assertNull(MealPlanFactory.createMealPlan("Daily", 1));
    }

    @Test
    @DisplayName("Daily plan has 1 daily schedule slot")
    void dailyPlanHasOneSchedule() {
        MealPlan plan = MealPlanFactory.createMealPlan("daily", 1);
        assertNotNull(plan.getDailySchedules());
        assertEquals(1, plan.getDailySchedules().length);
    }

    @Test
    @DisplayName("Weekly plan has 7 daily schedule slots")
    void weeklyPlanHasSevenSchedules() {
        MealPlan plan = MealPlanFactory.createMealPlan("weekly", 1);
        assertNotNull(plan.getDailySchedules());
        assertEquals(7, plan.getDailySchedules().length);
    }

    @Test
    @DisplayName("Family plan has 7 daily schedule slots")
    void familyPlanHasSevenSchedules() {
        MealPlan plan = MealPlanFactory.createMealPlan("family", 3);
        assertNotNull(plan.getDailySchedules());
        assertEquals(7, plan.getDailySchedules().length);
    }

    @Test
    @DisplayName("Family plan members parameter is stored")
    void familyMembersStored() {
        MealPlan plan = MealPlanFactory.createMealPlan("family", 5);
        assertEquals(5, ((FamilyMealPlan) plan).getMembersNumber());
    }

    @Test
    @DisplayName("Self plan recipes map starts empty")
    void selfPlanStartsEmpty() {
        MealPlan plan = MealPlanFactory.createMealPlan("self", 1);
        assertTrue(plan.getRecipes().isEmpty());
    }

    @Test
    @DisplayName("Members param ignored for non-family types")
    void membersIgnoredForNonFamily() {
        MealPlan daily = MealPlanFactory.createMealPlan("daily", 99);
        assertInstanceOf(DailyMealPlan.class, daily);
    }
}
