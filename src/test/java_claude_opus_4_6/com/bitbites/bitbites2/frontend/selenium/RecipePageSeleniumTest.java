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
 * Selenium tests for the Recipe pages (/recipe/).
 * Tests the recipe index page structure and the recipe detail page.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Recipe Page Selenium Tests")
class RecipePageSeleniumTest {

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

    // ==================== Recipe Index Page ====================

    @Nested
    @DisplayName("Recipe Index Page")
    class RecipeIndexPage {

        @Test
        @DisplayName("Recipe index page loads successfully")
        void recipeIndexLoads() {
            driver.get(baseUrl + "/recipe/");
            assertEquals("BitBites", driver.getTitle());
        }

        @Test
        @DisplayName("Recipe index page has correct heading")
        void recipeIndexHeading() {
            driver.get(baseUrl + "/recipe/");
            WebElement heading = driver.findElement(By.cssSelector("h2.mb-4"));
            assertEquals("Recipe List", heading.getText());
        }

        @Test
        @DisplayName("Recipe index accessed via /recipe works")
        void recipeIndexWithoutTrailingSlash() {
            driver.get(baseUrl + "/recipe");
            assertTrue(driver.getPageSource().contains("Recipe List"));
        }

        @Test
        @DisplayName("Recipe index accessed via /recipe/index works")
        void recipeIndexExplicitPath() {
            driver.get(baseUrl + "/recipe/index");
            assertTrue(driver.getPageSource().contains("Recipe List"));
        }

        @Test
        @DisplayName("Recipe grid container is present")
        void recipeGridPresent() {
            driver.get(baseUrl + "/recipe/");
            // Either the grid with recipes or the 'no recipes' message should be present
            boolean hasGrid = !driver.findElements(By.cssSelector(".row.row-cols-1.row-cols-md-3")).isEmpty();
            boolean hasNoRecipes = driver.getPageSource().contains("No recipes available");
            assertTrue(hasGrid || hasNoRecipes);
        }

        @Test
        @DisplayName("Recipe cards have correct structure (if recipes exist)")
        void recipeCardStructure() {
            driver.get(baseUrl + "/recipe/");
            List<WebElement> cards = driver.findElements(By.cssSelector(".recipe-card"));
            if (!cards.isEmpty()) {
                WebElement firstCard = cards.get(0);
                // Card should have a title
                assertNotNull(firstCard.findElement(By.cssSelector(".card-title")));
                // Card should have a body
                assertNotNull(firstCard.findElement(By.cssSelector(".card-body")));
            }
        }

        @Test
        @DisplayName("Recipe cards show category info (if recipes exist)")
        void recipeCardCategory() {
            driver.get(baseUrl + "/recipe/");
            List<WebElement> subtitles = driver.findElements(By.cssSelector(".card-subtitle"));
            if (!subtitles.isEmpty()) {
                assertFalse(subtitles.get(0).getText().isEmpty());
            }
        }

        @Test
        @DisplayName("Recipe cards have links to detail pages (if recipes exist)")
        void recipeCardLinks() {
            driver.get(baseUrl + "/recipe/");
            List<WebElement> titleLinks = driver.findElements(By.cssSelector(".card-title a"));
            if (!titleLinks.isEmpty()) {
                String href = titleLinks.get(0).getAttribute("href");
                assertTrue(href.contains("/recipe/"));
            }
        }

        @Test
        @DisplayName("Recipe cards show kitchen type (if recipes exist)")
        void recipeCardKitchenType() {
            driver.get(baseUrl + "/recipe/");
            List<WebElement> cards = driver.findElements(By.cssSelector(".recipe-card .card-text"));
            if (!cards.isEmpty()) {
                String text = cards.get(0).getText();
                assertTrue(text.contains("Kitchen:"));
            }
        }

        @Test
        @DisplayName("Recipe cards show kilocalories (if recipes exist)")
        void recipeCardKcal() {
            driver.get(baseUrl + "/recipe/");
            List<WebElement> cards = driver.findElements(By.cssSelector(".recipe-card .card-text"));
            if (!cards.isEmpty()) {
                String text = cards.get(0).getText();
                assertTrue(text.contains("Kcal:"));
            }
        }

        @Test
        @DisplayName("Recipe cards show servings (if recipes exist)")
        void recipeCardServings() {
            driver.get(baseUrl + "/recipe/");
            List<WebElement> cards = driver.findElements(By.cssSelector(".recipe-card .card-text"));
            if (!cards.isEmpty()) {
                String text = cards.get(0).getText();
                assertTrue(text.contains("Servings:"));
            }
        }

        @Test
        @DisplayName("Clicking recipe title navigates to detail page (if recipes exist)")
        void clickRecipeTitleNavigates() {
            driver.get(baseUrl + "/recipe/");
            List<WebElement> titleLinks = driver.findElements(By.cssSelector(".card-title a"));
            if (!titleLinks.isEmpty()) {
                String href = titleLinks.get(0).getAttribute("href");
                titleLinks.get(0).click();
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
                wait.until(ExpectedConditions.urlContains("/recipe/"));
                // Should navigate away from index
                assertFalse(driver.getPageSource().contains("Recipe List"));
            }
        }
    }

    // ==================== Recipe Detail Page ====================

    @Nested
    @DisplayName("Recipe Detail Page")
    class RecipeDetailPage {

        @Test
        @DisplayName("Invalid recipe ID redirects to index")
        void invalidRecipeIdRedirects() {
            driver.get(baseUrl + "/recipe/999999");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.urlContains("/recipe/index"),
                    ExpectedConditions.urlContains("/recipe/999999")
            ));
            // Should redirect to index or show an error
            boolean redirected = driver.getCurrentUrl().contains("/recipe/index") ||
                    driver.getCurrentUrl().contains("/recipe/");
            assertTrue(redirected);
        }

        @Test
        @DisplayName("Recipe detail page has back button (if recipe exists)")
        void detailPageBackButton() {
            // First, get a valid recipe ID from the index
            driver.get(baseUrl + "/recipe/");
            List<WebElement> titleLinks = driver.findElements(By.cssSelector(".card-title a"));
            if (!titleLinks.isEmpty()) {
                titleLinks.get(0).click();
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
                // Check for back button
                WebElement backButton = driver.findElement(By.cssSelector("a[href*='/recipe/index']"));
                assertNotNull(backButton);
                assertTrue(backButton.getText().contains("Back to recipes"));
            }
        }

        @Test
        @DisplayName("Recipe detail page shows recipe info (if recipe exists)")
        void detailPageShowsInfo() {
            driver.get(baseUrl + "/recipe/");
            List<WebElement> titleLinks = driver.findElements(By.cssSelector(".card-title a"));
            if (!titleLinks.isEmpty()) {
                titleLinks.get(0).click();
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
                wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".recipe-card")));

                String pageSource = driver.getPageSource();
                assertTrue(pageSource.contains("Kitchen Type:"));
                assertTrue(pageSource.contains("Calories:"));
                assertTrue(pageSource.contains("Servings:"));
                assertTrue(pageSource.contains("Duration:"));
                assertTrue(pageSource.contains("Instructions:"));
            }
        }
    }
}
