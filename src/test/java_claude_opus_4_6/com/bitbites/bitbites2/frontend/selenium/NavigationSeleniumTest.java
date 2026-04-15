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
 * Selenium tests for the Navigation bar and Layout elements.
 * Tests navbar links, brand, footer, and theme toggle across all pages.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Navigation & Layout Selenium Tests")
class NavigationSeleniumTest {

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

    // ==================== Navbar Brand ====================

    @Nested
    @DisplayName("Navbar Brand")
    class NavbarBrand {

        @Test
        @DisplayName("Brand link is present on login page")
        void brandOnLoginPage() {
            driver.get(baseUrl + "/user/login");
            WebElement brand = driver.findElement(By.cssSelector(".navbar-brand"));
            assertEquals("BitBites", brand.getText());
        }

        @Test
        @DisplayName("Brand link is present on register page")
        void brandOnRegisterPage() {
            driver.get(baseUrl + "/user/register");
            WebElement brand = driver.findElement(By.cssSelector(".navbar-brand"));
            assertEquals("BitBites", brand.getText());
        }

        @Test
        @DisplayName("Brand link is present on recipe page")
        void brandOnRecipePage() {
            driver.get(baseUrl + "/recipe/");
            WebElement brand = driver.findElement(By.cssSelector(".navbar-brand"));
            assertEquals("BitBites", brand.getText());
        }

        @Test
        @DisplayName("Brand link points to home page")
        void brandLinkPointsHome() {
            driver.get(baseUrl + "/user/login");
            WebElement brand = driver.findElement(By.cssSelector(".navbar-brand"));
            assertTrue(brand.getAttribute("href").endsWith("/"));
        }
    }

    // ==================== Navbar Links (unauthenticated) ====================

    @Nested
    @DisplayName("Navbar Links (unauthenticated)")
    class NavbarLinksUnauthenticated {

        @BeforeEach
        void navigate() {
            driver.get(baseUrl + "/recipe/");
        }

        @Test
        @DisplayName("Recipes link is visible")
        void recipesLinkVisible() {
            List<WebElement> links = driver.findElements(By.cssSelector(".nav-link"));
            boolean hasRecipesLink = links.stream()
                    .anyMatch(l -> "Recipes".equals(l.getDomProperty("textContent").trim()));
            assertTrue(hasRecipesLink);
        }

        @Test
        @DisplayName("Meal Plans link is visible")
        void mealPlansLinkVisible() {
            List<WebElement> links = driver.findElements(By.cssSelector(".nav-link"));
            boolean hasMealPlansLink = links.stream()
                    .anyMatch(l -> "Meal Plans".equals(l.getDomProperty("textContent").trim()));
            assertTrue(hasMealPlansLink);
        }

        @Test
        @DisplayName("Grocery List link is visible")
        void groceryListLinkVisible() {
            List<WebElement> links = driver.findElements(By.cssSelector(".nav-link"));
            boolean hasGroceryLink = links.stream()
                    .anyMatch(l -> "Grocery List".equals(l.getDomProperty("textContent").trim()));
            assertTrue(hasGroceryLink);
        }

        @Test
        @DisplayName("Login link is visible when not authenticated")
        void loginLinkVisible() {
            List<WebElement> links = driver.findElements(By.cssSelector(".nav-link"));
            boolean hasLoginLink = links.stream()
                    .anyMatch(l -> "Login".equals(l.getDomProperty("textContent").trim()));
            assertTrue(hasLoginLink);
        }

        @Test
        @DisplayName("Register link is visible when not authenticated")
        void registerLinkVisible() {
            List<WebElement> links = driver.findElements(By.cssSelector(".nav-link"));
            boolean hasRegisterLink = links.stream()
                    .anyMatch(l -> "Register".equals(l.getDomProperty("textContent").trim()));
            assertTrue(hasRegisterLink);
        }

        @Test
        @DisplayName("Logout link is NOT visible when not authenticated")
        void logoutLinkNotVisible() {
            List<WebElement> links = driver.findElements(By.cssSelector(".nav-link"));
            boolean hasLogoutLink = links.stream()
                    .anyMatch(l -> "Logout".equals(l.getDomProperty("textContent").trim()));
            assertFalse(hasLogoutLink);
        }

        @Test
        @DisplayName("Admin Panel link is NOT visible when not authenticated")
        void adminPanelNotVisible() {
            List<WebElement> links = driver.findElements(By.cssSelector(".nav-link"));
            boolean hasAdminLink = links.stream()
                    .anyMatch(l -> "Admin Panel".equals(l.getDomProperty("textContent").trim()));
            assertFalse(hasAdminLink);
        }

        @Test
        @DisplayName("Writer Panel link is NOT visible when not authenticated")
        void writerPanelNotVisible() {
            List<WebElement> links = driver.findElements(By.cssSelector(".nav-link"));
            boolean hasWriterLink = links.stream()
                    .anyMatch(l -> "Writer Panel".equals(l.getDomProperty("textContent").trim()));
            assertFalse(hasWriterLink);
        }
    }

    // ==================== Navbar Navigation ====================

    @Nested
    @DisplayName("Navbar Navigation")
    class NavbarNavigation {

        @Test
        @DisplayName("Recipes link navigates to recipe page")
        void recipesLinkNavigates() {
            driver.get(baseUrl + "/user/login");
            WebElement recipesLink = driver.findElement(By.cssSelector("a.nav-link[href*='/recipe']"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", recipesLink);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.urlContains("/recipe"));
            assertTrue(driver.getCurrentUrl().contains("/recipe"));
        }

        @Test
        @DisplayName("Login link navigates to login page")
        void loginLinkNavigates() {
            driver.get(baseUrl + "/recipe/");
            WebElement loginLink = driver.findElement(By.cssSelector("a.nav-link[href*='/user/login']"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", loginLink);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.urlContains("/user/login"));
            assertTrue(driver.getCurrentUrl().contains("/user/login"));
        }

        @Test
        @DisplayName("Register link navigates to register page")
        void registerLinkNavigates() {
            driver.get(baseUrl + "/recipe/");
            WebElement registerLink = driver.findElement(By.cssSelector("a.nav-link[href*='/user/register']"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", registerLink);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.urlContains("/user/register"));
            assertTrue(driver.getCurrentUrl().contains("/user/register"));
        }

        @Test
        @DisplayName("Meal Plans link redirects to login when not authenticated")
        void mealPlansLinkRedirectsToLogin() {
            driver.get(baseUrl + "/recipe/");
            WebElement mealPlansLink = driver.findElement(By.cssSelector("a.nav-link[href*='/plans']"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", mealPlansLink);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            // Should show login page since user is not authenticated
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.urlContains("/user/login"),
                    ExpectedConditions.presenceOfElementLocated(By.id("name"))
            ));
        }
    }

    // ==================== Footer ====================

    @Nested
    @DisplayName("Footer")
    class Footer {

        @Test
        @DisplayName("Footer is present on login page")
        void footerOnLoginPage() {
            driver.get(baseUrl + "/user/login");
            WebElement footer = driver.findElement(By.cssSelector("footer"));
            assertNotNull(footer);
            assertTrue(footer.getText().contains("BitBites"));
        }

        @Test
        @DisplayName("Footer is present on recipe page")
        void footerOnRecipePage() {
            driver.get(baseUrl + "/recipe/");
            WebElement footer = driver.findElement(By.cssSelector("footer"));
            assertNotNull(footer);
        }

        @Test
        @DisplayName("Footer contains copyright text")
        void footerCopyrightText() {
            driver.get(baseUrl + "/user/login");
            WebElement footer = driver.findElement(By.cssSelector("footer"));
            assertTrue(footer.getText().contains("2025"));
            assertTrue(footer.getText().contains("BitBites"));
        }
    }

    // ==================== Theme Toggle ====================

    @Nested
    @DisplayName("Theme Toggle")
    class ThemeToggle {

        @Test
        @DisplayName("Theme toggle button exists")
        void themeToggleExists() {
            driver.get(baseUrl + "/user/login");
            WebElement toggleButton = driver.findElement(By.id("themeToggle"));
            assertNotNull(toggleButton);
        }

        @Test
        @DisplayName("Theme toggle button has correct aria-label")
        void themeToggleAriaLabel() {
            driver.get(baseUrl + "/user/login");
            WebElement toggleButton = driver.findElement(By.id("themeToggle"));
            assertEquals("Toggle Theme", toggleButton.getAttribute("aria-label"));
        }

        @Test
        @DisplayName("Theme toggle button is clickable")
        void themeToggleClickable() {
            driver.get(baseUrl + "/user/login");
            WebElement toggleButton = driver.findElement(By.id("themeToggle"));
            // Use JS click since button might be inside collapsed navbar
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", toggleButton);
        }
    }

    // ==================== Header ====================

    @Nested
    @DisplayName("Header")
    class Header {

        @Test
        @DisplayName("Header is present")
        void headerPresent() {
            driver.get(baseUrl + "/user/login");
            WebElement header = driver.findElement(By.cssSelector("header"));
            assertNotNull(header);
        }

        @Test
        @DisplayName("Navigation bar is present within header")
        void navbarPresent() {
            driver.get(baseUrl + "/user/login");
            WebElement navbar = driver.findElement(By.cssSelector("nav.navbar"));
            assertNotNull(navbar);
        }

        @Test
        @DisplayName("Navbar uses expand-lg variant")
        void navbarExpandLg() {
            driver.get(baseUrl + "/user/login");
            WebElement navbar = driver.findElement(By.cssSelector("nav.navbar"));
            assertTrue(navbar.getAttribute("class").contains("navbar-expand-lg"));
        }
    }

    // ==================== CSS and Styling ====================

    @Nested
    @DisplayName("CSS and External Resources")
    class CssAndResources {

        @Test
        @DisplayName("Bootstrap CSS is linked")
        void bootstrapCssLinked() {
            driver.get(baseUrl + "/user/login");
            List<WebElement> cssLinks = driver.findElements(By.cssSelector("link[rel='stylesheet']"));
            boolean hasBootstrap = cssLinks.stream()
                    .anyMatch(l -> l.getAttribute("href").contains("bootstrap"));
            assertTrue(hasBootstrap);
        }

        @Test
        @DisplayName("Custom CSS (style.css) is linked")
        void customCssLinked() {
            driver.get(baseUrl + "/user/login");
            List<WebElement> cssLinks = driver.findElements(By.cssSelector("link[rel='stylesheet']"));
            boolean hasCustomCss = cssLinks.stream()
                    .anyMatch(l -> l.getAttribute("href").contains("style.css"));
            assertTrue(hasCustomCss);
        }
    }
}
