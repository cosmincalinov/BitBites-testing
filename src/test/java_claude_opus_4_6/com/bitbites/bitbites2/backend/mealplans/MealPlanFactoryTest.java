package com.bitbites.bitbites2.backend.mealplans;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MealPlanFactory Tests")
class MealPlanFactoryTest {

    // ==================== Daily MealPlan ====================

    @Nested
    @DisplayName("Daily MealPlan creation")
    class DailyMealPlanTests {

        @Test
        @DisplayName("Create daily meal plan returns DailyMealPlan instance")
        void createDailyReturnsCorrectType() {
            MealPlan plan = MealPlanFactory.createMealPlan("daily", 0);
            assertInstanceOf(DailyMealPlan.class, plan);
        }

        @Test
        @DisplayName("Daily meal plan type is 'daily'")
        void dailyTypeString() {
            MealPlan plan = MealPlanFactory.createMealPlan("daily", 0);
            assertEquals("daily", plan.getType());
        }

        @Test
        @DisplayName("Daily meal plan has 1 daily schedule slot")
        void dailyHasOneDaySchedule() {
            MealPlan plan = MealPlanFactory.createMealPlan("daily", 0);
            assertEquals(1, plan.getDailySchedules().length);
        }

        @Test
        @DisplayName("Daily meal plan ignores members parameter")
        void dailyIgnoresMembers() {
            MealPlan plan1 = MealPlanFactory.createMealPlan("daily", 0);
            MealPlan plan2 = MealPlanFactory.createMealPlan("daily", 5);
            // both should be DailyMealPlan regardless of members
            assertInstanceOf(DailyMealPlan.class, plan1);
            assertInstanceOf(DailyMealPlan.class, plan2);
        }
    }

    // ==================== Family MealPlan ====================

    @Nested
    @DisplayName("Family MealPlan creation")
    class FamilyMealPlanTests {

        @Test
        @DisplayName("Create family meal plan returns FamilyMealPlan instance")
        void createFamilyReturnsCorrectType() {
            MealPlan plan = MealPlanFactory.createMealPlan("family", 4);
            assertInstanceOf(FamilyMealPlan.class, plan);
        }

        @Test
        @DisplayName("Family meal plan type is 'family'")
        void familyTypeString() {
            MealPlan plan = MealPlanFactory.createMealPlan("family", 4);
            assertEquals("family", plan.getType());
        }

        @Test
        @DisplayName("Family meal plan has 7 daily schedule slots")
        void familyHasSevenDaySchedules() {
            MealPlan plan = MealPlanFactory.createMealPlan("family", 4);
            assertEquals(7, plan.getDailySchedules().length);
        }

        @Test
        @DisplayName("Family meal plan stores members number")
        void familyStoresMembersNumber() {
            FamilyMealPlan plan = (FamilyMealPlan) MealPlanFactory.createMealPlan("family", 4);
            assertEquals(4, plan.getMembersNumber());
        }

        @Test
        @DisplayName("Family meal plan with 1 member")
        void familyWithOneMember() {
            FamilyMealPlan plan = (FamilyMealPlan) MealPlanFactory.createMealPlan("family", 1);
            assertEquals(1, plan.getMembersNumber());
        }

        @Test
        @DisplayName("Family meal plan with large family")
        void familyWithLargeFamily() {
            FamilyMealPlan plan = (FamilyMealPlan) MealPlanFactory.createMealPlan("family", 10);
            assertEquals(10, plan.getMembersNumber());
        }
    }

    // ==================== Self MealPlan ====================

    @Nested
    @DisplayName("Self MealPlan creation")
    class SelfMealPlanTests {

        @Test
        @DisplayName("Create self meal plan returns SelfMealPlan instance")
        void createSelfReturnsCorrectType() {
            MealPlan plan = MealPlanFactory.createMealPlan("self", 0);
            assertInstanceOf(SelfMealPlan.class, plan);
        }

        @Test
        @DisplayName("Self meal plan type is 'self'")
        void selfTypeString() {
            MealPlan plan = MealPlanFactory.createMealPlan("self", 0);
            assertEquals("self", plan.getType());
        }

        @Test
        @DisplayName("Self meal plan ignores members parameter")
        void selfIgnoresMembers() {
            MealPlan plan = MealPlanFactory.createMealPlan("self", 99);
            assertInstanceOf(SelfMealPlan.class, plan);
        }
    }

    // ==================== Weekly MealPlan ====================

    @Nested
    @DisplayName("Weekly MealPlan creation")
    class WeeklyMealPlanTests {

        @Test
        @DisplayName("Create weekly meal plan returns WeeklyMealPlan instance")
        void createWeeklyReturnsCorrectType() {
            MealPlan plan = MealPlanFactory.createMealPlan("weekly", 0);
            assertInstanceOf(WeeklyMealPlan.class, plan);
        }

        @Test
        @DisplayName("Weekly meal plan type is 'weekly'")
        void weeklyTypeString() {
            MealPlan plan = MealPlanFactory.createMealPlan("weekly", 0);
            assertEquals("weekly", plan.getType());
        }

        @Test
        @DisplayName("Weekly meal plan has 7 daily schedule slots")
        void weeklyHasSevenDaySchedules() {
            MealPlan plan = MealPlanFactory.createMealPlan("weekly", 0);
            assertEquals(7, plan.getDailySchedules().length);
        }
    }

    // ==================== Invalid type ====================

    @Nested
    @DisplayName("Invalid type handling")
    class InvalidTypeTests {

        @Test
        @DisplayName("Unknown type returns null")
        void unknownTypeReturnsNull() {
            MealPlan plan = MealPlanFactory.createMealPlan("unknown", 0);
            assertNull(plan);
        }

        @Test
        @DisplayName("Empty string type returns null")
        void emptyStringReturnsNull() {
            MealPlan plan = MealPlanFactory.createMealPlan("", 0);
            assertNull(plan);
        }

        @Test
        @DisplayName("Uppercase type returns null (case sensitive)")
        void uppercaseTypeReturnsNull() {
            MealPlan plan = MealPlanFactory.createMealPlan("Daily", 0);
            assertNull(plan);
        }

        @Test
        @DisplayName("Mixed case type returns null")
        void mixedCaseTypeReturnsNull() {
            MealPlan plan = MealPlanFactory.createMealPlan("WEEKLY", 0);
            assertNull(plan);
        }

        @ParameterizedTest(name = "Invalid type \"{0}\" returns null")
        @ValueSource(strings = {"breakfast", "lunch", "dinner", "monthly", "yearly", "  daily  "})
        @DisplayName("Various invalid types return null")
        void variousInvalidTypes(String type) {
            assertNull(MealPlanFactory.createMealPlan(type, 0));
        }
    }

    // ==================== Common MealPlan behavior ====================

    @Nested
    @DisplayName("Common MealPlan properties")
    class CommonMealPlanTests {

        @Test
        @DisplayName("All created plans have empty recipe map initially")
        void plansHaveEmptyRecipes() {
            MealPlan daily = MealPlanFactory.createMealPlan("daily", 0);
            MealPlan self = MealPlanFactory.createMealPlan("self", 0);
            MealPlan weekly = MealPlanFactory.createMealPlan("weekly", 0);
            MealPlan family = MealPlanFactory.createMealPlan("family", 4);

            assertTrue(daily.getRecipes().isEmpty());
            assertTrue(self.getRecipes().isEmpty());
            assertTrue(weekly.getRecipes().isEmpty());
            assertTrue(family.getRecipes().isEmpty());
        }

        @Test
        @DisplayName("All created plans have empty recipe map (getMapRecipes)")
        void plansHaveEmptyMapRecipes() {
            MealPlan daily = MealPlanFactory.createMealPlan("daily", 0);

            assertNotNull(daily.getMapRecipes());
            assertTrue(daily.getMapRecipes().isEmpty());
        }

        @Test
        @DisplayName("Factory returns new instances each time")
        void factoryReturnsNewInstances() {
            MealPlan plan1 = MealPlanFactory.createMealPlan("daily", 0);
            MealPlan plan2 = MealPlanFactory.createMealPlan("daily", 0);

            assertNotSame(plan1, plan2);
        }
    }
}
