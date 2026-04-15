package com.bitbites.bitbites2.backend.mealplans.testng;

import com.bitbites.bitbites2.backend.mealplans.*;
import org.testng.Assert;
import org.testng.annotations.*;

/**
 * TestNG tests for MealPlanFactory.
 * Uses DataProvider for parameterized tests and groups for test organization.
 */
public class MealPlanFactoryTestNG {

    // ==================== Valid types (DataProvider) ====================

    @DataProvider(name = "validMealPlanTypes")
    public Object[][] validMealPlanTypes() {
        return new Object[][]{
                {"daily", DailyMealPlan.class, "daily", 1},
                {"weekly", WeeklyMealPlan.class, "weekly", 7},
                {"self", SelfMealPlan.class, "self", -1}, // self doesn't have dailySchedules initialized to fixed length
                {"family", FamilyMealPlan.class, "family", 7},
        };
    }

    @Test(dataProvider = "validMealPlanTypes", groups = {"creation"})
    public void createMealPlan_validType_returnsCorrectInstance(
            String type, Class<?> expectedClass, String expectedType, int expectedScheduleLen) {
        int members = type.equals("family") ? 4 : 0;
        MealPlan plan = MealPlanFactory.createMealPlan(type, members);

        Assert.assertNotNull(plan, "Plan should not be null for type: " + type);
        Assert.assertTrue(expectedClass.isInstance(plan),
                "Expected " + expectedClass.getSimpleName() + " but got " + plan.getClass().getSimpleName());
        Assert.assertEquals(plan.getType(), expectedType);
    }

    // ==================== Daily ====================

    @Test(groups = {"daily"})
    public void daily_hasOneDailyScheduleSlot() {
        MealPlan plan = MealPlanFactory.createMealPlan("daily", 0);
        Assert.assertEquals(plan.getDailySchedules().length, 1);
    }

    @Test(groups = {"daily"})
    public void daily_ignoresMembersParameter() {
        MealPlan plan1 = MealPlanFactory.createMealPlan("daily", 0);
        MealPlan plan2 = MealPlanFactory.createMealPlan("daily", 5);
        Assert.assertTrue(plan1 instanceof DailyMealPlan);
        Assert.assertTrue(plan2 instanceof DailyMealPlan);
    }

    @Test(groups = {"daily"})
    public void daily_typeIsDailyString() {
        Assert.assertEquals(MealPlanFactory.createMealPlan("daily", 0).getType(), "daily");
    }

    // ==================== Family ====================

    @Test(groups = {"family"})
    public void family_hasSevenDailyScheduleSlots() {
        MealPlan plan = MealPlanFactory.createMealPlan("family", 4);
        Assert.assertEquals(plan.getDailySchedules().length, 7);
    }

    @Test(groups = {"family"})
    public void family_storesMembersNumber() {
        FamilyMealPlan plan = (FamilyMealPlan) MealPlanFactory.createMealPlan("family", 4);
        Assert.assertEquals(plan.getMembersNumber(), 4);
    }

    @DataProvider(name = "familyMemberCounts")
    public Object[][] familyMemberCounts() {
        return new Object[][]{{1}, {2}, {4}, {6}, {10}};
    }

    @Test(dataProvider = "familyMemberCounts", groups = {"family"})
    public void family_variousMemberCounts(int members) {
        FamilyMealPlan plan = (FamilyMealPlan) MealPlanFactory.createMealPlan("family", members);
        Assert.assertEquals(plan.getMembersNumber(), members);
    }

    @Test(groups = {"family"})
    public void family_typeIsFamilyString() {
        Assert.assertEquals(MealPlanFactory.createMealPlan("family", 3).getType(), "family");
    }

    // ==================== Self ====================

    @Test(groups = {"self"})
    public void self_typeIsSelfString() {
        Assert.assertEquals(MealPlanFactory.createMealPlan("self", 0).getType(), "self");
    }

    @Test(groups = {"self"})
    public void self_ignoresMembersParameter() {
        MealPlan plan = MealPlanFactory.createMealPlan("self", 99);
        Assert.assertTrue(plan instanceof SelfMealPlan);
    }

    // ==================== Weekly ====================

    @Test(groups = {"weekly"})
    public void weekly_hasSevenDailyScheduleSlots() {
        MealPlan plan = MealPlanFactory.createMealPlan("weekly", 0);
        Assert.assertEquals(plan.getDailySchedules().length, 7);
    }

    @Test(groups = {"weekly"})
    public void weekly_typeIsWeeklyString() {
        Assert.assertEquals(MealPlanFactory.createMealPlan("weekly", 0).getType(), "weekly");
    }

    // ==================== Invalid types (DataProvider) ====================

    @DataProvider(name = "invalidTypes")
    public Object[][] invalidTypes() {
        return new Object[][]{
                {"unknown"},
                {""},
                {"Daily"},    // case sensitive
                {"WEEKLY"},   // case sensitive
                {"breakfast"},
                {"lunch"},
                {"dinner"},
                {"monthly"},
                {"yearly"},
                {"  daily  "}, // whitespace
        };
    }

    @Test(dataProvider = "invalidTypes", groups = {"invalid"})
    public void createMealPlan_invalidType_returnsNull(String type) {
        Assert.assertNull(MealPlanFactory.createMealPlan(type, 0),
                "Invalid type '" + type + "' should return null");
    }

    // ==================== Common behavior ====================

    @Test(groups = {"common"})
    public void allPlans_haveEmptyRecipesInitially() {
        MealPlan daily = MealPlanFactory.createMealPlan("daily", 0);
        MealPlan self = MealPlanFactory.createMealPlan("self", 0);
        MealPlan weekly = MealPlanFactory.createMealPlan("weekly", 0);
        MealPlan family = MealPlanFactory.createMealPlan("family", 4);

        Assert.assertTrue(daily.getRecipes().isEmpty());
        Assert.assertTrue(self.getRecipes().isEmpty());
        Assert.assertTrue(weekly.getRecipes().isEmpty());
        Assert.assertTrue(family.getRecipes().isEmpty());
    }

    @Test(groups = {"common"})
    public void allPlans_mapRecipesNotNull() {
        MealPlan plan = MealPlanFactory.createMealPlan("daily", 0);
        Assert.assertNotNull(plan.getMapRecipes());
        Assert.assertTrue(plan.getMapRecipes().isEmpty());
    }

    @Test(groups = {"common"})
    public void factory_returnsNewInstancesEachTime() {
        MealPlan plan1 = MealPlanFactory.createMealPlan("daily", 0);
        MealPlan plan2 = MealPlanFactory.createMealPlan("daily", 0);
        Assert.assertNotSame(plan1, plan2);
    }
}
