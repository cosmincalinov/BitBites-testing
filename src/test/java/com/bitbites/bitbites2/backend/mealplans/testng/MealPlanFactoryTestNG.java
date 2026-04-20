package com.bitbites.bitbites2.backend.mealplans.testng;

import com.bitbites.bitbites2.backend.mealplans.*;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import static org.testng.Assert.*;

/**
 * TestNG tests for MealPlanFactory.
 *
 * Comparative note vs JUnit:
 * - Uses @DataProvider for testing all valid types in a single test method
 * - Uses expectedExceptions for error cases
 * - Uses SoftAssert for multi-property assertions
 */
public class MealPlanFactoryTestNG {

    @DataProvider(name = "validTypes")
    public Object[][] validTypes() {
        return new Object[][]{
                {"daily", DailyMealPlan.class, "daily"},
                {"weekly", WeeklyMealPlan.class, "weekly"},
                {"family", FamilyMealPlan.class, "family"},
                {"self", SelfMealPlan.class, "self"},
        };
    }

    @Test(dataProvider = "validTypes", groups = "valid")
    public void createValidType(String type, Class<? extends MealPlan> expectedClass, String expectedType) {
        MealPlan plan = MealPlanFactory.createMealPlan(type, 4);
        SoftAssert sa = new SoftAssert();
        sa.assertNotNull(plan);
        sa.assertTrue(expectedClass.isInstance(plan), "Expected " + expectedClass.getSimpleName());
        sa.assertEquals(plan.getType(), expectedType);
        sa.assertAll();
    }

    @DataProvider(name = "invalidTypes")
    public Object[][] invalidTypes() {
        return new Object[][]{
                {"unknown"},
                {""},
                {"Daily"},   // case-sensitive
                {"WEEKLY"},
                {"random"},
        };
    }

    @Test(dataProvider = "invalidTypes", groups = "invalid")
    public void invalidTypeReturnsNull(String type) {
        assertNull(MealPlanFactory.createMealPlan(type, 1));
    }

    @Test(groups = "structure")
    public void dailyHasOneScheduleSlot() {
        MealPlan plan = MealPlanFactory.createMealPlan("daily", 1);
        assertNotNull(plan.getDailySchedules());
        assertEquals(plan.getDailySchedules().length, 1);
    }

    @Test(groups = "structure")
    public void weeklyHasSevenScheduleSlots() {
        MealPlan plan = MealPlanFactory.createMealPlan("weekly", 1);
        assertNotNull(plan.getDailySchedules());
        assertEquals(plan.getDailySchedules().length, 7);
    }

    @Test(groups = "structure")
    public void familyHasSevenScheduleSlots() {
        MealPlan plan = MealPlanFactory.createMealPlan("family", 3);
        assertNotNull(plan.getDailySchedules());
        assertEquals(plan.getDailySchedules().length, 7);
    }

    @Test(groups = "family")
    public void familyMembersStored() {
        MealPlan plan = MealPlanFactory.createMealPlan("family", 5);
        assertEquals(((FamilyMealPlan) plan).getMembersNumber(), 5);
    }

    @Test(groups = "self")
    public void selfPlanStartsEmpty() {
        MealPlan plan = MealPlanFactory.createMealPlan("self", 1);
        assertTrue(plan.getRecipes().isEmpty());
    }

    @Test(groups = "misc")
    public void membersIgnoredForNonFamily() {
        MealPlan daily = MealPlanFactory.createMealPlan("daily", 99);
        assertTrue(daily instanceof DailyMealPlan);
    }
}
