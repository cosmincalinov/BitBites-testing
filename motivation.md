## 📋 Project Overview

BitBites is a Spring Boot meal planning app with: unit conversion, grocery list management, recipe management (with web scraping), meal plan generation (Daily/Weekly/Family/Self), user auth, and role-based access (COOKER/WRITER/ADMIN).

---

## 🎯 REQUIREMENT 1: Unit Testing a Class (with all strategies)

### ⭐ Primary Target: [UnitConverter](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html) — [UnitConverter.java](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html)

This is your **best class** for demonstrating ALL testing strategies. Here's why:

#### a) Equivalence Class Partitioning

The [convert(double, Unit, Unit)](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html) method naturally partitions into:

- **Mass→Mass** conversions (GRAM↔KILOGRAM)
- **Volume→Volume** conversions (LITER↔MILLILITER)
- **Count→Any** conversions (CUP↔GRAM, SPOON↔MILLILITER, etc.)
- **Same unit→Same unit** (identity conversion)
- **Incompatible pairs** (GRAM→PIECE — should throw `IllegalArgumentException`)
- **PIECE→PIECE** (the only valid PIECE conversion)

#### b) Boundary Value Analysis

- [quantity = 0](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html) (zero conversion)
- [quantity = 0.001](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html) (very small positive)
- [quantity = -1](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html) (negative — does it handle it?)
- [quantity = Double.MAX_VALUE](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html) (overflow risk in multiplication like [quantity * 1000.0](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html))
- [quantity = 1000.0](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html) for GRAM→KILOGRAM (exact boundary = 1.0 KG)
- [quantity = 999.999](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html) just below boundary
- [quantity = 1](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html) (smallest meaningful unit)

#### c) Statement Coverage

You need tests that hit every `case` in both the outer switch (GRAM, KILOGRAM, LITER, MILLILITER, CUP, SPOON, PIECE) and inner switches. That's ~30+ case branches at [UnitConverter.java:17-68](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html).

#### d) Decision & Condition Coverage

- The guard at [UnitConverter.java:13](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html): [if (!forUnit.isCompatibleWith(toUnit))](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html) — test both `true` (incompatible) and `false` (compatible).
- Pair this with [Unit.isCompatibleWith()](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html) at [Unit.java:37](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html) which has a compound condition: `this.type.equals("count") || other.type.equals("count")` — requires 4 combinations for MC/DC.

#### e) Independent Circuits (CFG paths)

The nested switch creates many independent paths through the CFG. For example:

- Path 1: GRAM → GRAM (identity)
- Path 2: GRAM → KILOGRAM (÷1000)
- Path 3: GRAM → PIECE (exception)
- Path 4: incompatible at guard (exception before switch)

Each outer switch branch × inner switch branch = an independent circuit.

#### f) Mutation Testing

Run a mutant generator (PIT). Likely surviving mutants:

- Changing `/ 1000.0` to `* 1000.0` (arithmetic operator replacement)
- Changing `/ 20.0` to `/ 200.0` (constant replacement)
- Removing the `!` in [!forUnit.isCompatibleWith(toUnit)](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html) (negation removal)
- Changing [quantity](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html) return to `0` (return value mutation)

You'll write 2 additional tests to kill the surviving non-equivalent mutants.

---

### ⭐ Secondary Target: [GroceryList](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html) — [GroceryList.java](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html)

#### [addItem()](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html) — [GroceryList.java:23-40](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html)

- **Equivalence partitioning**: (1) add new ingredient, (2) add existing ingredient with compatible units → merge, (3) add existing ingredient with incompatible units → replace
- **Decision coverage**: [if (!items.containsKey(ingredient))](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html) true/false, plus the inner `try/catch`
- **Condition coverage**: the [containsKey](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html) check
- **Boundary**: add to empty list, add same item twice, add item with quantity=0

#### [addLists()](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html) — [GroceryList.java:48-59](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html)

- Two empty lists, one empty + one full, two lists with overlapping ingredients, two lists with no overlap

#### [sortedGroceryList()](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html) — [GroceryList.java:68-81](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html)

- Tests with items convertible to GRAM (should sort descending), items NOT convertible (catch block → return 0), mix of both

---

### ⭐ Additional Targets for Full Coverage

|Class|Method(s)|Why test it|
|---|---|---|
|[Recipe.java](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html)|[setDuration(double, String)](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html) [Recipe.java:65-78](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html)|Equivalence: "hours"/"h"/"hour", "minutes"/"m"/"minute", "seconds"/"s"/"second", invalid string → null. Boundary: 0, negative|
|[Recipe.java](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html)|[formatDuration()](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html) [Recipe.java:119-128](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html)|Boundary: 0 min, 59 min, 60 min (=1h), 61 min, plural vs singular ("1 hour" vs "2 hours", "1 minute" vs "0 minutes")|
|[Unit.java](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html)|[isCompatibleWith()](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html) [Unit.java:32-41](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html)|MC/DC for `this.type.equals("count") \| other.type.equals("count")`. 4 test cases needed|
|[Ingredient.java](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html)|[equals()](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html) [Ingredient.java:13-18](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html)|Same name diff category, diff name, non-Ingredient object, null, same object|
|[User.java](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html)|[equals()](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html) [User.java:40-45](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html)|Same reference, same username diff password, diff username, non-User object|
|[PasswordUtils.java](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html)|[hashPassword()](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html)|Deterministic: same input → same hash. Different inputs → different hashes. Empty string. Long string|
|[MealPlanFactory.java](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html)|[createMealPlan()](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html)|"daily"→DailyMealPlan, "family"→FamilyMealPlan, "self"→SelfMealPlan, "weekly"→WeeklyMealPlan, "invalid"→null|
|[RecipeFactory.java](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html)|[createRecipe(int,...)](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html) [RecipeFactory.java:17-31](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html)|All 7 categories + default case|
|[AuditService.java](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html)|[getInstance()](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html), [log()](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html), [logException()](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html)|Singleton pattern test, file writing, exception formatting|
|[MealPlan.java](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html)|[generateGroceryList()](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html) [MealPlan.java:23-28](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html)|Empty recipes, one recipe, multiple recipes with overlapping ingredients|

---

## 🎯 REQUIREMENT 2: Comparative Study of 2 Testing Frameworks

**Recommendation: JUnit 5 vs TestNG**

Test the **same class** ([UnitConverter](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html) or [GroceryList](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html)) with both frameworks and compare:

|Aspect|JUnit 5|TestNG|
|---|---|---|
|Parameterized tests|`@ParameterizedTest` + `@CsvSource`/`@MethodSource`|`@DataProvider`|
|Test grouping|`@Tag`|`@Test(groups=...)`|
|Exception testing|`assertThrows()`|`@Test(expectedExceptions=...)`|
|Lifecycle|`@BeforeEach`/`@AfterEach`|`@BeforeMethod`/`@AfterMethod`|
|Assertions|`Assertions.assertEquals()`|`Assert.assertEquals()` (param order reversed!)|
|Dependency between tests|Not natively supported|`@Test(dependsOnMethods=...)`|
|Parallel execution|via config|`@Test(threadPoolSize=...)`|
|Suite definition|`@Suite` (JUnit Platform)|XML suite files|

Good classes to showcase differences:

- **[UnitConverter.convert()](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html)** — parameterized tests shine here (many input combinations)
- **[GroceryList.addItem()](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html)** — test dependency (add then verify) showcases TestNG's `dependsOnMethods`
- **[MealPlanFactory.createMealPlan()](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html)** — data-driven with `@DataProvider` vs `@MethodSource`

---

## 🎯 REQUIREMENT 3: Web UI Testing (Selenium)

**Recommended framework: Selenium WebDriver** (motivations: widely adopted, Java-native, supports multiple browsers, integrates with JUnit/TestNG, your project is a Spring Boot web app with Thymeleaf templates).

### Pages & Test Scenarios

#### 1. Login page — [login.html](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html)

- **Form validation**: submit empty form (HTML `required` attribute), submit with only username, submit with only password
- **Functionality**: login with valid credentials → redirect to [/plans/myPlans](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html), login with wrong password → "Wrong password" error, login with non-existent user → "User not found" error
- **Navigation**: click "Register" link → goes to [/user/register](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html)

#### 2. Register page — [register.html](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html)

- **Form validation**: empty fields, duplicate username → "User already exists" error
- **Functionality**: successful registration → "User has been logged in" message
- **Navigation**: click "Login" link → goes to [/user/login](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html)

#### 3. Recipe listing — [index.html](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html)

- **Functionality**: page loads with recipe list, click on recipe → navigates to detail page
- **Navigation**: accessible without login (no auth check in [RecipeController](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html))

#### 4. Recipe detail — [recipe-detail.html](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html)

- **Functionality**: displays recipe name, ingredients, details. Non-existent ID → redirects to index

#### 5. Meal Plans — [myPlans.html](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html), [create.html](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html)

- **Auth check**: accessing `/plans` without login → redirected to login
- **Functionality**: create daily/weekly/family/self plan, see them in "My Plans"
- **Form validation**: plan creation form inputs

#### 6. Admin panel — [panel.html](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html)

- **Authorization**: non-admin user → "You are not admin" error
- **Functionality**: user list displayed, update user, delete user

#### 7. Cross-browser

- Run same tests on Chrome and Firefox (at minimum)

---

## 👥 Suggested Work Distribution (5 Members)

|Member|Responsibility|
|---|---|
|**Member 1**|Unit tests for [UnitConverter](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html) — full strategy demo (equivalence partitioning, BVA, statement/decision/condition coverage, independent circuits). This is the **showcase class**. Also run PIT mutation testing and write 2 extra tests to kill surviving mutants|
|**Member 2**|Unit tests for [GroceryList](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html) ([addItem](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html), [addLists](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html), [sortedGroceryList](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html)) + [Unit.isCompatibleWith()](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html) + [Ingredient.equals()](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html). Focus on decision/condition coverage and equivalence partitioning|
|**Member 3**|Unit tests for [Recipe](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html) ([setDuration](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html), [formatDuration](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html)) + [RecipeFactory.createRecipe()](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html) + [MealPlanFactory.createMealPlan()](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html) + [PasswordUtils.hashPassword()](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html) + [User.equals()](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html). Focus on boundary value analysis and statement coverage|
|**Member 4**|**Framework comparison**: rewrite Member 1 or 2's tests in both **JUnit 5** and **TestNG**, do the comparative analysis with code examples showing differences in annotations, parameterized tests, assertions, etc.|
|**Member 5**|**Selenium WebDriver UI tests**: login/register flows, recipe browsing, meal plan creation, admin panel authorization, form validation, navigation, cross-browser (Chrome + Firefox)|

---

## 💡 Key Notes

- **[UnitConverter](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html) is package-private** (no `public` modifier) — your tests will need to be in the same package ([com.bitbites.bitbites2.backend.groceries](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html)) or you can test it indirectly through [GroceryList.addItem()](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html) which calls it internally.
- All domain classes ([UnitConverter](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html), [GroceryList](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html), [Recipe](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html), [Unit](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html), [Ingredient](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html), [User](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html), [PasswordUtils](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html), factories) are **pure logic with no DB dependency** — perfect for unit testing without mocking.
- For **mutation testing**, use [PIT (pitest)](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html) — add the Gradle plugin and run it against [UnitConverter](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html) and [GroceryList](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html).
- For **Selenium**, you'll need the app running locally (e.g., `./gradlew bootRun`) and a test database seeded with data.