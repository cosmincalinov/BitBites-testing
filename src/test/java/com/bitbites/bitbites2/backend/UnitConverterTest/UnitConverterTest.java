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

}
