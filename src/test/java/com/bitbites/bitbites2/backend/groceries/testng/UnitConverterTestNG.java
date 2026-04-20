package com.bitbites.bitbites2.backend.groceries.testng;

import com.bitbites.bitbites2.backend.groceries.Unit;
import org.testng.annotations.*;

import java.lang.reflect.Method;

import static org.testng.Assert.*;

/**
 * TestNG tests for UnitConverter (package-private, accessed via reflection).
 *
 * Comparative note vs JUnit:
 * - TestNG uses @DataProvider for parameterized tests (vs JUnit's @ParameterizedTest + @CsvSource)
 * - TestNG uses expectedExceptions attribute (vs JUnit's assertThrows)
 * - TestNG uses groups for categorization (vs JUnit's @Nested)
 * - TestNG uses soft assertions (SoftAssert) for multiple checks
 */
public class UnitConverterTestNG {

    private Method convertMethod;

    @BeforeClass
    public void setUp() throws Exception {
        Class<?> clazz = Class.forName("com.bitbites.bitbites2.backend.groceries.UnitConverter");
        convertMethod = clazz.getDeclaredMethod("convert", double.class, Unit.class, Unit.class);
        convertMethod.setAccessible(true);
    }

    private double convert(double qty, Unit from, Unit to) throws Exception {
        return (double) convertMethod.invoke(null, qty, from, to);
    }

    // ===================== IDENTITY =====================

    @Test(groups = "identity")
    public void gramToGram() throws Exception {
        assertEquals(convert(100, Unit.GRAM, Unit.GRAM), 100.0);
    }

    @Test(groups = "identity")
    public void kilogramToKilogram() throws Exception {
        assertEquals(convert(2.5, Unit.KILOGRAM, Unit.KILOGRAM), 2.5);
    }

    @Test(groups = "identity")
    public void literToLiter() throws Exception {
        assertEquals(convert(3, Unit.LITER, Unit.LITER), 3.0);
    }

    @Test(groups = "identity")
    public void milliliterToMilliliter() throws Exception {
        assertEquals(convert(500, Unit.MILLILITER, Unit.MILLILITER), 500.0);
    }

    @Test(groups = "identity")
    public void cupToCup() throws Exception {
        assertEquals(convert(2, Unit.CUP, Unit.CUP), 2.0);
    }

    @Test(groups = "identity")
    public void spoonToSpoon() throws Exception {
        assertEquals(convert(5, Unit.SPOON, Unit.SPOON), 5.0);
    }

    @Test(groups = "identity")
    public void pieceToPiece() throws Exception {
        assertEquals(convert(3, Unit.PIECE, Unit.PIECE), 3.0);
    }

    // ===================== MASS =====================

    @Test(groups = "mass")
    public void gramToKilogram() throws Exception {
        assertEquals(convert(500, Unit.GRAM, Unit.KILOGRAM), 0.5, 1e-9);
    }

    @Test(groups = "mass")
    public void kilogramToGram() throws Exception {
        assertEquals(convert(2, Unit.KILOGRAM, Unit.GRAM), 2000.0, 1e-9);
    }

    @Test(groups = "mass")
    public void gramToSpoon() throws Exception {
        assertEquals(convert(100, Unit.GRAM, Unit.SPOON), 5.0, 1e-9);
    }

    @Test(groups = "mass")
    public void gramToCup() throws Exception {
        assertEquals(convert(200, Unit.GRAM, Unit.CUP), 1.0, 1e-9);
    }

    @Test(groups = "mass")
    public void kilogramToSpoon() throws Exception {
        assertEquals(convert(1, Unit.KILOGRAM, Unit.SPOON), 50.0, 1e-9);
    }

    @Test(groups = "mass")
    public void kilogramToCup() throws Exception {
        assertEquals(convert(1, Unit.KILOGRAM, Unit.CUP), 5.0, 1e-9);
    }

    // ===================== VOLUME =====================

    @Test(groups = "volume")
    public void literToMilliliter() throws Exception {
        assertEquals(convert(1, Unit.LITER, Unit.MILLILITER), 1000.0, 1e-9);
    }

    @Test(groups = "volume")
    public void milliliterToLiter() throws Exception {
        assertEquals(convert(500, Unit.MILLILITER, Unit.LITER), 0.5, 1e-9);
    }

    @Test(groups = "volume")
    public void literToCup() throws Exception {
        assertEquals(convert(1, Unit.LITER, Unit.CUP), 5.0, 1e-9);
    }

    @Test(groups = "volume")
    public void literToSpoon() throws Exception {
        assertEquals(convert(1, Unit.LITER, Unit.SPOON), 50.0, 1e-9);
    }

    @Test(groups = "volume")
    public void milliliterToCup() throws Exception {
        assertEquals(convert(200, Unit.MILLILITER, Unit.CUP), 1.0, 1e-9);
    }

    @Test(groups = "volume")
    public void milliliterToSpoon() throws Exception {
        assertEquals(convert(100, Unit.MILLILITER, Unit.SPOON), 5.0, 1e-9);
    }

    // ===================== COUNT CROSS-CONVERSIONS =====================

    @Test(groups = "count")
    public void cupToGram() throws Exception {
        assertEquals(convert(1, Unit.CUP, Unit.GRAM), 200.0, 1e-9);
    }

    @Test(groups = "count")
    public void cupToMilliliter() throws Exception {
        assertEquals(convert(1, Unit.CUP, Unit.MILLILITER), 200.0, 1e-9);
    }

    @Test(groups = "count")
    public void cupToKilogram() throws Exception {
        assertEquals(convert(1, Unit.CUP, Unit.KILOGRAM), 0.2, 1e-9);
    }

    @Test(groups = "count")
    public void cupToLiter() throws Exception {
        assertEquals(convert(1, Unit.CUP, Unit.LITER), 0.2, 1e-9);
    }

    @Test(groups = "count")
    public void cupToSpoon() throws Exception {
        assertEquals(convert(1, Unit.CUP, Unit.SPOON), 10.0, 1e-9);
    }

    @Test(groups = "count")
    public void spoonToGram() throws Exception {
        assertEquals(convert(1, Unit.SPOON, Unit.GRAM), 20.0, 1e-9);
    }

    @Test(groups = "count")
    public void spoonToCup() throws Exception {
        assertEquals(convert(5, Unit.SPOON, Unit.CUP), 0.5, 1e-9);
    }

    // ===================== INCOMPATIBLE =====================

    @Test(groups = "incompatible", expectedExceptions = Exception.class)
    public void gramToLiterThrows() throws Exception {
        convert(100, Unit.GRAM, Unit.LITER);
    }

    @Test(groups = "incompatible", expectedExceptions = Exception.class)
    public void literToGramThrows() throws Exception {
        convert(1, Unit.LITER, Unit.GRAM);
    }

    @Test(groups = "incompatible", expectedExceptions = Exception.class)
    public void pieceToGramThrows() throws Exception {
        convert(1, Unit.PIECE, Unit.GRAM);
    }

    @Test(groups = "incompatible", expectedExceptions = Exception.class)
    public void gramToPieceThrows() throws Exception {
        convert(1, Unit.GRAM, Unit.PIECE);
    }

    @Test(groups = "incompatible", expectedExceptions = Exception.class)
    public void kilogramToLiterThrows() throws Exception {
        convert(1, Unit.KILOGRAM, Unit.LITER);
    }

    @Test(groups = "incompatible", expectedExceptions = Exception.class)
    public void milliliterToKilogramThrows() throws Exception {
        convert(1, Unit.MILLILITER, Unit.KILOGRAM);
    }

    // ===================== BOUNDARY =====================

    @Test(groups = "boundary")
    public void zeroQuantity() throws Exception {
        assertEquals(convert(0, Unit.GRAM, Unit.KILOGRAM), 0.0, 1e-9);
    }

    @Test(groups = "boundary")
    public void negativeQuantity() throws Exception {
        assertEquals(convert(-1000, Unit.GRAM, Unit.KILOGRAM), -1.0, 1e-9);
    }

    @Test(groups = "boundary")
    public void veryLargeQuantity() throws Exception {
        assertEquals(convert(1000, Unit.KILOGRAM, Unit.GRAM), 1_000_000.0, 1e-9);
    }

    // ===================== DATAPROVIDER (roundtrip) =====================

    @DataProvider(name = "roundtripData")
    public Object[][] roundtripData() {
        return new Object[][]{
                {100.0, Unit.GRAM, Unit.KILOGRAM},
                {2.0, Unit.KILOGRAM, Unit.GRAM},
                {1.0, Unit.LITER, Unit.MILLILITER},
                {500.0, Unit.MILLILITER, Unit.LITER},
                {5.0, Unit.CUP, Unit.SPOON},
                {10.0, Unit.SPOON, Unit.CUP},
        };
    }

    @Test(dataProvider = "roundtripData", groups = "roundtrip")
    public void roundtripConversion(double qty, Unit from, Unit to) throws Exception {
        double converted = convert(qty, from, to);
        double back = convert(converted, to, from);
        assertEquals(back, qty, 1e-6, "Roundtrip should return original value");
    }
}
