# Tema 3: Testare Unitara în Java - BitBites

**Membri Echipa:**
* Alexandru Norina
* Balan Liviu
* Calinov Cosmin
* Fota Adrian
* Preda Cristian

---

# BitBites

## Prezentare Generala

BitBites este o aplicatie Java pentru planificarea meselor, care genereaza planuri saptamanale de alimentatie si liste de cumparaturi bazate pe retetele selectate. Sistemul este proiectat cu o abordare orientata pe obiecte, folosind mostenire si pattern-ul factory pentru a gestiona eficient planificarea meselor.

## Video si Prezentare
Video de prezentare a aplicatiei: [https://s.go.ro/iv7t6hs7](https://s.go.ro/iv7t6hs7)

Executia testelor: [https://s.go.ro/iv7t6hs7](https://s.go.ro/01vsnz9c)

Prezentare PowerPoint: [Java Unit Testing](presentation/Java%20Unit%20Testing.pptx)

## Functionalitati

- Defineste **ingrediente** si categorizeaza-le.
- Creeaza **retete** cu atribute detaliate (calorii, portii, instructiuni, etc.).
- Genereaza planuri de masa de diferite tipuri (zilnic, saptamanal, familie).
- Creeaza o lista de cumparaturi bazata pe retetele selectate.
- Creeaza o lista de cumparaturi bazata pe un plan de masa generat.
- Foloseste un **pattern de tip factory** pentru a crea retete dintr-un URL.
- **Agregheaza ingrediente** automat si gestioneaza **conversiile de unitati**.
- **Stocheaza planuri de masa** pentru utilizatori.
- Creeaza o **baza de date de utilizatori** cu hash-uirea parolelor stocate.
- Asigura ca planurile de masa indeplinesc **constrangerile calorice**.
- (Bonus) Ofera o **interfata web** pentru gestionarea retetelor, planurilor de masa si listelor de cumparaturi.

---

## Logica Proiectului

Sistemul urmeaza o abordare structurata cu componente interconectate. Logica de baza se concentreaza pe:

- **Ingredients**: Reprezentate prin inregistrari simple, categorizate pentru gestionare mai usoara.
- **Grocery Items**: Definite prin ingredient, cantitate si unitate, permitand agregare si conversie a unitatilor.
- **Recipes**: Reprezentare abstracta a unei mese, categorisita in tipuri precum `Dessert`, `MainCourse`, etc.
- **Recipe Factory**: Initial suporta importul retetelor dintr-un singur site, cu extensibilitate ulterioara pentru mai multe surse.
- **Schedule**: Define structura meselor zilnice, asigurand un plan de masa echilibrat.
- **Meal Plans**:
    - **SelfMealPlan**: Utilizatorul furnizeaza o lista predefinita de retete, iar logica de generare nu este necesara.
    - **Alte planuri de masa (Daily, Weekly, Family)**:
        - Utilizatorul selecteaza tipurile de bucatarie si un interval de timp de preparare.
        - Sistemul alege aleator retete din bucatariile alese, asigurand ca aportul total de calorii este echilibrat.
        - **FamilyMealPlan** extinde planul saptamanal (weekly) prin ajustarea portiilor in functie de numarul de persoane.
        - Sistemul previne mesele duplicate si echilibreaza distributia meselor pe parcursul saptamanii.

### Reprezentare Diagrama

Diagrama urmatoare reprezinta vizual logica de baza a **BitBites**:

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
## Baza de Date

- implementata folosind PostgreSQL
- a fost nevoie sa adaugam cateva clase Model pentru a realiza baza de date
  - colectiile si tablourile din obiecte sunt transformate in chei straine in clasele incluse
  - am adaugat inregistrari pentru a stoca cheile primare (id) si cheile straine
- am adaugat operatii CRUD pe toate tabelele (functii diferite pentru tabele)

### Diagrama
![img.png](img.png)
---

## Interfata Web

Aplicatia web BitBites va furniza urmatoarele functionalitati:

- **Creeaza si stocheaza retete**.
- **Genereaza planuri de masa** de orice tip.
- **Vizualizeaza lista de cumparaturi** pentru planurile de masa selectate.
- **Vezi mesele planificate** pentru saptamana intr-un format de calendar.

---

# Documentatie de Testare si Asigurare a Calitatii (QA)

Aceasta sectiune detaliaza ciclul de viata al testarii software pentru aplicatia BitBites, incluzand configurarea mediului, strategiile de testare unitara backend, automatizarea UI frontend, comparatii intre framework-uri si utilizarea instrumentelor AI in faza de testare.

## 1. Configurarea Mediului de Testare

### Specificatii Hardware si Software
* **Sistem de Operare:** Windows 11 (64-bit)
* **Mediu de Executie:** Calculator local (fara masina virtuala)
* **Java Development Kit:** JDK 21.0.6
* **Baza de Date de Test:** PostgreSQL local (Default port 5432)

### Instrumente si Versiuni
* **Framework Backend:** JUnit 5 (Jupiter API)
* **Automatizare Frontend:** Selenium WebDriver (v4.31.0)
* **Driver Browser Web:** Firefox GeckoDriver (v0.36.0) rulat in mod `--headless`
* **Code Coverage Tool:** IntelliJ IDEA Built-in Coverage Runner
* **Build Tool:** Gradle


![Config](images/gradle.png)

---
