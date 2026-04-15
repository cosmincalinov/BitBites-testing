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
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Selenium tests for the Register page (/user/register).
 * Tests page structure, form elements, navigation links, and form validation.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Register Page Selenium Tests")
class RegisterPageSeleniumTest {

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

    // ==================== Page Loading ====================

    @Nested
    @DisplayName("Page Loading")
    class PageLoading {

        @Test
        @DisplayName("Register page loads successfully")
        void registerPageLoads() {
            driver.get(baseUrl + "/user/register");
            assertFalse(driver.getTitle().isEmpty());
        }

        @Test
        @DisplayName("Register page has correct title")
        void registerPageTitle() {
            driver.get(baseUrl + "/user/register");
            assertEquals("BitBites", driver.getTitle());
        }

        @Test
        @DisplayName("Register page URL is correct")
        void registerPageUrl() {
            driver.get(baseUrl + "/user/register");
            assertTrue(driver.getCurrentUrl().contains("/user/register"));
        }
    }

    // ==================== Page Structure ====================

    @Nested
    @DisplayName("Page Structure")
    class PageStructure {

        @BeforeEach
        void navigate() {
            driver.get(baseUrl + "/user/register");
        }

        @Test
        @DisplayName("Register heading is present")
        void registerHeadingPresent() {
            WebElement heading = driver.findElement(By.cssSelector("h3.card-title"));
            assertEquals("Register", heading.getText());
        }

        @Test
        @DisplayName("Register form is present with correct action")
        void registerFormPresent() {
            WebElement form = driver.findElement(By.cssSelector("form"));
            assertNotNull(form);
            assertTrue(form.getAttribute("action").contains("/user/register"));
            assertEquals("post", form.getAttribute("method").toLowerCase());
        }

        @Test
        @DisplayName("Username input field exists with correct attributes")
        void usernameFieldExists() {
            WebElement usernameInput = driver.findElement(By.id("name"));
            assertNotNull(usernameInput);
            assertEquals("text", usernameInput.getAttribute("type"));
            assertEquals("name", usernameInput.getAttribute("name"));
            assertTrue(Boolean.parseBoolean(usernameInput.getAttribute("required")));
        }

        @Test
        @DisplayName("Password input field exists with correct attributes")
        void passwordFieldExists() {
            WebElement passwordInput = driver.findElement(By.id("password"));
            assertNotNull(passwordInput);
            assertEquals("password", passwordInput.getAttribute("type"));
            assertEquals("password", passwordInput.getAttribute("name"));
            assertTrue(Boolean.parseBoolean(passwordInput.getAttribute("required")));
        }

        @Test
        @DisplayName("Username label is present")
        void usernameLabelPresent() {
            WebElement label = driver.findElement(By.cssSelector("label[for='name']"));
            assertEquals("Username", label.getText());
        }

        @Test
        @DisplayName("Password label is present")
        void passwordLabelPresent() {
            WebElement label = driver.findElement(By.cssSelector("label[for='password']"));
            assertEquals("Password", label.getText());
        }

        @Test
        @DisplayName("Submit button is present with correct text and style")
        void submitButtonPresent() {
            WebElement button = driver.findElement(By.cssSelector("button[type='submit']"));
            assertEquals("Register", button.getText());
            assertTrue(button.getAttribute("class").contains("btn-success"));
        }

        @Test
        @DisplayName("Card container wraps the form")
        void cardContainerPresent() {
            WebElement card = driver.findElement(By.cssSelector(".card"));
            assertNotNull(card);
            WebElement cardBody = card.findElement(By.cssSelector(".card-body"));
            assertNotNull(cardBody);
        }
    }

    // ==================== Form Interaction ====================

    @Nested
    @DisplayName("Form Interaction")
    class FormInteraction {

        @BeforeEach
        void navigate() {
            driver.get(baseUrl + "/user/register");
        }

        @Test
        @DisplayName("Can type into username field")
        void canTypeUsername() {
            WebElement usernameInput = driver.findElement(By.id("name"));
            usernameInput.sendKeys("newuser");
            assertEquals("newuser", usernameInput.getAttribute("value"));
        }

        @Test
        @DisplayName("Can type into password field")
        void canTypePassword() {
            WebElement passwordInput = driver.findElement(By.id("password"));
            passwordInput.sendKeys("securepass123");
            assertEquals("securepass123", passwordInput.getAttribute("value"));
        }

        @Test
        @DisplayName("Both fields can be filled simultaneously")
        void canFillBothFields() {
            WebElement usernameInput = driver.findElement(By.id("name"));
            WebElement passwordInput = driver.findElement(By.id("password"));
            usernameInput.sendKeys("testuser");
            passwordInput.sendKeys("testpass");
            assertEquals("testuser", usernameInput.getAttribute("value"));
            assertEquals("testpass", passwordInput.getAttribute("value"));
        }

        @Test
        @DisplayName("Password field hides text")
        void passwordFieldHidesText() {
            WebElement passwordInput = driver.findElement(By.id("password"));
            assertEquals("password", passwordInput.getAttribute("type"));
        }
    }

    // ==================== Navigation Links ====================

    @Nested
    @DisplayName("Navigation Links")
    class NavigationLinks {

        @BeforeEach
        void navigate() {
            driver.get(baseUrl + "/user/register");
        }

        @Test
        @DisplayName("Login link is present")
        void loginLinkPresent() {
            WebElement loginLink = driver.findElement(By.cssSelector("p.mt-3 a[href*='/user/login']"));
            assertNotNull(loginLink);
            assertEquals("Login", loginLink.getText());
        }

        @Test
        @DisplayName("Login link navigates to login page")
        void loginLinkNavigates() {
            // Find the link in the form area (not the navbar)
            WebElement loginLink = driver.findElement(By.cssSelector("p.mt-3 a[href*='/user/login']"));
            loginLink.click();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.urlContains("/user/login"));
            assertTrue(driver.getCurrentUrl().contains("/user/login"));
        }

        @Test
        @DisplayName("Login prompt text is displayed")
        void loginPromptText() {
            WebElement prompt = driver.findElement(By.cssSelector("p.mt-3.text-center"));
            assertTrue(prompt.getText().contains("Already have an account?"));
        }
    }

    // ==================== No Alerts Initially ====================

    @Nested
    @DisplayName("Initial Alert State")
    class InitialAlertState {

        @BeforeEach
        void navigate() {
            driver.get(baseUrl + "/user/register");
        }

        @Test
        @DisplayName("No success alert on initial load")
        void noSuccessAlertInitially() {
            List<WebElement> successAlerts = driver.findElements(By.cssSelector(".alert-success"));
            assertTrue(successAlerts.isEmpty());
        }

        @Test
        @DisplayName("No error alert on initial load")
        void noErrorAlertInitially() {
            List<WebElement> errorAlerts = driver.findElements(By.cssSelector(".alert-danger"));
            assertTrue(errorAlerts.isEmpty());
        }
    }

    // ==================== Cross-navigation between Login and Register ====================

    @Nested
    @DisplayName("Cross-navigation")
    class CrossNavigation {

        @Test
        @DisplayName("Can navigate from register to login and back")
        void registerToLoginAndBack() {
            driver.get(baseUrl + "/user/register");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

            // Go to login via the form link
            driver.findElement(By.cssSelector("p.mt-3 a[href*='/user/login']")).click();
            wait.until(ExpectedConditions.urlContains("/user/login"));
            assertTrue(driver.getCurrentUrl().contains("/user/login"));

            // Go back to register via the form link
            driver.findElement(By.cssSelector("p.mt-3 a[href*='/user/register']")).click();
            wait.until(ExpectedConditions.urlContains("/user/register"));
            assertTrue(driver.getCurrentUrl().contains("/user/register"));
        }
    }
}
