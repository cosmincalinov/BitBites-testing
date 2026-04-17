package com.bitbites.bitbites2.backend.groceries;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GroceryListTest {

    // ====================================================================
    //  addItem()
    // ====================================================================

    // a) Partitionare de echivalenta
    /*
     * Partitii:
     * P1: ingredientul NU exista in lista -> se adauga direct
     * P2: ingredientul EXISTA, unitati compatibile -> se face merge (cantitati cumulate)
     * P3: ingredientul EXISTA, unitati incompatibile -> noul item il inlocuieste pe cel vechi
     */

    @Test
    public void testAddItem_IngredientNou() {
        // P1
        GroceryList list = new GroceryList();
        Ingredient faina = new Ingredient("faina", "cereale");
        list.addItem(new GroceryItem(500, Unit.GRAM, faina));

        assertEquals(1, list.getItems().size());
        assertEquals(500, list.getItems().get(faina).quantity(), 0.001);
        assertEquals(Unit.GRAM, list.getItems().get(faina).unit());
    }

    @Test
    public void testAddItem_IngredientExistent_UnitatiCompatibile() {
        // P2: KILOGRAM -> convertit in GRAM si adaugat
        GroceryList list = new GroceryList();
        Ingredient zahar = new Ingredient("zahar", "indulcitor");
        list.addItem(new GroceryItem(200, Unit.GRAM, zahar));
        list.addItem(new GroceryItem(0.3, Unit.KILOGRAM, zahar)); // 0.3 kg = 300 g

        // 200g + 300g = 500g
        assertEquals(500, list.getItems().get(zahar).quantity(), 0.001);
        assertEquals(Unit.GRAM, list.getItems().get(zahar).unit());
    }

    @Test
    public void testAddItem_IngredientExistent_UnitatiIncompatibile() {
        // P3: masa (GRAM) vs volum (LITER) -> incompatibile -> noul item inlocuieste
        GroceryList list = new GroceryList();
        Ingredient lapte = new Ingredient("lapte", "lactate");
        list.addItem(new GroceryItem(500, Unit.GRAM, lapte));
        list.addItem(new GroceryItem(1, Unit.LITER, lapte));

        assertEquals(1, list.getItems().size());
        assertEquals(Unit.LITER, list.getItems().get(lapte).unit());
        assertEquals(1, list.getItems().get(lapte).quantity(), 0.001);
    }

    // b) Boundary Value Analysis

    @Test
    public void testAddItem_ListaGoala_PrimulElement() {
        // Limita: marimea listei trece de la 0 la 1
        GroceryList list = new GroceryList();
        assertTrue(list.getItems().isEmpty());
        list.addItem(new GroceryItem(10, Unit.GRAM, new Ingredient("sare", "condiment")));
        assertEquals(1, list.getItems().size());
    }

    @Test
    public void testAddItem_CantitateZero() {
        // Limita: cantitate = 0
        GroceryList list = new GroceryList();
        Ingredient piper = new Ingredient("piper", "condiment");
        list.addItem(new GroceryItem(0, Unit.GRAM, piper));
        assertEquals(0, list.getItems().get(piper).quantity(), 0.001);
    }

    @Test
    public void testAddItem_SumaExacta_DupaConversie() {
        // Verificare suma exacta: 1L + 500ml = 1.5L
        GroceryList list = new GroceryList();
        Ingredient apa = new Ingredient("apa", "lichid");
        list.addItem(new GroceryItem(1, Unit.LITER, apa));
        list.addItem(new GroceryItem(500, Unit.MILLILITER, apa));
        assertEquals(1.5, list.getItems().get(apa).quantity(), 0.001);
        assertEquals(Unit.LITER, list.getItems().get(apa).unit());
    }

    // c) Category Partitioning
    /*
     * Parametri: newItem
     * - ingredient: nou / existent
     * - unitati: aceeasi unitate / unitate diferita compatibila / unitate incompatibila
     * - cantitate: pozitiva
     */

    @Test
    public void testAddItem_Category_AceeaziUnitate() {
        GroceryList list = new GroceryList();
        Ingredient oua = new Ingredient("oua", "proteina");
        list.addItem(new GroceryItem(3, Unit.PIECE, oua));
        list.addItem(new GroceryItem(2, Unit.PIECE, oua));
        assertEquals(5, list.getItems().get(oua).quantity(), 0.001);
        assertEquals(Unit.PIECE, list.getItems().get(oua).unit());
    }

    @Test
    public void testAddItem_Category_UnitatiDiferiteCompatibile() {
        // CUP -> SPOON: ambele sunt "count" si convertibile
        GroceryList list = new GroceryList();
        Ingredient otet = new Ingredient("otet", "condiment");
        list.addItem(new GroceryItem(1, Unit.CUP, otet));   // 1 CUP
        list.addItem(new GroceryItem(5, Unit.SPOON, otet)); // 5 SPOON = 0.5 CUP
        // 5 SPOON -> CUP = 5/10 = 0.5; total = 0.5 + 1 = 1.5 CUP
        assertEquals(1.5, list.getItems().get(otet).quantity(), 0.001);
        assertEquals(Unit.CUP, list.getItems().get(otet).unit());
    }

    @Test
    public void testAddItem_Category_UnitatiIncompatibile_Inlocuire() {
        GroceryList list = new GroceryList();
        Ingredient branza = new Ingredient("branza", "lactate");
        list.addItem(new GroceryItem(200, Unit.GRAM, branza));
        list.addItem(new GroceryItem(2, Unit.LITER, branza)); // GRAM vs LITER incompatibile
        assertEquals(Unit.LITER, list.getItems().get(branza).unit());
        assertEquals(2, list.getItems().get(branza).quantity(), 0.001);
    }

    // Statement Coverage
    @Test
    public void TestStmt_addItem() {
        GroceryList list = new GroceryList();
        Ingredient ulei = new Ingredient("ulei", "grasime");

        // Ramura 1: ingredient nou -> put direct
        list.addItem(new GroceryItem(100, Unit.MILLILITER, ulei));
        assertEquals(100, list.getItems().get(ulei).quantity(), 0.001);

        // Ramura 2: ingredient existent, unitati compatibile -> merge
        list.addItem(new GroceryItem(0.1, Unit.LITER, ulei)); // 100ml -> total 200ml
        assertEquals(200, list.getItems().get(ulei).quantity(), 0.001);

        // Ramura 3: ingredient existent, unitati incompatibile -> catch -> inlocuieste
        Ingredient cascaval = new Ingredient("cascaval", "lactate");
        list.addItem(new GroceryItem(300, Unit.GRAM, cascaval));
        list.addItem(new GroceryItem(0.5, Unit.LITER, cascaval));
        assertEquals(Unit.LITER, list.getItems().get(cascaval).unit());
    }

    // Decision Coverage
    @Test
    public void TestDec_addItem() {
        GroceryList list = new GroceryList();
        Ingredient rosie = new Ingredient("rosie", "leguma");

        // Decizia 1: !containsKey -> true (ingredientul nu e in lista)
        list.addItem(new GroceryItem(3, Unit.PIECE, rosie));
        assertEquals(3, list.getItems().get(rosie).quantity(), 0.001);

        // Decizia 1: !containsKey -> false; Decizia 2 (compatibil) -> true (merge reusit)
        list.addItem(new GroceryItem(2, Unit.PIECE, rosie));
        assertEquals(5, list.getItems().get(rosie).quantity(), 0.001);

        // Decizia 1: !containsKey -> false; Decizia 2 (compatibil) -> false (exceptie -> replace)
        Ingredient unt = new Ingredient("unt", "grasime");
        list.addItem(new GroceryItem(100, Unit.GRAM, unt));
        list.addItem(new GroceryItem(0.5, Unit.LITER, unt)); // GRAM vs LITER
        assertEquals(Unit.LITER, list.getItems().get(unt).unit());
    }

    // Cyclomatic Complexity pentru addItem()
    // n = 6 noduri, e = 7 arce => V(G) = e - n + 2 = 7 - 6 + 2 = 3
    // C1: !containsKey -> true -> put nou -> End
    // C2: !containsKey -> false -> try: convert reusit -> put mergedItem -> End
    // C3: !containsKey -> false -> try: convert arunca exceptie -> catch -> put nou -> End
    @Test
    public void TestCirc_addItem() {
        GroceryList list = new GroceryList();
        Ingredient paste = new Ingredient("paste", "cereale");

        // C1: ingredient nu e in lista
        list.addItem(new GroceryItem(200, Unit.GRAM, paste));
        assertEquals(200, list.getItems().get(paste).quantity(), 0.001);

        // C2: ingredient existent, conversie reusita -> merge
        list.addItem(new GroceryItem(300, Unit.GRAM, paste));
        assertEquals(500, list.getItems().get(paste).quantity(), 0.001);

        // C3: ingredient existent, conversie esueaza -> catch -> inlocuire
        list.addItem(new GroceryItem(1, Unit.LITER, paste)); // GRAM vs LITER
        assertEquals(Unit.LITER, list.getItems().get(paste).unit());
        assertEquals(1, list.getItems().get(paste).quantity(), 0.001);
    }


    // ====================================================================
    //  addLists()
    // ====================================================================

    // a) Partitionare de echivalenta
    /*
     * P1: ambele liste goale -> rezultat gol
     * P2: o lista goala, una plina -> rezultat = lista plina
     * P3: ambele pline, fara ingrediente comune -> toate elementele in rezultat
     * P4: ambele pline, ingredient comun cu unitati compatibile -> merge cantitati
     * P5: ambele pline, ingredient comun cu unitati incompatibile -> inlocuire
     */

    @Test
    public void testAddLists_AmbeleListe_Goale() {
        // P1
        GroceryList result = GroceryList.addLists(new GroceryList(), new GroceryList());
        assertTrue(result.getItems().isEmpty());
    }

    @Test
    public void testAddLists_OListaGoala() {
        // P2
        GroceryList l1 = new GroceryList();
        GroceryList l2 = new GroceryList();
        Ingredient mar = new Ingredient("mar", "fruct");
        l2.addItem(new GroceryItem(3, Unit.PIECE, mar));

        GroceryList result = GroceryList.addLists(l1, l2);
        assertEquals(1, result.getItems().size());
        assertEquals(3, result.getItems().get(mar).quantity(), 0.001);
    }

    @Test
    public void testAddLists_FaraIngredieteComune() {
        // P3
        GroceryList l1 = new GroceryList();
        GroceryList l2 = new GroceryList();
        Ingredient mar = new Ingredient("mar", "fruct");
        Ingredient banana = new Ingredient("banana", "fruct");
        l1.addItem(new GroceryItem(2, Unit.PIECE, mar));
        l2.addItem(new GroceryItem(3, Unit.PIECE, banana));

        GroceryList result = GroceryList.addLists(l1, l2);
        assertEquals(2, result.getItems().size());
        assertEquals(2, result.getItems().get(mar).quantity(), 0.001);
        assertEquals(3, result.getItems().get(banana).quantity(), 0.001);
    }

    @Test
    public void testAddLists_IngredientComun_UnitatiCompatibile() {
        // P4: faina in grame + kilograme -> merge in grame
        GroceryList l1 = new GroceryList();
        GroceryList l2 = new GroceryList();
        Ingredient faina = new Ingredient("faina", "cereale");
        l1.addItem(new GroceryItem(500, Unit.GRAM, faina));
        l2.addItem(new GroceryItem(0.5, Unit.KILOGRAM, faina)); // 0.5 kg = 500g

        GroceryList result = GroceryList.addLists(l1, l2);
        assertEquals(1, result.getItems().size());
        assertEquals(1000, result.getItems().get(faina).quantity(), 0.001);
        assertEquals(Unit.GRAM, result.getItems().get(faina).unit());
    }

    @Test
    public void testAddLists_IngredientComun_UnitatiIncompatibile() {
        // P5: GRAM (masa) vs LITER (volum) -> incompatibile -> inlocuire
        GroceryList l1 = new GroceryList();
        GroceryList l2 = new GroceryList();
        Ingredient lapte = new Ingredient("lapte", "lactate");
        l1.addItem(new GroceryItem(200, Unit.GRAM, lapte));
        l2.addItem(new GroceryItem(1, Unit.LITER, lapte));

        GroceryList result = GroceryList.addLists(l1, l2);
        assertEquals(1, result.getItems().size());
        assertEquals(Unit.LITER, result.getItems().get(lapte).unit());
    }

    // b) Boundary Value Analysis

    @Test
    public void testAddLists_UnElementFiecare_AcelasiIngredient() {
        // Minim: 1 item in fiecare, acelasi ingredient
        GroceryList l1 = new GroceryList();
        GroceryList l2 = new GroceryList();
        Ingredient zahar = new Ingredient("zahar", "indulcitor");
        l1.addItem(new GroceryItem(100, Unit.GRAM, zahar));
        l2.addItem(new GroceryItem(100, Unit.GRAM, zahar));

        GroceryList result = GroceryList.addLists(l1, l2);
        assertEquals(200, result.getItems().get(zahar).quantity(), 0.001);
    }

    @Test
    public void testAddLists_UnElementFiecare_IngredienteDiferite() {
        GroceryList l1 = new GroceryList();
        GroceryList l2 = new GroceryList();
        Ingredient sare = new Ingredient("sare", "condiment");
        Ingredient piper = new Ingredient("piper", "condiment");
        l1.addItem(new GroceryItem(10, Unit.GRAM, sare));
        l2.addItem(new GroceryItem(5, Unit.GRAM, piper));

        GroceryList result = GroceryList.addLists(l1, l2);
        assertEquals(2, result.getItems().size());
    }

    // c) Category Partitioning
    /*
     * Parametri: list1, list2
     * - list1: goala / non-goala
     * - list2: goala / non-goala
     * - ingrediente comune: nu / compatibile / incompatibile
     */
    @Test
    public void testAddLists_Category_AmbelePline_MultipleIngrediente() {
        GroceryList l1 = new GroceryList();
        GroceryList l2 = new GroceryList();
        Ingredient orez = new Ingredient("orez", "cereale");
        Ingredient ulei = new Ingredient("ulei", "grasime");
        Ingredient sare = new Ingredient("sare", "condiment");

        l1.addItem(new GroceryItem(1, Unit.KILOGRAM, orez));
        l1.addItem(new GroceryItem(100, Unit.MILLILITER, ulei));
        l2.addItem(new GroceryItem(500, Unit.GRAM, orez)); // compatibil cu l1 (KILOGRAM)
        l2.addItem(new GroceryItem(5, Unit.GRAM, sare));   // ingredient nou

        GroceryList result = GroceryList.addLists(l1, l2);
        // orez: 1kg + 500g -> existedItem = (1, KILOGRAM), convert(500, GRAM, KILOGRAM) = 0.5
        // merged = 0.5 + 1 = 1.5 KILOGRAM
        assertEquals(3, result.getItems().size());
        assertEquals(1.5, result.getItems().get(orez).quantity(), 0.001);
        assertEquals(Unit.KILOGRAM, result.getItems().get(orez).unit());
        assertEquals(5, result.getItems().get(sare).quantity(), 0.001);
    }

    // Statement Coverage
    @Test
    public void TestStmt_addLists() {
        GroceryList l1 = new GroceryList();
        GroceryList l2 = new GroceryList();
        Ingredient paste = new Ingredient("paste", "cereale");

        // Acopera iterarea prin list1 (non-goala)
        l1.addItem(new GroceryItem(200, Unit.GRAM, paste));
        // Acopera iterarea prin list2 (non-goala) cu ingredient comun
        l2.addItem(new GroceryItem(300, Unit.GRAM, paste));

        GroceryList result = GroceryList.addLists(l1, l2);
        assertEquals(500, result.getItems().get(paste).quantity(), 0.001);
    }

    // Decision Coverage
    @Test
    public void TestDec_addLists() {
        // Decizia 1: bucla list1 parcursa (list1 non-goala)
        // Decizia 2: bucla list2 parcursa (list2 non-goala)
        GroceryList l1 = new GroceryList();
        GroceryList l2 = new GroceryList();
        Ingredient ceapa = new Ingredient("ceapa", "leguma");
        Ingredient usturoi = new Ingredient("usturoi", "leguma");
        l1.addItem(new GroceryItem(2, Unit.PIECE, ceapa));
        l2.addItem(new GroceryItem(3, Unit.PIECE, usturoi));

        GroceryList result1 = GroceryList.addLists(l1, l2);
        assertEquals(2, result1.getItems().size());

        // Decizia 1: bucla list1 sarita (list1 goala)
        // Decizia 2: bucla list2 parcursa
        GroceryList result2 = GroceryList.addLists(new GroceryList(), l2);
        assertEquals(1, result2.getItems().size());
    }

    // Cyclomatic Complexity pentru addLists()
    // n = 7 noduri, e = 8 arce => V(G) = 8 - 7 + 2 = 3
    // C1: list1 goala, list2 goala -> return lista goala
    // C2: list1 non-goala, list2 goala -> parcurge doar list1
    // C3: list1 non-goala, list2 non-goala -> parcurge ambele
    @Test
    public void TestCirc_addLists() {
        Ingredient morcov = new Ingredient("morcov", "leguma");
        Ingredient cartof = new Ingredient("cartof", "leguma");

        // C1: ambele goale
        GroceryList r1 = GroceryList.addLists(new GroceryList(), new GroceryList());
        assertTrue(r1.getItems().isEmpty());

        // C2: list1 non-goala, list2 goala
        GroceryList l1 = new GroceryList();
        l1.addItem(new GroceryItem(200, Unit.GRAM, morcov));
        GroceryList r2 = GroceryList.addLists(l1, new GroceryList());
        assertEquals(1, r2.getItems().size());
        assertEquals(200, r2.getItems().get(morcov).quantity(), 0.001);

        // C3: ambele non-goale
        GroceryList l2 = new GroceryList();
        l2.addItem(new GroceryItem(300, Unit.GRAM, cartof));
        GroceryList r3 = GroceryList.addLists(l1, l2);
        assertEquals(2, r3.getItems().size());
        assertEquals(200, r3.getItems().get(morcov).quantity(), 0.001);
        assertEquals(300, r3.getItems().get(cartof).quantity(), 0.001);
    }


    // ====================================================================
    //  sortedGroceryList()
    // ====================================================================

    @Test
    public void testSortedGroceryList_OrdinareDescrescatoareDupaGrame() {
        GroceryList list = new GroceryList();
        Ingredient a = new Ingredient("a", "cat");
        Ingredient b = new Ingredient("b", "cat");
        Ingredient c = new Ingredient("c", "cat");
        list.addItem(new GroceryItem(100, Unit.GRAM, a));
        list.addItem(new GroceryItem(1, Unit.KILOGRAM, b));  // 1000g
        list.addItem(new GroceryItem(500, Unit.GRAM, c));

        list.sortedGroceryList();
        List<GroceryItem> items = list.getItems().values().stream().toList();

        // b(1000g) > c(500g) > a(100g)
        assertEquals(b, items.get(0).ingredient());
        assertEquals(c, items.get(1).ingredient());
        assertEquals(a, items.get(2).ingredient());
    }

    @Test
    public void testSortedGroceryList_UnitatiInconvertibile_NuArunca() {
        // PIECE -> GRAM nu e posibil; comparatia intoarce 0, ordinea ramane stabila
        GroceryList list = new GroceryList();
        list.addItem(new GroceryItem(5, Unit.PIECE, new Ingredient("x", "cat")));
        list.addItem(new GroceryItem(10, Unit.PIECE, new Ingredient("y", "cat")));

        assertDoesNotThrow(list::sortedGroceryList);
        assertEquals(2, list.getItems().size());
    }
}