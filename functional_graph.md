# Functional Graph for `createCustomRecipe`

## Control Flow Graph (CFG)

The nodes represent linear code blocks; edges represent jumps (branches, returns, throws).

```mermaid
flowchart TD
    START(["START: entry"]) --> N1{"name == null || blank?"}

    N1 -- "T" --> T1["throw IAE: name null/blank"]
    N1 -- "F" --> N2["name = name.trim()"]
    N2 --> N3{"name.length() > 100?"}

    N3 -- "T" --> T2["throw IAE: name too long"]
    N3 -- "F" --> N4{"category == null || blank?"}

    N4 -- "T" --> T3["throw IAE: category null/blank"]
    N4 -- "F" --> N5["trim + capitalise category"]

    N5 --> N6{"alias switch"}
    N6 -- "Starter/Appetiser" --> A1["category = Appetizer"]
    N6 -- "Main/Maincourse/Entree" --> A2["category = MainCourse"]
    N6 -- "Beverage" --> A3["category = Drink"]
    N6 -- "default" --> A4["category unchanged"]

    A1 --> N7{"VALID_CATEGORIES.contains?"}
    A2 --> N7
    A3 --> N7
    A4 --> N7

    N7 -- "F" --> T4["throw IAE: invalid category"]
    N7 -- "T" --> N8{"kitchenType == null || blank?"}

    N8 -- "T" --> T5["throw IAE: kitchen null/blank"]
    N8 -- "F" --> N9["kitchenType = trim"]

    N9 --> N10{"kcal <= 0?"}
    N10 -- "T" --> T6["throw IAE: kcal <= 0"]
    N10 -- "F" --> N11{"kcal > 5000?"}

    N11 -- "T" --> T7["throw IAE: kcal > 5000"]
    N11 -- "F" --> N12{"servings < 1?"}

    N12 -- "T" --> T8["throw IAE: servings < 1"]
    N12 -- "F" --> N13{"servings > 50?"}

    N13 -- "T" --> T9["throw IAE: servings > 50"]
    N13 -- "F" --> N14{"kcal adjustment"}

    N14 -- "Drink" --> ADJ1["kcal *= 0.8"]
    N14 -- "Salad" --> ADJ2["kcal *= 0.9"]
    N14 -- "Dessert" --> ADJ3["kcal *= 1.15"]
    N14 -- "else" --> ADJ4["kcal unchanged"]

    ADJ3 --> N15{"adjustedKcal > 5000?"}
    N15 -- "T" --> CAP["kcal = 5000"]
    N15 -- "F" --> CREATE

    ADJ1 --> CREATE
    ADJ2 --> CREATE
    ADJ4 --> CREATE
    CAP --> CREATE

    CREATE["switch: create subclass"] --> DUR["switch: set duration"]
    DUR --> RET(["RETURN recipe"])
```

## Node & Edge Count

| Metric | Value |
|--------|-------|
| Nodes (N) | 28 |
| Edges (E) | 33 |
| Connected components (P) | 1 |
| **Cyclomatic Complexity** V(G) = E − N + 2P | **33 − 28 + 2 = 7** |

> Note: The switch statements with 7+1 arms are counted as single decision nodes
> for the high-level CFG. Expanding each switch arm would give a higher V(G).

## LCSAJ Table

| # | Start Node | Linear Sequence | Jump To |
|---|-----------|-----------------|---------|
| 1 | START | N1(T) | T1 (throw) |
| 2 | START | N1(F)→N2→N3(T) | T2 (throw) |
| 3 | START | N1(F)→N2→N3(F)→N4(T) | T3 (throw) |
| 4 | START | …→N4(F)→N5→N6→A*→N7(F) | T4 (throw) |
| 5 | START | …→N7(T)→N8(T) | T5 (throw) |
| 6 | START | …→N8(F)→N9→N10(T) | T6 (throw) |
| 7 | START | …→N10(F)→N11(T) | T7 (throw) |
| 8 | START | …→N11(F)→N12(T) | T8 (throw) |
| 9 | START | …→N12(F)→N13(T) | T9 (throw) |
| 10 | START | …→N13(F)→N14→ADJ1→CREATE→DUR | RET |
| 11 | START | …→N13(F)→N14→ADJ2→CREATE→DUR | RET |
| 12 | START | …→N13(F)→N14→ADJ3→N15(F)→CREATE→DUR | RET |
| 13 | START | …→N13(F)→N14→ADJ3→N15(T)→CAP→CREATE→DUR | RET |
| 14 | START | …→N13(F)→N14→ADJ4→CREATE→DUR | RET |

## Expanded Creation/Duration Switch Paths

Each of the 7 valid categories goes through its own arm in both the creation
switch and the duration switch, yielding 7 distinct end-to-end happy paths
(× the 4 kcal-adjustment paths = 10 distinct LCSAJs since Drink/Salad/Dessert
have fixed category-kcal pairings).
