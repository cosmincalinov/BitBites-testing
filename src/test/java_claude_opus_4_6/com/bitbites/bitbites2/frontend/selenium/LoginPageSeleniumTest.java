package com.bitbites.bitbites2.frontend.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
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
 * Selenium tests for the Login page (/user/login).
 * Tests page structure, form elements, navigation links, and form validation.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Login Page Selenium Tests")
class LoginPageSeleniumTest {

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
        @DisplayName("Login page loads successfully")
        void loginPageLoads() {
            driver.get(baseUrl + "/user/login");
            assertFalse(driver.getTitle().isEmpty());
        }

        @Test
        @DisplayName("Login page has correct title")
        void loginPageTitle() {
            driver.get(baseUrl + "/user/login");
            assertEquals("BitBites", driver.getTitle());
        }

        @Test
        @DisplayName("Login page URL is correct")
        void loginPageUrl() {
            driver.get(baseUrl + "/user/login");
            assertTrue(driver.getCurrentUrl().contains("/user/login"));
        }
    }

    // ==================== Page Structure ====================

    @Nested
    @DisplayName("Page Structure")
    class PageStructure {

        @BeforeEach
        void navigate() {
            driver.get(baseUrl + "/user/login");
        }

        @Test
        @DisplayName("Login heading is present")
        void loginHeadingPresent() {
            WebElement heading = driver.findElement(By.cssSelector("h3.card-title"));
            assertEquals("Login", heading.getText());
        }

        @Test
        @DisplayName("Login form is present")
        void loginFormPresent() {
            WebElement form = driver.findElement(By.cssSelector("form"));
            assertNotNull(form);
            assertTrue(form.getAttribute("action").contains("/user/login"));
            assertEquals("post", form.getAttribute("method").toLowerCase());
        }

        @Test
        @DisplayName("Username input field exists")
        void usernameFieldExists() {
            WebElement usernameInput = driver.findElement(By.id("name"));
            assertNotNull(usernameInput);
            assertEquals("text", usernameInput.getAttribute("type"));
            assertTrue(Boolean.parseBoolean(usernameInput.getAttribute("required")));
        }

        @Test
        @DisplayName("Password input field exists")
        void passwordFieldExists() {
            WebElement passwordInput = driver.findElement(By.id("password"));
            assertNotNull(passwordInput);
            assertEquals("password", passwordInput.getAttribute("type"));
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
        @DisplayName("Submit button is present with correct text")
        void submitButtonPresent() {
            WebElement button = driver.findElement(By.cssSelector("button[type='submit']"));
            assertEquals("Login", button.getText());
            assertTrue(button.getAttribute("class").contains("btn-primary"));
        }
    }

    // ==================== Form Interaction ====================

    @Nested
    @DisplayName("Form Interaction")
    class FormInteraction {

        @BeforeEach
        void navigate() {
            driver.get(baseUrl + "/user/login");
        }

        @Test
        @DisplayName("Can type into username field")
        void canTypeUsername() {
            WebElement usernameInput = driver.findElement(By.id("name"));
            usernameInput.sendKeys("testuser");
            assertEquals("testuser", usernameInput.getAttribute("value"));
        }

        @Test
        @DisplayName("Can type into password field")
        void canTypePassword() {
            WebElement passwordInput = driver.findElement(By.id("password"));
            passwordInput.sendKeys("testpass");
            assertEquals("testpass", passwordInput.getAttribute("value"));
        }

        @Test
        @DisplayName("Can clear and retype username")
        void canClearAndRetypeUsername() {
            WebElement usernameInput = driver.findElement(By.id("name"));
            usernameInput.sendKeys("first");
            usernameInput.clear();
            usernameInput.sendKeys("second");
            assertEquals("second", usernameInput.getAttribute("value"));
        }

        @Test
        @DisplayName("Submit with invalid credentials shows login page or error")
        void submitInvalidCredentials() {
            WebElement usernameInput = driver.findElement(By.id("name"));
            WebElement passwordInput = driver.findElement(By.id("password"));
            usernameInput.sendKeys("nonexistent_user_xyz");
            passwordInput.sendKeys("wrongpassword");
            driver.findElement(By.cssSelector("button[type='submit']")).click();

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            // After failed login, should stay on login page with error
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.urlContains("/user/login"),
                    ExpectedConditions.presenceOfElementLocated(By.cssSelector(".alert-danger"))
            ));
            assertTrue(driver.getCurrentUrl().contains("/user/login") ||
                    !driver.findElements(By.cssSelector(".alert-danger")).isEmpty());
        }
    }

    // ==================== Navigation Links ====================

    @Nested
    @DisplayName("Navigation Links")
    class NavigationLinks {

        @BeforeEach
        void navigate() {
            driver.get(baseUrl + "/user/login");
        }

        @Test
        @DisplayName("Register link is present")
        void registerLinkPresent() {
            WebElement registerLink = driver.findElement(By.cssSelector("p.mt-3 a[href*='/user/register']"));
            assertNotNull(registerLink);
            assertEquals("Register", registerLink.getText());
        }

        @Test
        @DisplayName("Register link navigates to register page")
        void registerLinkNavigates() {
            WebElement registerLink = driver.findElement(By.cssSelector("p.mt-3 a[href*='/user/register']"));
            registerLink.click();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.urlContains("/user/register"));
            assertTrue(driver.getCurrentUrl().contains("/user/register"));
        }

        @Test
        @DisplayName("Register prompt text is displayed")
        void registerPromptText() {
            WebElement prompt = driver.findElement(By.cssSelector("p.mt-3.text-center"));
            assertTrue(prompt.getText().contains("Don't have an account?"));
        }
    }

    // ==================== No Alerts Initially ====================

    @Nested
    @DisplayName("Initial Alert State")
    class InitialAlertState {

        @BeforeEach
        void navigate() {
            driver.get(baseUrl + "/user/login");
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
}
