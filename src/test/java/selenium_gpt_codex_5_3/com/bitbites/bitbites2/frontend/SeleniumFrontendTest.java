package selenium_gpt_codex_5_3.com.bitbites.bitbites2.frontend;

import com.bitbites.bitbites2.Application;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SeleniumFrontendTest {

    @LocalServerPort
    private int port;

    private WebDriver driver;

    @BeforeEach
    void setUp() {
        driver = new HtmlUnitDriver(true);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void loginPageShouldDisplayMainElements() {
        driver.get(baseUrl("/user/login"));

        assertTrue(driver.getPageSource().contains("Login"));
        assertTrue(driver.findElement(By.id("name")).isDisplayed());
        assertTrue(driver.findElement(By.id("password")).isDisplayed());
        assertTrue(driver.getPageSource().contains("Don't have an account?"));
    }

    @Test
    void registerPageShouldDisplayMainElements() {
        driver.get(baseUrl("/user/register"));

        assertTrue(driver.getPageSource().contains("Register"));
        assertTrue(driver.findElement(By.id("name")).isDisplayed());
        assertTrue(driver.findElement(By.id("password")).isDisplayed());
        assertTrue(driver.getPageSource().contains("Already have an account?"));
    }

    @Test
    void myPlansShouldAskForLoginWhenSessionIsMissing() {
        driver.get(baseUrl("/plans/myPlans"));

        assertTrue(driver.getPageSource().contains("You are not logged in"));
        assertTrue(driver.getPageSource().contains("Login"));
    }

    @Test
    void recipeIndexShouldBeReachable() {
        driver.get(baseUrl("/recipe/index"));

        assertTrue(driver.getPageSource().contains("Recipe List"));
    }

    @Test
    void recipeRootShouldBeReachable() {
        driver.get(baseUrl("/recipe/"));

        assertTrue(driver.getPageSource().contains("Recipe List"));
    }

    @Test
    void missingRecipeShouldRedirectToRecipeIndex() {
        driver.get(baseUrl("/recipe/999999"));

        assertTrue(driver.getCurrentUrl().contains("/recipe/index"));
    }

    @Test
    void plansCreateShouldAskForLoginWhenSessionIsMissing() {
        assertLoginRequiredPage("/plans/create");
    }

    @Test
    void plansCreateSelfShouldAskForLoginWhenSessionIsMissing() {
        assertLoginRequiredPage("/plans/create/self");
    }

    @Test
    void plansCreateDailyShouldAskForLoginWhenSessionIsMissing() {
        assertLoginRequiredPage("/plans/create/daily");
    }

    @Test
    void plansCreateWeeklyShouldAskForLoginWhenSessionIsMissing() {
        assertLoginRequiredPage("/plans/create/weekly");
    }

    @Test
    void plansCreateFamilyShouldAskForLoginWhenSessionIsMissing() {
        assertLoginRequiredPage("/plans/create/family");
    }

    @Test
    void adminPanelShouldAskForLoginWhenSessionIsMissing() {
        assertLoginRequiredPage("/admin/panel");
    }

    @Test
    void writerPanelShouldAskForLoginWhenSessionIsMissing() {
        assertLoginRequiredPage("/writer/panel");
    }

    private void assertLoginRequiredPage(String path) {
        driver.get(baseUrl(path));

        assertTrue(driver.getPageSource().contains("You are not logged in"));
        assertTrue(driver.getPageSource().contains("Login"));
    }

    private String baseUrl(String path) {
        return "http://localhost:" + port + path;
    }
}
