package com.bitbites.bitbites2;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Selenium UI tests for the BitBites web application.
 *
 * Prerequisites: the application must be running on http://localhost:8080
 * before executing these tests.
 *
 * Run the app with: ./gradlew bootRun
 * Then run these tests with: ./gradlew test --tests "com.bitbites.bitbites2.SeleniumUITests"
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SeleniumUITests {

    private static final String BASE_URL = "http://localhost:8080";

    private WebDriver driver;
    private WebDriverWait wait;

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
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // -------------------------------------------------------------------------
    // Page load tests
    // -------------------------------------------------------------------------

    @Test
    @Order(1)
    void testLoginPageLoads() {
        driver.get(BASE_URL + "/user/login");
        String title = driver.getTitle();
        assertNotNull(title, "Page title should not be null");
        assertFalse(title.isBlank(), "Page title should not be blank");
        WebElement heading = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.tagName("h3")));
        assertEquals("Login", heading.getText());
    }

    @Test
    @Order(2)
    void testRegisterPageLoads() {
        driver.get(BASE_URL + "/user/register");
        WebElement heading = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.tagName("h3")));
        assertEquals("Register", heading.getText());
    }

    @Test
    @Order(3)
    void testRecipeListPageLoads() {
        driver.get(BASE_URL + "/recipe/");
        WebElement heading = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.tagName("h2")));
        assertTrue(heading.getText().contains("Recipe"), "Heading should mention 'Recipe'");
    }

    // -------------------------------------------------------------------------
    // Form element / presence tests
    // -------------------------------------------------------------------------

    @Test
    @Order(4)
    void testLoginFormHasRequiredFields() {
        driver.get(BASE_URL + "/user/login");
        WebElement usernameField = driver.findElement(By.id("name"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));

        assertTrue(usernameField.isDisplayed(), "Username field should be visible");
        assertTrue(passwordField.isDisplayed(), "Password field should be visible");
        assertTrue(submitButton.isDisplayed(), "Submit button should be visible");

        assertEquals("text", usernameField.getAttribute("type"));
        assertEquals("password", passwordField.getAttribute("type"));
        assertNotNull(usernameField.getAttribute("required"), "Username field should be required");
        assertNotNull(passwordField.getAttribute("required"), "Password field should be required");
    }

    @Test
    @Order(5)
    void testRegisterFormHasRequiredFields() {
        driver.get(BASE_URL + "/user/register");
        WebElement usernameField = driver.findElement(By.id("name"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));

        assertTrue(usernameField.isDisplayed(), "Username field should be visible");
        assertTrue(passwordField.isDisplayed(), "Password field should be visible");
        assertTrue(submitButton.isDisplayed(), "Submit button should be visible");

        assertNotNull(usernameField.getAttribute("required"), "Username field should be required");
        assertNotNull(passwordField.getAttribute("required"), "Password field should be required");
    }

    // -------------------------------------------------------------------------
    // Navigation tests
    // -------------------------------------------------------------------------

    @Test
    @Order(6)
    void testNavbarContainsExpectedLinks() {
        driver.get(BASE_URL + "/user/login");
        List<WebElement> navLinks = driver.findElements(By.cssSelector("nav .nav-link"));
        List<String> linkTexts = navLinks.stream()
                .map(WebElement::getText)
                .toList();

        assertTrue(linkTexts.contains("Recipes"), "Navbar should have a 'Recipes' link");
        assertTrue(linkTexts.contains("Login"), "Navbar should have a 'Login' link when not logged in");
        assertTrue(linkTexts.contains("Register"), "Navbar should have a 'Register' link when not logged in");
    }

    @Test
    @Order(7)
    void testNavigateFromLoginToRegister() {
        driver.get(BASE_URL + "/user/login");
        WebElement registerLink = driver.findElement(By.cssSelector("a[href*='/user/register']"));
        registerLink.click();

        wait.until(ExpectedConditions.urlContains("/user/register"));
        assertTrue(driver.getCurrentUrl().contains("/user/register"),
                "Should navigate to register page");
    }

    @Test
    @Order(8)
    void testNavigateFromRegisterToLogin() {
        driver.get(BASE_URL + "/user/register");
        WebElement loginLink = driver.findElement(By.cssSelector("a[href*='/user/login']"));
        loginLink.click();

        wait.until(ExpectedConditions.urlContains("/user/login"));
        assertTrue(driver.getCurrentUrl().contains("/user/login"),
                "Should navigate back to login page");
    }

    @Test
    @Order(9)
    void testNavbarBrandNavigatesToHome() {
        driver.get(BASE_URL + "/user/login");
        WebElement brand = driver.findElement(By.cssSelector(".navbar-brand"));
        assertEquals("BitBites", brand.getText());
        brand.click();

        wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe(BASE_URL + "/user/login")));
        assertFalse(driver.getCurrentUrl().contains("/user/login"),
                "Clicking brand should navigate away from login page");
    }

    // -------------------------------------------------------------------------
    // Form validation / functionality tests
    // -------------------------------------------------------------------------

    @Test
    @Order(10)
    void testLoginWithInvalidCredentials() {
        driver.get(BASE_URL + "/user/login");

        driver.findElement(By.id("name")).sendKeys("nonexistent_user_xyz");
        driver.findElement(By.id("password")).sendKeys("wrongpassword123");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        WebElement errorMessage = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".alert-danger")));
        assertNotNull(errorMessage, "An error message should appear for invalid credentials");
        assertFalse(errorMessage.getText().isBlank(), "Error message should not be blank");
    }
}
