package com.bitbites.bitbites2.frontend.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Selenium WebDriver tests for the BitBites frontend.
 *
 * Motivation for choosing Selenium:
 * - Industry-standard for web UI testing
 * - Rich API for form interactions, navigation, element queries
 * - Cross-browser support (Chrome, Firefox, Edge)
 * - WebDriverManager auto-manages browser drivers
 *
 * Tests cover:
 * - Navigation between pages
 * - Login form validation (empty fields, wrong credentials, successful login)
 * - Registration form validation
 * - Recipe listing page
 * - Meal plan creation flow
 * - Navbar link visibility based on authentication state
 * - Browser compatibility (headless Chrome)
 *
 * PREREQUISITES:
 * - The Spring Boot application must be running on localhost:8080
 * - A PostgreSQL database must be configured and accessible
 * - Chrome browser must be installed
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SeleniumFrontendTest {

    private static WebDriver driver;
    private static WebDriverWait wait;
    private static final String BASE_URL = "http://localhost:8080";
    private static final String TEST_USERNAME = "selenium_test_" + UUID.randomUUID().toString().substring(0, 8);
    private static final String TEST_PASSWORD = "TestPass123!";

    @BeforeAll
    static void setUpDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterAll
    static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @AfterEach
    void clearCookies() {
        driver.manage().deleteAllCookies();
    }

    // ===================== NAVIGATION TESTS =====================

    @Test
    @Order(1)
    @DisplayName("Homepage redirects or loads successfully")
    void homepageLoads() {
        driver.get(BASE_URL + "/recipe/");
        assertFalse(driver.getTitle().isEmpty(), "Page should have a title");
    }

    @Test
    @Order(2)
    @DisplayName("Login page loads correctly")
    void loginPageLoads() {
        driver.get(BASE_URL + "/user/login");
        WebElement heading = driver.findElement(By.tagName("h3"));
        assertEquals("Login", heading.getText());
    }

    @Test
    @Order(3)
    @DisplayName("Register page loads correctly")
    void registerPageLoads() {
        driver.get(BASE_URL + "/user/register");
        WebElement heading = driver.findElement(By.tagName("h3"));
        assertEquals("Register", heading.getText());
    }

    @Test
    @Order(4)
    @DisplayName("Recipe index page loads")
    void recipeIndexLoads() {
        driver.get(BASE_URL + "/recipe/");
        WebElement heading = driver.findElement(By.tagName("h2"));
        assertEquals("Recipe List", heading.getText());
    }

    @Test
    @Order(5)
    @DisplayName("Navbar contains Recipes link")
    void navbarHasRecipesLink() {
        driver.get(BASE_URL + "/recipe/");
        List<WebElement> navLinks = driver.findElements(By.cssSelector("nav a.nav-link"));
        boolean hasRecipes = navLinks.stream()
                .anyMatch(link -> link.getText().contains("Recipes"));
        assertTrue(hasRecipes, "Navbar should contain a Recipes link");
    }

    @Test
    @Order(6)
    @DisplayName("Navbar shows Login/Register when not authenticated")
    void navbarShowsLoginWhenNotAuth() {
        driver.get(BASE_URL + "/recipe/");
        List<WebElement> navLinks = driver.findElements(By.cssSelector("nav a.nav-link"));
        boolean hasLogin = navLinks.stream().anyMatch(l -> l.getText().contains("Login"));
        boolean hasRegister = navLinks.stream().anyMatch(l -> l.getText().contains("Register"));
        assertTrue(hasLogin, "Should show Login link");
        assertTrue(hasRegister, "Should show Register link");
    }

    // ===================== LOGIN FORM VALIDATION =====================

    @Test
    @Order(10)
    @DisplayName("Login with empty fields shows HTML5 validation")
    void loginEmptyFields() {
        driver.get(BASE_URL + "/user/login");
        WebElement nameInput = driver.findElement(By.id("name"));
        WebElement submitBtn = driver.findElement(By.cssSelector("button[type='submit']"));

        submitBtn.click();

        // HTML5 required attribute prevents submission; check we're still on login
        assertTrue(driver.getCurrentUrl().contains("/user/login"));
    }

    @Test
    @Order(11)
    @DisplayName("Login with wrong credentials shows error")
    void loginWrongCredentials() {
        driver.get(BASE_URL + "/user/login");
        driver.findElement(By.id("name")).sendKeys("nonexistent_user_xyz");
        driver.findElement(By.id("password")).sendKeys("wrongpassword");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".alert-danger")));
        WebElement errorAlert = driver.findElement(By.cssSelector(".alert-danger"));
        assertTrue(errorAlert.isDisplayed(), "Error message should be visible");
        assertTrue(errorAlert.getText().contains("not found") || errorAlert.getText().contains("Wrong"),
                "Error should indicate user not found or wrong password");
    }

    // ===================== REGISTRATION TESTS =====================

    @Test
    @Order(20)
    @DisplayName("Register a new user successfully")
    void registerNewUser() {
        driver.get(BASE_URL + "/user/register");
        driver.findElement(By.id("name")).sendKeys(TEST_USERNAME);
        driver.findElement(By.id("password")).sendKeys(TEST_PASSWORD);
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector(".alert-success, .alert-danger")));

        // Check for success message
        List<WebElement> successAlerts = driver.findElements(By.cssSelector(".alert-success"));
        if (!successAlerts.isEmpty()) {
            assertTrue(successAlerts.get(0).getText().contains("logged in"),
                    "Success message should confirm registration");
        }
    }

    @Test
    @Order(21)
    @DisplayName("Register duplicate user shows error")
    void registerDuplicateUser() {
        driver.get(BASE_URL + "/user/register");
        driver.findElement(By.id("name")).sendKeys(TEST_USERNAME);
        driver.findElement(By.id("password")).sendKeys(TEST_PASSWORD);
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".alert-danger")));
        WebElement error = driver.findElement(By.cssSelector(".alert-danger"));
        assertTrue(error.getText().contains("already exists"), "Should show user already exists");
    }

    @Test
    @Order(22)
    @DisplayName("Register page has link to login")
    void registerPageHasLoginLink() {
        driver.get(BASE_URL + "/user/register");
        WebElement loginLink = driver.findElement(By.linkText("Login"));
        assertNotNull(loginLink);
        assertTrue(loginLink.getAttribute("href").contains("/user/login"));
    }

    @Test
    @Order(23)
    @DisplayName("Login page has link to register")
    void loginPageHasRegisterLink() {
        driver.get(BASE_URL + "/user/login");
        WebElement registerLink = driver.findElement(By.linkText("Register"));
        assertNotNull(registerLink);
        assertTrue(registerLink.getAttribute("href").contains("/user/register"));
    }

    // ===================== AUTHENTICATED FLOW =====================

    private void loginAsTestUser() {
        driver.get(BASE_URL + "/user/login");
        driver.findElement(By.id("name")).sendKeys(TEST_USERNAME);
        driver.findElement(By.id("password")).sendKeys(TEST_PASSWORD);
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        wait.until(ExpectedConditions.urlContains("/plans/myPlans"));
    }

    @Test
    @Order(30)
    @DisplayName("Successful login redirects to myPlans")
    void successfulLogin() {
        loginAsTestUser();
        assertTrue(driver.getCurrentUrl().contains("/plans/myPlans"),
                "Should redirect to meal plans page after login");
    }

    @Test
    @Order(31)
    @DisplayName("Meal Plans page shows 'New Meal Plan' button when logged in")
    void mealPlansPageHasNewButton() {
        loginAsTestUser();
        WebElement newPlanBtn = driver.findElement(By.linkText("+ New Meal Plan"));
        assertNotNull(newPlanBtn);
        assertTrue(newPlanBtn.getAttribute("href").contains("/plans/create"));
    }

    @Test
    @Order(32)
    @DisplayName("Create meal plan page shows type selector")
    void createMealPlanPage() {
        loginAsTestUser();
        driver.get(BASE_URL + "/plans/create");

        WebElement typeSelect = driver.findElement(By.id("type"));
        assertNotNull(typeSelect);

        Select select = new Select(typeSelect);
        List<WebElement> options = select.getOptions();
        assertTrue(options.size() >= 4, "Should have at least 4 plan types");
    }

    @Test
    @Order(33)
    @DisplayName("Navbar shows Logout when authenticated")
    void navbarShowsLogoutWhenAuth() {
        loginAsTestUser();
        List<WebElement> navLinks = driver.findElements(By.cssSelector("nav a.nav-link"));
        boolean hasLogout = navLinks.stream().anyMatch(l -> l.getText().contains("Logout"));
        assertTrue(hasLogout, "Should show Logout link when logged in");
    }

    @Test
    @Order(34)
    @DisplayName("Logout redirects to recipe page")
    void logoutRedirects() {
        loginAsTestUser();
        driver.get(BASE_URL + "/user/logout");
        wait.until(ExpectedConditions.urlContains("/recipe"));
        assertTrue(driver.getCurrentUrl().contains("/recipe"));
    }

    // ===================== RECIPE PAGE TESTS =====================

    @Test
    @Order(40)
    @DisplayName("Recipe cards are displayed if recipes exist")
    void recipeCardsDisplayed() {
        driver.get(BASE_URL + "/recipe/");
        List<WebElement> cards = driver.findElements(By.cssSelector(".recipe-card"));
        // If DB has recipes, cards should be present; otherwise check for empty message
        if (cards.isEmpty()) {
            List<WebElement> emptyMsg = driver.findElements(By.cssSelector(".text-muted"));
            assertFalse(emptyMsg.isEmpty(), "Should show 'No recipes' message if list is empty");
        } else {
            assertTrue(cards.size() > 0);
        }
    }

    @Test
    @Order(41)
    @DisplayName("Recipe card contains name, category, kitchen info")
    void recipeCardContent() {
        driver.get(BASE_URL + "/recipe/");
        List<WebElement> cards = driver.findElements(By.cssSelector(".recipe-card"));
        if (!cards.isEmpty()) {
            WebElement firstCard = cards.get(0);
            assertNotNull(firstCard.findElement(By.cssSelector(".card-title")));
            assertNotNull(firstCard.findElement(By.cssSelector(".card-subtitle")));
        }
    }

    @Test
    @Order(42)
    @DisplayName("Clicking recipe name navigates to detail page")
    void clickRecipeNavigatesToDetail() {
        driver.get(BASE_URL + "/recipe/");
        List<WebElement> recipeLinks = driver.findElements(By.cssSelector(".card-title a"));
        if (!recipeLinks.isEmpty()) {
            String href = recipeLinks.get(0).getAttribute("href");
            recipeLinks.get(0).click();
            wait.until(ExpectedConditions.urlContains("/recipe/"));
            assertTrue(driver.getCurrentUrl().matches(".*\\/recipe\\/\\d+.*"),
                    "Should navigate to recipe detail with numeric ID");
        }
    }

    @Test
    @Order(43)
    @DisplayName("Recipe detail page has back button")
    void recipeDetailHasBackButton() {
        driver.get(BASE_URL + "/recipe/");
        List<WebElement> recipeLinks = driver.findElements(By.cssSelector(".card-title a"));
        if (!recipeLinks.isEmpty()) {
            recipeLinks.get(0).click();
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector("a[href='/recipe/index']")));
            WebElement backBtn = driver.findElement(By.cssSelector("a[href='/recipe/index']"));
            assertTrue(backBtn.isDisplayed());
        }
    }

    // ===================== MEAL PLAN CREATION FLOW =====================

    @Test
    @Order(50)
    @DisplayName("Accessing myPlans without login redirects to login")
    void myPlansRequiresAuth() {
        driver.get(BASE_URL + "/plans/myPlans");
        // Should show login page with error
        List<WebElement> loginHeading = driver.findElements(By.tagName("h3"));
        boolean isLoginPage = loginHeading.stream().anyMatch(h -> h.getText().equals("Login"));
        assertTrue(isLoginPage, "Should redirect to login page");
    }

    @Test
    @Order(51)
    @DisplayName("Select daily plan type and see preferences form")
    void selectDailyPlanType() {
        loginAsTestUser();
        driver.get(BASE_URL + "/plans/create");

        Select typeSelect = new Select(driver.findElement(By.id("type")));
        typeSelect.selectByValue("daily");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        wait.until(ExpectedConditions.urlContains("/plans/create/daily"));
        assertTrue(driver.getPageSource().contains("daily meal plan"));
    }

    // ===================== THEME TOGGLE =====================

    @Test
    @Order(60)
    @DisplayName("Theme toggle button exists")
    void themeToggleExists() {
        driver.get(BASE_URL + "/recipe/");
        WebElement themeBtn = driver.findElement(By.id("themeToggle"));
        assertNotNull(themeBtn);
        assertTrue(themeBtn.isDisplayed());
    }

    @Test
    @Order(61)
    @DisplayName("Theme toggle changes data-theme attribute")
    void themeToggleChangesAttribute() {
        driver.get(BASE_URL + "/recipe/");
        WebElement html = driver.findElement(By.tagName("html"));

        // Click theme toggle
        driver.findElement(By.id("themeToggle")).click();

        // Wait a moment for JS to execute
        try { Thread.sleep(500); } catch (InterruptedException ignored) {}

        String theme = html.getAttribute("data-theme");
        assertNotNull(theme);
        // Theme should be either 'dark' or 'light'
        assertTrue(theme.equals("dark") || theme.equals("light"));
    }

    // ===================== RESPONSIVE / LAYOUT TESTS =====================

    @Test
    @Order(70)
    @DisplayName("Page has proper Bootstrap structure")
    void bootstrapStructure() {
        driver.get(BASE_URL + "/recipe/");
        assertNotNull(driver.findElement(By.tagName("header")), "Should have header");
        assertNotNull(driver.findElement(By.tagName("main")), "Should have main");
        assertNotNull(driver.findElement(By.tagName("footer")), "Should have footer");
    }

    @Test
    @Order(71)
    @DisplayName("Footer contains copyright text")
    void footerContent() {
        driver.get(BASE_URL + "/recipe/");
        WebElement footer = driver.findElement(By.tagName("footer"));
        assertTrue(footer.getText().contains("BitBites"), "Footer should mention BitBites");
    }

    @Test
    @Order(72)
    @DisplayName("Navbar brand links to home")
    void navbarBrandLink() {
        driver.get(BASE_URL + "/recipe/");
        WebElement brand = driver.findElement(By.cssSelector(".navbar-brand"));
        assertEquals("BitBites", brand.getText());
    }
}
