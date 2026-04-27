# BitBites – Component Specifications

---

## 1. `RecipeFactory.createCustomRecipe`

**Signature**
```java
Recipe createCustomRecipe(String name, String categoryFood, String kitchenType,
                          double kilocalories, int servings)
```

### Input Domain

| Parameter | Valid | Invalid |
|---|---|---|
| `name` | Non-null, non-blank; trimmed length ∈ [1, 100] | `null`, blank/whitespace-only, trimmed length > 100 |
| `categoryFood` | Non-null, non-blank; case-insensitive match to a valid category or alias | `null`, blank, unrecognised string |
| `kitchenType` | Non-null, non-blank | `null`, blank/whitespace-only |
| `kilocalories` | Real number ∈ (0.0, 5000.0] | ≤ 0.0 or > 5000.0 |
| `servings` | Integer ∈ [1, 50] | < 1 or > 50 |

### Valid Categories

`Appetizer`, `Bread`, `Dessert`, `Drink`, `MainCourse`, `Salad`, `Soup`

### Accepted Aliases (case-insensitive, applied after capitalising first letter)

| Input | Resolves to |
|---|---|
| `Starter`, `Appetiser` | `Appetizer` |
| `Main`, `Maincourse`, `Entree` | `MainCourse` |
| `Beverage` | `Drink` |

### Post-conditions (on valid input)

| Condition | Result |
|---|---|
| `name` has leading/trailing whitespace | Returned recipe `name` is trimmed |
| `categoryFood` is `Drink` | `kilocalories` = input × 0.8 |
| `categoryFood` is `Salad` | `kilocalories` = input × 0.9 |
| `categoryFood` is `Dessert` and input × 1.15 ≤ 5000 | `kilocalories` = input × 1.15 |
| `categoryFood` is `Dessert` and input × 1.15 > 5000 | `kilocalories` = 5000 (capped) |
| Any other category | `kilocalories` = input (unchanged) |

### Auto-assigned Duration

| Category | Duration |
|---|---|
| `Drink` | 10 minutes |
| `Salad` | 15 minutes |
| `Appetizer` | 20 minutes |
| `MainCourse` | 40 minutes |
| `Soup` | 45 minutes |
| `Dessert` | 60 minutes |
| `Bread` | 90 minutes |

### Exceptions

`IllegalArgumentException` is thrown for any invalid parameter value.

---

## 2. `UnitConverter.convert`

**Signature**
```java
double convert(double quantity, Unit forUnit, Unit toUnit) throws IllegalArgumentException
```

### Units and Compatibility Groups

| Unit | Group |
|---|---|
| `GRAM`, `KILOGRAM` | mass |
| `LITER`, `MILLILITER` | volume |
| `CUP`, `SPOON`, `PIECE` | count |

**Compatibility rule:** Units within the same group are convertible to each other. `count` units (`CUP`, `SPOON`) are also inter-convertible with mass and volume units. `PIECE` is only convertible with `PIECE`.

### Conversion Table

| From | To | Factor |
|---|---|---|
| `GRAM` | `KILOGRAM` | ÷ 1000 |
| `GRAM` | `SPOON` | ÷ 20 |
| `GRAM` | `CUP` | ÷ 200 |
| `KILOGRAM` | `GRAM` | × 1000 |
| `KILOGRAM` | `SPOON` | × 50 |
| `KILOGRAM` | `CUP` | × 5 |
| `LITER` | `MILLILITER` | × 1000 |
| `LITER` | `CUP` | × 5 |
| `LITER` | `SPOON` | × 50 |
| `MILLILITER` | `LITER` | ÷ 1000 |
| `MILLILITER` | `CUP` | ÷ 200 |
| `MILLILITER` | `SPOON` | ÷ 20 |
| `CUP` | `GRAM` / `MILLILITER` | × 200 |
| `CUP` | `KILOGRAM` / `LITER` | ÷ 5 |
| `CUP` | `SPOON` | × 10 |
| `SPOON` | `GRAM` / `MILLILITER` | × 20 |
| `SPOON` | `KILOGRAM` / `LITER` | ÷ 50 |
| `SPOON` | `CUP` | ÷ 10 |
| `PIECE` | `PIECE` | × 1 |
| Same unit | Same unit | × 1 (identity) |

### Exceptions

`IllegalArgumentException` is thrown when `forUnit` and `toUnit` are incompatible (e.g. `GRAM` → `LITER`, or `PIECE` → `GRAM`).

---

## 3. `PasswordUtils.hashPassword`

**Signature**
```java
String hashPassword(String password)
```

### Behaviour

| Condition | Result |
|---|---|
| Valid `password` string | Returns a lowercase hexadecimal SHA-256 digest (64 characters) |
| SHA-256 algorithm unavailable | Throws `RuntimeException` wrapping `NoSuchAlgorithmException` |

### Notes
- The same input always produces the same output (deterministic, no salt).
- Output is always a 64-character lowercase hex string.

---

## 4. `MealPlanFactory.createMealPlan`

**Signature**
```java
MealPlan createMealPlan(String type, int members)
```

### Input Domain

| `type` | Returns | Notes on `members` |
|---|---|---|
| `"daily"` | `DailyMealPlan` | `members` is ignored |
| `"weekly"` | `WeeklyMealPlan` | `members` is ignored |
| `"self"` | `SelfMealPlan` | `members` is ignored |
| `"family"` | `FamilyMealPlan(members)` | `members` is forwarded to the constructor |
| Any other value | `null` | — |

---

## 5. `GroceryList`

### `addItem(GroceryItem newItem)`

| Condition | Behaviour |
|---|---|
| Ingredient not yet in list | Item is added as-is |
| Ingredient already in list, units compatible | Quantities are merged: existing item's unit is kept; new quantity is converted and added |
| Ingredient already in list, units incompatible | New item replaces the existing entry |

### `addLists(GroceryList list1, GroceryList list2)`

Returns a new `GroceryList` containing all items from both lists, merging quantities for matching ingredients according to the `addItem` rules above.

### `sortedGroceryList()`

Sorts the internal item map in descending order by quantity (converted to grams for comparison). Items with incompatible units that cannot be converted to grams retain their relative order.

---

## 6. Meal Plan Types — `generatePlan`

All auto-generated plan types (`DailyMealPlan`, `WeeklyMealPlan`, `FamilyMealPlan`) accept three argument arrays:

| Position | Type | Description |
|---|---|---|
| `preferences[0]` | `String[]` | Kitchen types to filter by |
| `preferences[1]` | `Duration[]` (length 1) | Maximum recipe duration (exclusive upper bound) |
| `preferences[2]` | `Recipe[]` | Pool of recipes to select from |

If the argument count is not 2 or 3, or any argument has the wrong type, an error is printed and the method returns without generating a plan.

### DailyMealPlan

Covers **1 day**. Selects randomly (one recipe per slot):

| Meal Slot | Category |
|---|---|
| Breakfast | `Appetizer` |
| Lunch – Soup | `Soup` |
| Lunch – Main Course | `MainCourse` |
| Dinner – Main Course | `MainCourse` |
| Dinner – Dessert | `Dessert` |

### WeeklyMealPlan

Covers **7 days**. Selects recipes until cumulative servings reach the target:

| Meal Slot | Category | Target Servings (total) |
|---|---|---|
| Breakfast | `Appetizer` | ≥ 7 |
| Lunch – Soup | `Soup` | ≥ 7 |
| Lunch – Main Course | `MainCourse` | ≥ 14 |
| Dinner – Main Course | `MainCourse` | (shared with above pool) |
| Dinner – Dessert | `Dessert` | ≥ 7 |

Recipes with `servings` contributing multiple days are reused across the schedule.

### FamilyMealPlan

Covers **7 days**. Same structure as `WeeklyMealPlan`, but:
- Only recipes with `servings >= membersNumber` are included in the pool.
- Serving counts are divided by `membersNumber` when computing how many days a recipe covers.

### SelfMealPlan

`generatePlan(Object[]... args)` accepts a single `Recipe[]` as `args[0]`. The provided recipes are stored as-is; no random selection is performed. There are no daily schedule slots — `getPlan()` simply prints each recipe.
