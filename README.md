# BitBites

## Overview

BitBites is a Java-based meal planning application that generates weekly meal plans and grocery lists based on selected recipes. The system is designed with an object-oriented approach, utilizing inheritance and factory patterns to efficiently manage meal planning.


## Videos and Presentation
App presentation video here: [https://s.go.ro/iv7t6hs7](https://s.go.ro/iv7t6hs7)

Execution of tests: [https://s.go.ro/iv7t6hs7](https://s.go.ro/01vsnz9c)

Powerpoint Presentation: [Java Unit Testing](presentation/Java%20Unit%20Testing.pptx)

## Features

- Define **ingredients** and categorize them.
- Create **recipes** with detailed attributes (calories, servings, instructions, etc.).
- Generate meal plans of different types (daily, weekly, family).
- Create a grocery list based on selected recipes.
- Create a grocery list based on an generated meal plan.
- Use a **factory pattern** to create recipes from a URL.
- Automatically **aggregate ingredients** and handle **unit conversions**.
- **Store meal plans** for users.
- Create **user database** with hashed store password.
- Ensure meal plans meet **caloric constraints**.
- (Bonus) Provide a **web interface** for managing recipes, meal plans, and grocery lists.

---

## Project Logic

The system follows a structured approach with interconnected components. The core logic revolves around:

- **Ingredients**: Represented by simple records, categorized for easier management.
- **Grocery Items**: Defined by their ingredient, quantity, and unit, allowing aggregation and unit conversion.
- **Recipes**: Abstract representation of a meal, categorized into different types like `Dessert`, `MainCourse`, etc.
- **Recipe Factory**: Initially supports importing recipes from a single website, with future extensibility for more sources.
- **Schedule**: Defines the structure of daily meals, ensuring a balanced meal plan.
- **Meal Plans**:
    - **SelfMealPlan**: The user provides a predefined list of recipes, and no generation logic is needed.
    - **Other Meal Plans (Daily, Weekly, Family)**:
        - The user selects kitchen types and a preparation time range.
        - The system randomly picks recipes from the chosen kitchen types while ensuring the total calorie intake is balanced.
        - The **FamilyMealPlan** extends the Weekly plan by adjusting portions based on the number of people.
        - The system prevents duplicate meals and balances meal distribution throughout the week.

### Diagram Representation

The following diagram visually represents the core logic of **BitBites**:

```mermaid
%%{init: {'theme': 'default', 'look':'handDrawn'}}%%
graph TD;
    Ingredient -->|Used in| GroceryItem;
    GroceryList -->|Part of| Recipe;
    GroceryItem -->|Part of| GroceryList;
    Recipe -->|Categorized as| Dessert;
    Recipe -->|Categorized as| MainCourse;
    Recipe -->|Categorized as| Soup;
    Recipe -->|Categorized as| Appetizer;
    Recipe -->|Categorized as| Bread;
    Recipe -->|Categorized as| Salad;
    Recipe -->|Categorized as| Drink;
    RecipeFactory -->|Creates| Recipe;
    MealPlan -->|Contains| Schedule;
    MealPlan -->|Contains| Recipe;
    MealPlan -->|Contains| GroceryList;
    MealPlan -->|Specialized as| DailyMealPlan;
    MealPlan -->|Specialized as| WeeklyMealPlan;
    MealPlan -->|Specialized as| FamilyMealPlan;
    MealPlan -->|Specialized as| SelfMealPlan;
    User -->|Contains| MealPlan;
```

---
## Database

- implemented using ProsgreSQL 
- needed to add some Model classes in order to make the database
  - collections and arrays from the objects are transform in foreign keys in included classes
  - added records to store the id keys and the foreign keys
- added CRUD operations on all table (different functions for tables)

### Diagram
![img.png](img.png)
---

## Web Interface

The BitBites web application will provide the following functionalities:

- **Create and store recipes**.
- **Generate meal plans** of any type.
- **View the grocery list** for selected meal plans.
- **See the planned meals** for the week in a calendar format.

---

# Testing & Quality Assurance (QA) Documentation

This section details the software testing lifecycle for the BitBites application, including environment configuration, backend unit testing strategies, frontend UI automation, framework comparisons, and the use of AI tools during the testing phase.

## 1. Test Environment Configuration

### Hardware & Software Specifications
* **Operating System:** Windows 11 (64-bit)
* **Execution Environment:** Local Machine (No VM used)
* **Java Development Kit:** JDK 21.0.6
* **Test Database:** Local PostgreSQL (Default port 5432)

### Tools and Versions
* **Backend Framework:** JUnit 5 (Jupiter API)
* **Frontend Automation:** Selenium WebDriver (v4.31.0)
* **Web Browser Driver:** Firefox GeckoDriver (v0.36.0) running in `--headless` mode
* **Code Coverage Tool:** IntelliJ IDEA Built-in Coverage Runner
* **Build Tool:** Gradle


![Config](images/gradle.png)

---

## 2. Unit Testing & Design Strategies (Backend)

The logic of the application was tested using JUnit 5. The testing phase applied usual software engineering strategies to ensure a good decision coverage. 

While multiple classes were fully tested (`GroceryList`, `MealPlanFactory`, `Recipe`, `PasswordUtils`), the `UnitConverter` class is used as our primary showcase for documenting the approch we chose.

### 2.1. Equivalence Class Partitioning
We divided the input data into equivalent partitions to reduce the number of test cases while maintaining full coverage. 

| Partition | Scenario Description | Expected Outcome | Associated Test Method |
| :--- | :--- | :--- | :--- |
| **C1** | Identity (e.g., GRAM -> GRAM) | Returns the exact same quantity | `testConvertSameUnit()` |
| **C2** | Same category (e.g., GRAM -> KILOGRAM) | Applies standard mathematical multiplier | `testConverterSameCategory()` |
| **C3** | Cross-category (e.g., GRAM -> CUP) | Uses density-based formulas for conversion | `testConverterDiffCategories()` |
| **C4** | Incompatible units (e.g., PIECE -> LITER) | Throws `IllegalArgumentException` | `testConverterIncompatible()` |

### 2.2. Boundary Value Analysis (BVA)
To ensure the system handles correctly extreme or unexpected values, we tested the absolute boundaries of the conversion inputs.

* **Lower Bound (T1):** Tested an input quantity of `0`. Result: Handled safely, returning `0`.
* **Invalid Input (T2):** Tested a negative quantity (`-1`). Result: Expected to throw an exception to prevent corrupted grocery lists.
* **Fractional Precision (T3):** Tested a very small value (`0.000001`) to verify decimal accuracy without floating-point errors.
* **Maximum Threshold (T4):** Tested a massive value (`1,000,000`) to ensure no integer overflow occurs during multiplication.

![Tests](images/tests.png)

### 2.3. Code Coverage & Code Complexity
* **Testing Every Scenario:** We made sure our tests hit every single option in the `UnitConverter` so that no block of code is left untested.
* **Managing Complex Logic:** The conversion feature has a lot of possible paths the code can take. To test this reliably without making things overly complicated, we focused on verifying the main scenarios: what happens when a conversion succeeds, what happens when the units are already the same, and what happens when the system correctly catches an error.

![Test_with_Coverage](images/test_with_coverage.png)

---

## 3. Web UI Automation Testing (Frontend)

The Graphical User Interface was tested using **Selenium WebDriver**. To speed up execution and allow tests to run seamlessly in the background, the Firefox browser was configured to run in `--headless` mode.

We executed 10 distinct automated scenarios across the application:
1. **Page Load Verification:** Confirmed that the URL routes for `/user/login`, `/user/register`, and `/recipe/` successfully load the correct `<h2>` and `<h3>` title headers.
2. **Form Validation:** Checked that authentication forms contain the correct HTML `required` attributes and that password fields are securely hidden (`type="password"`).
3. **Routing and Navigation:** Simulated clicks on the Navbar (Brand Logo, Login, Register) to ensure the system redirects users to the correct endpoints without dead links.
4. **Error Handling:** Simulated a login attempt with an invalid username and password to verify that the application renders the correct `.alert-danger` UI component.

---

## 4. Comparative Study: JUnit 5 vs. TestNG

To evaluate the efficiency of different testing tools, the QA team implemented identical test scenarios for the `UnitConverter` and `GroceryList` classes in both **JUnit 5** and **TestNG**.

| Evaluation Metric | JUnit 5 Implementation | TestNG Implementation | Team Interpretation |
| :--- | :--- | :--- | :--- |
| **Data-Driven Tests** | Used `@ParameterizedTest` and `@CsvSource`. | Used `@DataProvider`. | **TestNG** handled multiple data sets more elegantly, reducing duplicate lines of code for our math-heavy tests. |
| **Test Grouping** | Used `@Tag("backend")`. | Used `@Test(groups={"backend"})`. | **TestNG** offers better suite management via XML configurations, but JUnit tags were easier to run via Gradle. |
| **Exception Testing** | Used inline `assertThrows()`. | Used `@Test(expectedExceptions)`. | **JUnit 5** is superior here because it allows us to pinpoint exactly which line of code throws the error, preventing false positives. |
| **Parallel Execution** | Requires manual external configuration. | Native support via `threadPoolSize`. | **TestNG** executed the suite slightly faster (~1.0s vs ~1.2s) due to built-in multithreading. |

**Final Conclusion:** While TestNG showed strong advantages in parallelization and data-driven testing, the team selected **JUnit 5** as the primary framework because of its seamless, native integration with the Spring Boot ecosystem.

---

## 5. AI Tool Usage Report

During the testing lifecycle, the team utilized Artificial Intelligence tools (**GitHub Copilot** and **Google Gemini**) strictly as productivity assistants. 

### 5.1. GitHub Copilot (Test Generation & Brainstorming)
* **Usage:** Copilot was used inside the IDE to quickly generate the boilerplate code for the TestNG comparative study and to suggest equivalence classes.
* **Prompt Example:** *"Convert this JUnit 5 test class `UnitConverterTest` into a TestNG class using a DataProvider for the different conversion categories."*
* **Response / Autogenerated Code:** Copilot successfully generated the `@DataProvider` structure, extracting our hardcoded JUnit values into a 2D array. 
* **Comparison & Differences:** The autogenerated suite was structurally correct but lacked context. Copilot suggested testing random incompatible units (like converting "meters" to "grams"), which do not exist in our system. The team had to manually intervene to restrict the test data exclusively to our custom `Unit` enums (e.g., `GRAM`, `CUP`, `PIECE`).

### 5.2. Google Gemini (Documentation & Structuring)
* **Usage:** Used to format the final Markdown documentation, translate technical QA jargon into plain English, and generate the comparative tables.
* **Interpretation:** The AI significantly reduced the time required for technical writing, allowing the team to focus purely on software architecture and debugging.

**Declaration:** The AI tools were not used to write the core business logic. The team fully assumes responsibility for the system architecture, manual assertion validations, and database configurations.

---

## 6. References & Bibliography
1. JUnit 5 User Guide. (n.d.). *JUnit 5*. Retrieved from https://junit.org/junit5/docs/current/user-guide/
2. Selenium WebDriver Documentation. (n.d.). *Selenium*. Retrieved from https://www.selenium.dev/documentation/webdriver/
3. Spring Boot Reference Documentation. (n.d.). *Spring*. Retrieved from https://docs.spring.io/spring-boot/docs/current/reference/html/
4. Pressman, R. S. (2014). *Software Engineering: A Practitioner's Approach* (8th ed.). McGraw-Hill Education. (Reference for Equivalence Partitioning and Boundary Value Analysis).
---

## License

MIT License.
