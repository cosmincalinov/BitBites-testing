package gpt_codex_5_3.com.bitbites.bitbites2.backend.mealplans;

import com.bitbites.bitbites2.backend.mealplans.DailyMealPlan;
import com.bitbites.bitbites2.backend.mealplans.FamilyMealPlan;
import com.bitbites.bitbites2.backend.mealplans.MealPlan;
import com.bitbites.bitbites2.backend.mealplans.MealPlanFactory;
import com.bitbites.bitbites2.backend.mealplans.SelfMealPlan;
import com.bitbites.bitbites2.backend.mealplans.WeeklyMealPlan;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MealPlanFactoryTest {

    @Test
    void shouldCreateDailyMealPlan() {
        MealPlan plan = MealPlanFactory.createMealPlan("daily", 1);

        assertInstanceOf(DailyMealPlan.class, plan);
        assertEquals("daily", plan.getType());
    }

    @Test
    void shouldCreateFamilyMealPlanWithMembers() {
        MealPlan plan = MealPlanFactory.createMealPlan("family", 4);

        assertInstanceOf(FamilyMealPlan.class, plan);
        FamilyMealPlan family = (FamilyMealPlan) plan;
        assertEquals(4, family.getMembersNumber());
    }

    @Test
    void shouldCreateSelfMealPlan() {
        MealPlan plan = MealPlanFactory.createMealPlan("self", 1);

        assertInstanceOf(SelfMealPlan.class, plan);
        assertEquals("self", plan.getType());
    }

    @Test
    void shouldCreateWeeklyMealPlan() {
        MealPlan plan = MealPlanFactory.createMealPlan("weekly", 1);

        assertInstanceOf(WeeklyMealPlan.class, plan);
        assertEquals("weekly", plan.getType());
    }

    @Test
    void shouldReturnNullForUnknownType() {
        MealPlan plan = MealPlanFactory.createMealPlan("unknown", 1);

        assertNull(plan);
    }
}
