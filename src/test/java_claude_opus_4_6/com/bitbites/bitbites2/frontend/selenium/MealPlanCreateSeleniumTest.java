package com.bitbites.bitbites2.frontend.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Selenium tests for Meal Plan creation pages (/plans/create).
 * Tests plan type selection, form elements, and page structure.
 * Note: these pages require authentication, so unauthenticated access should redirect to login.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Meal Plan Creation Selenium Tests")
class MealPlanCreateSeleniumTest {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private String baseUrl;

    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        driver = new ChromeDriver(options);
        baseUrl = "http://localhost:" + port;
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // ==================== Authentication Guard ====================

    @Nested
    @DisplayName("Authentication Guard")
    class AuthenticationGuard {

        @Test
        @DisplayName("Create plan page redirects to login when not authenticated")
        void createPlanRedirectsToLogin() {
            driver.get(baseUrl + "/plans/create");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            // Should show login form since user is not authenticated
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("name")));
            // Error message should be displayed
            WebElement errorAlert = driver.findElement(By.cssSelector(".alert-danger"));
            assertEquals("You are not logged in", errorAlert.getText());
        }

        @Test
        @DisplayName("My Plans page redirects to login when not authenticated")
        void myPlansRedirectsToLogin() {
            driver.get(baseUrl + "/plans/myPlans");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("name")));
            WebElement errorAlert = driver.findElement(By.cssSelector(".alert-danger"));
            assertEquals("You are not logged in", errorAlert.getText());
        }

        @Test
        @DisplayName("Daily plan page redirects to login when not authenticated")
        void dailyPlanRedirectsToLogin() {
            driver.get(baseUrl + "/plans/create/daily");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("name")));
            WebElement errorAlert = driver.findElement(By.cssSelector(".alert-danger"));
            assertEquals("You are not logged in", errorAlert.getText());
        }

        @Test
        @DisplayName("Weekly plan page redirects to login when not authenticated")
        void weeklyPlanRedirectsToLogin() {
            driver.get(baseUrl + "/plans/create/weekly");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("name")));
            WebElement errorAlert = driver.findElement(By.cssSelector(".alert-danger"));
            assertEquals("You are not logged in", errorAlert.getText());
        }

        @Test
        @DisplayName("Family plan page redirects to login when not authenticated")
        void familyPlanRedirectsToLogin() {
            driver.get(baseUrl + "/plans/create/family");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("name")));
            WebElement errorAlert = driver.findElement(By.cssSelector(".alert-danger"));
            assertEquals("You are not logged in", errorAlert.getText());
        }

        @Test
        @DisplayName("Self plan page redirects to login when not authenticated")
        void selfPlanRedirectsToLogin() {
            driver.get(baseUrl + "/plans/create/self");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("name")));
            WebElement errorAlert = driver.findElement(By.cssSelector(".alert-danger"));
            assertEquals("You are not logged in", errorAlert.getText());
        }
    }

    // ==================== Admin Panel Guard ====================

    @Nested
    @DisplayName("Admin Panel Guard")
    class AdminPanelGuard {

        @Test
        @DisplayName("Admin panel redirects to login when not authenticated")
        void adminPanelRedirectsToLogin() {
            driver.get(baseUrl + "/admin/panel");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("name")));
            WebElement errorAlert = driver.findElement(By.cssSelector(".alert-danger"));
            assertEquals("You are not logged in", errorAlert.getText());
        }
    }

    // ==================== Writer Panel Guard ====================

    @Nested
    @DisplayName("Writer Panel Guard")
    class WriterPanelGuard {

        @Test
        @DisplayName("Writer panel redirects to login when not authenticated")
        void writerPanelRedirectsToLogin() {
            driver.get(baseUrl + "/writer/panel");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("name")));
            WebElement errorAlert = driver.findElement(By.cssSelector(".alert-danger"));
            assertEquals("You are not logged in", errorAlert.getText());
        }
    }

    // ==================== Login Page Form Validation ====================

    @Nested
    @DisplayName("Login Page Form Validation via Selenium")
    class LoginFormValidation {

        @Test
        @DisplayName("Login with nonexistent user shows error")
        void loginNonexistentUserShowsError() {
            driver.get(baseUrl + "/user/login");

            driver.findElement(By.id("name")).sendKeys("nonexistent_selenium_test_user");
            driver.findElement(By.id("password")).sendKeys("somepassword");
            driver.findElement(By.cssSelector("button[type='submit']")).click();

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".alert-danger")));

            WebElement errorAlert = driver.findElement(By.cssSelector(".alert-danger"));
            assertTrue(errorAlert.isDisplayed());
            assertEquals("User not found", errorAlert.getText());
        }

        @Test
        @DisplayName("Login form fields are empty after failed login attempt")
        void loginFieldsResetAfterFailure() {
            driver.get(baseUrl + "/user/login");

            driver.findElement(By.id("name")).sendKeys("nonexistent_user");
            driver.findElement(By.id("password")).sendKeys("wrongpass");
            driver.findElement(By.cssSelector("button[type='submit']")).click();

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".alert-danger")));

            // Fields should be cleared on page reload
            WebElement nameField = driver.findElement(By.id("name"));
            WebElement passwordField = driver.findElement(By.id("password"));
            assertEquals("", nameField.getAttribute("value"));
            assertEquals("", passwordField.getAttribute("value"));
        }
    }

    // ==================== Page Title Consistency ====================

    @Nested
    @DisplayName("Page Title Consistency")
    class PageTitleConsistency {

        @Test
        @DisplayName("All pages have 'BitBites' title")
        void allPagesHaveBitBitesTitle() {
            String[] paths = {"/user/login", "/user/register", "/recipe/", "/plans/create"};
            for (String path : paths) {
                driver.get(baseUrl + path);
                assertEquals("BitBites", driver.getTitle(),
                        "Page " + path + " should have 'BitBites' title");
            }
        }
    }

    // ==================== Responsive Layout Elements ====================

    @Nested
    @DisplayName("Layout Structure")
    class LayoutStructure {

        @Test
        @DisplayName("Login page has main content area")
        void loginPageMainContent() {
            driver.get(baseUrl + "/user/login");
            WebElement main = driver.findElement(By.cssSelector("main"));
            assertNotNull(main);
        }

        @Test
        @DisplayName("Recipe page uses container class")
        void recipePageContainer() {
            driver.get(baseUrl + "/recipe/");
            WebElement container = driver.findElement(By.cssSelector(".container"));
            assertNotNull(container);
        }

        @Test
        @DisplayName("Login page card is centered")
        void loginCardCentered() {
            driver.get(baseUrl + "/user/login");
            WebElement card = driver.findElement(By.cssSelector(".card.mx-auto"));
            assertNotNull(card);
        }
    }

    // ==================== Multi-page Flow ====================

    @Nested
    @DisplayName("Multi-page Navigation Flow")
    class MultiPageFlow {

        @Test
        @DisplayName("Navigate from recipes to login to register and back")
        void fullNavigationFlow() {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

            // Start at recipes
            driver.get(baseUrl + "/recipe/");
            assertTrue(driver.getPageSource().contains("Recipe List"));

            // Navigate to login via navbar
            WebElement loginLink = driver.findElement(By.cssSelector("a.nav-link[href*='/user/login']"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", loginLink);
            wait.until(ExpectedConditions.urlContains("/user/login"));

            // Navigate to register from login page
            driver.findElement(By.cssSelector("p.mt-3 a[href*='/user/register']")).click();
            wait.until(ExpectedConditions.urlContains("/user/register"));

            // Navigate back to recipes via navbar
            WebElement recipesLink = driver.findElement(By.cssSelector("a.nav-link[href*='/recipe']"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", recipesLink);
            wait.until(ExpectedConditions.urlContains("/recipe"));
            assertTrue(driver.getPageSource().contains("Recipe List"));
        }

        @Test
        @DisplayName("Accessing protected page shows login form with error")
        void protectedPageShowsLoginWithError() {
            driver.get(baseUrl + "/plans/myPlans");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".alert-danger")));

            // Should see the login form
            assertNotNull(driver.findElement(By.id("name")));
            assertNotNull(driver.findElement(By.id("password")));

            // And the "not logged in" error
            WebElement errorAlert = driver.findElement(By.cssSelector(".alert-danger"));
            assertEquals("You are not logged in", errorAlert.getText());
        }
    }
}
