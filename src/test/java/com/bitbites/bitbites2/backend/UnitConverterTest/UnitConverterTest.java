package com.bitbites.bitbites2.backend.UnitConverterTest;

import com.bitbites.bitbites2.backend.groceries.Unit;
import com.bitbites.bitbites2.backend.groceries.UnitConverter;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UnitConverterTest {

    // ------------------
    // convert()
    // ------------------
    // a) Partitionare pe clase de echivalenta
    /*
        C1: Conversie identitate -> returneaza aceeasi cantitate
        C2: Unitati din aceeasi categorie (e.g. Gram - Kilogram) -> calcul simplu
        C3: Unitati din categorii diferite (e.g. Cup - Gram) -> conversie
        C4: Unitati incompatibile -> IllegalArgumentException
     */

    @Test
    public void testConvertSameUnit() { // C1
        double result = UnitConverter.convert(150, Unit.GRAM, Unit.GRAM);
        assertEquals(150, result, 0.001);
    }

    @Test
    public void testConverterSameCategory() { // C2
        double result = UnitConverter.convert(2500, Unit.GRAM, Unit.KILOGRAM);
        assertEquals(2.5, result, 0.001);
    }

    @Test
    public void testConverterDiffCategories() { // C3
        double result = UnitConverter.convert(200, Unit.GRAM, Unit.CUP);
        assertEquals(1.0, result, 0.001);
    }

    @Test
    public void testConverterIncompatible() { // C4
        assertThrows(IllegalArgumentException.class, () -> {
            UnitConverter.convert(67, Unit.PIECE, Unit.LITER);
        });
    }

    // b) Boundary Values
    /*
        T1: Cantitate nula
        T2: Cantitate negativa
        T3: Cantitate foarte mica
        T4: Cantitate mare
     */

    @Test
    public void TestConverterZero() { // T1
        double res = UnitConverter.convert(0, Unit.LITER, Unit.MILLILITER);
        assertEquals(0, res, 0.001);
    }

    @Test
    public void TestConverterNegative() { // T2
        assertThrows(IllegalArgumentException.class, () -> {
            UnitConverter.convert(-1, Unit.KILOGRAM, Unit.SPOON);
        });
    }

    @Test
    public void TestConverterSmall() { // T3
        double res = UnitConverter.convert(0.000001, Unit.KILOGRAM, Unit.GRAM);
        assertEquals(0.001, res, 0.0000001);
    }

    @Test
    public void TestConverterBig() { // T4
        double res = UnitConverter.convert(1000000, Unit.GRAM, Unit.KILOGRAM);
        assertEquals(1000, res, 0.001);
    }

    // c) Partitionare pe categorii
    /*
        Variabile: quantity forUnit, toUnit
        Categorii:
            - forUnit: masa (GRAM, KILOGRAM), volum (LITER, MILILITER), bucatarie (CUP, SPOON),
            unitar (PIECE)
           - toUnit: Compatibil, Incompatibil
           - quantity: pozitiv, negativ
     */

    @Test
    public void TestConverterCat1() {
        // Volum -> Bucatarie
        double res = UnitConverter.convert(2, Unit.LITER, Unit.CUP);
        assertEquals(10, res, 0.001);
    }

    @Test
    public void TestConverterCat2() {
        // Bucatarie -> masa
        double res = UnitConverter.convert(10, Unit.SPOON, Unit.GRAM);
        assertEquals(200, res, 0.001);
    }

    // Celelalte teste au fost acoperite in grupele a) si b)

}
