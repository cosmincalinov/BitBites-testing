package com.bitbites.bitbites2.backend.groceries.testng;

import com.bitbites.bitbites2.backend.groceries.Unit;
import org.testng.Assert;
import org.testng.annotations.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * TestNG tests for UnitConverter.
 * Since UnitConverter is package-private, we use reflection to access it.
 * Uses DataProvider for parameterized testing and groups for test organization.
 */
public class UnitConverterTestNG {

    private Method convertMethod;

    @BeforeClass
    public void setUpReflection() throws Exception {
        Class<?> converterClass = Class.forName("com.bitbites.bitbites2.backend.groceries.UnitConverter");
        convertMethod = converterClass.getDeclaredMethod("convert", double.class, Unit.class, Unit.class);
        convertMethod.setAccessible(true);
    }

    /**
     * Helper to invoke UnitConverter.convert via reflection.
     */
    private double convert(double quantity, Unit from, Unit to) throws Exception {
        try {
            return (double) convertMethod.invoke(null, quantity, from, to);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof IllegalArgumentException) {
                throw (IllegalArgumentException) e.getCause();
            }
            throw e;
        }
    }

    // ==================== Identity conversions (DataProvider) ====================

    @DataProvider(name = "identityConversions")
    public Object[][] identityConversions() {
        return new Object[][]{
                {100.0, Unit.GRAM},
                {5.0, Unit.KILOGRAM},
                {2.5, Unit.LITER},
                {300.0, Unit.MILLILITER},
                {4.0, Unit.CUP},
                {3.0, Unit.SPOON},
                {10.0, Unit.PIECE},
                {0.0, Unit.GRAM},
        };
    }

    @Test(dataProvider = "identityConversions", groups = {"identity"})
    public void identityConversion_returnsSameQuantity(double quantity, Unit unit) throws Exception {
        Assert.assertEquals(convert(quantity, unit, unit), quantity, 1e-9,
                unit + " -> " + unit + " should return same quantity");
    }

    // ==================== Mass conversions (DataProvider) ====================

    @DataProvider(name = "massConversions")
    public Object[][] massConversions() {
        return new Object[][]{
                {1000.0, Unit.GRAM, Unit.KILOGRAM, 1.0},
                {500.0, Unit.GRAM, Unit.KILOGRAM, 0.5},
                {1.0, Unit.KILOGRAM, Unit.GRAM, 1000.0},
                {2.5, Unit.KILOGRAM, Unit.GRAM, 2500.0},
                {0.0, Unit.GRAM, Unit.KILOGRAM, 0.0},
        };
    }

    @Test(dataProvider = "massConversions", groups = {"mass"})
    public void massConversion_convertsCorrectly(double input, Unit from, Unit to, double expected) throws Exception {
        Assert.assertEquals(convert(input, from, to), expected, 1e-9);
    }

    // ==================== Volume conversions (DataProvider) ====================

    @DataProvider(name = "volumeConversions")
    public Object[][] volumeConversions() {
        return new Object[][]{
                {1.0, Unit.LITER, Unit.MILLILITER, 1000.0},
                {1000.0, Unit.MILLILITER, Unit.LITER, 1.0},
                {500.0, Unit.MILLILITER, Unit.LITER, 0.5},
                {3.5, Unit.LITER, Unit.MILLILITER, 3500.0},
        };
    }

    @Test(dataProvider = "volumeConversions", groups = {"volume"})
    public void volumeConversion_convertsCorrectly(double input, Unit from, Unit to, double expected) throws Exception {
        Assert.assertEquals(convert(input, from, to), expected, 1e-9);
    }

    // ==================== Cup conversions (DataProvider) ====================

    @DataProvider(name = "cupConversions")
    public Object[][] cupConversions() {
        return new Object[][]{
                {1.0, Unit.CUP, Unit.GRAM, 200.0},
                {1.0, Unit.CUP, Unit.MILLILITER, 200.0},
                {5.0, Unit.CUP, Unit.KILOGRAM, 1.0},
                {5.0, Unit.CUP, Unit.LITER, 1.0},
                {1.0, Unit.CUP, Unit.SPOON, 10.0},
                {200.0, Unit.GRAM, Unit.CUP, 1.0},
                {200.0, Unit.MILLILITER, Unit.CUP, 1.0},
                {1.0, Unit.KILOGRAM, Unit.CUP, 5.0},
                {1.0, Unit.LITER, Unit.CUP, 5.0},
        };
    }

    @Test(dataProvider = "cupConversions", groups = {"cup"})
    public void cupConversion_convertsCorrectly(double input, Unit from, Unit to, double expected) throws Exception {
        Assert.assertEquals(convert(input, from, to), expected, 1e-9);
    }

    // ==================== Spoon conversions (DataProvider) ====================

    @DataProvider(name = "spoonConversions")
    public Object[][] spoonConversions() {
        return new Object[][]{
                {1.0, Unit.SPOON, Unit.GRAM, 20.0},
                {1.0, Unit.SPOON, Unit.MILLILITER, 20.0},
                {50.0, Unit.SPOON, Unit.KILOGRAM, 1.0},
                {50.0, Unit.SPOON, Unit.LITER, 1.0},
                {10.0, Unit.SPOON, Unit.CUP, 1.0},
                {1.0, Unit.GRAM, Unit.SPOON, 0.05},
                {1.0, Unit.KILOGRAM, Unit.SPOON, 50.0},
                {1.0, Unit.LITER, Unit.SPOON, 50.0},
        };
    }

    @Test(dataProvider = "spoonConversions", groups = {"spoon"})
    public void spoonConversion_convertsCorrectly(double input, Unit from, Unit to, double expected) throws Exception {
        Assert.assertEquals(convert(input, from, to), expected, 1e-9);
    }

    // ==================== Incompatible conversions ====================

    @DataProvider(name = "incompatibleConversions")
    public Object[][] incompatibleConversions() {
        return new Object[][]{
                {100.0, Unit.GRAM, Unit.LITER},
                {100.0, Unit.GRAM, Unit.MILLILITER},
                {1.0, Unit.KILOGRAM, Unit.LITER},
                {1.0, Unit.KILOGRAM, Unit.MILLILITER},
                {1.0, Unit.LITER, Unit.GRAM},
                {1.0, Unit.LITER, Unit.KILOGRAM},
                {100.0, Unit.MILLILITER, Unit.GRAM},
                {100.0, Unit.MILLILITER, Unit.KILOGRAM},
        };
    }

    @Test(dataProvider = "incompatibleConversions", groups = {"incompatible"},
            expectedExceptions = IllegalArgumentException.class)
    public void incompatibleConversion_throwsException(double quantity, Unit from, Unit to) throws Exception {
        convert(quantity, from, to);
    }

    // ==================== PIECE incompatibility ====================

    @DataProvider(name = "pieceIncompatible")
    public Object[][] pieceIncompatible() {
        return new Object[][]{
                {Unit.GRAM},
                {Unit.KILOGRAM},
                {Unit.LITER},
                {Unit.MILLILITER},
                {Unit.CUP},
                {Unit.SPOON},
        };
    }

    @Test(dataProvider = "pieceIncompatible", groups = {"piece"},
            expectedExceptions = IllegalArgumentException.class)
    public void pieceToOtherUnit_throwsException(Unit toUnit) throws Exception {
        convert(1.0, Unit.PIECE, toUnit);
    }

    @Test(groups = {"piece"})
    public void pieceToPiece_returnsSameQuantity() throws Exception {
        Assert.assertEquals(convert(7.0, Unit.PIECE, Unit.PIECE), 7.0, 1e-9);
    }

    // ==================== Roundtrip conversions ====================

    @DataProvider(name = "roundtripPairs")
    public Object[][] roundtripPairs() {
        return new Object[][]{
                {1500.0, Unit.GRAM, Unit.KILOGRAM},
                {2.5, Unit.LITER, Unit.MILLILITER},
                {3.0, Unit.CUP, Unit.SPOON},
                {5.0, Unit.SPOON, Unit.GRAM},
                {1.0, Unit.KILOGRAM, Unit.CUP},
                {2.0, Unit.LITER, Unit.CUP},
        };
    }

    @Test(dataProvider = "roundtripPairs", groups = {"roundtrip"})
    public void roundtripConversion_returnsOriginal(double original, Unit unitA, Unit unitB) throws Exception {
        double converted = convert(original, unitA, unitB);
        double roundtrip = convert(converted, unitB, unitA);
        Assert.assertEquals(roundtrip, original, 1e-9,
                unitA + " -> " + unitB + " -> " + unitA + " roundtrip failed");
    }

    // ==================== Edge cases ====================

    @Test(groups = {"edge"})
    public void zeroQuantity_convertsToZero() throws Exception {
        Assert.assertEquals(convert(0.0, Unit.GRAM, Unit.KILOGRAM), 0.0, 1e-9);
    }

    @Test(groups = {"edge"})
    public void veryLargeQuantity_convertsCorrectly() throws Exception {
        Assert.assertEquals(convert(1_000_000_000.0, Unit.GRAM, Unit.KILOGRAM),
                1_000_000.0, 1e-6);
    }

    @Test(groups = {"edge"})
    public void verySmallQuantity_convertsCorrectly() throws Exception {
        Assert.assertEquals(convert(1.0, Unit.GRAM, Unit.KILOGRAM), 0.001, 1e-9);
    }

    @Test(groups = {"edge"})
    public void negativeQuantity_convertsCorrectly() throws Exception {
        Assert.assertEquals(convert(-1000.0, Unit.GRAM, Unit.KILOGRAM), -1.0, 1e-9);
    }
}
