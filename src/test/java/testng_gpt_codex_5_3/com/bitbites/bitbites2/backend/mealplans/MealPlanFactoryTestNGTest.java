package testng_gpt_codex_5_3.com.bitbites.bitbites2.backend.mealplans;

import com.bitbites.bitbites2.backend.mealplans.DailyMealPlan;
import com.bitbites.bitbites2.backend.mealplans.FamilyMealPlan;
import com.bitbites.bitbites2.backend.mealplans.MealPlan;
import com.bitbites.bitbites2.backend.mealplans.MealPlanFactory;
import com.bitbites.bitbites2.backend.mealplans.SelfMealPlan;
import com.bitbites.bitbites2.backend.mealplans.WeeklyMealPlan;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class MealPlanFactoryTestNGTest {

    @Test
    public void shouldCreateAllKnownPlans() {
        MealPlan daily = MealPlanFactory.createMealPlan("daily", 1);
        MealPlan family = MealPlanFactory.createMealPlan("family", 4);
        MealPlan self = MealPlanFactory.createMealPlan("self", 1);
        MealPlan weekly = MealPlanFactory.createMealPlan("weekly", 1);

        assertTrue(daily instanceof DailyMealPlan);
        assertTrue(family instanceof FamilyMealPlan);
        assertTrue(self instanceof SelfMealPlan);
        assertTrue(weekly instanceof WeeklyMealPlan);
        assertEquals(((FamilyMealPlan) family).getMembersNumber(), 4);
    }

    @Test
    public void shouldReturnNullForUnknownType() {
        assertNull(MealPlanFactory.createMealPlan("other", 1));
    }
}
