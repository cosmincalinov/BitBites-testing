package com.bitbites.bitbites2.backend.groceries;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 tests for UnitConverter.
 *
 * Strategies used:
 * - Equivalence class partitioning (mass↔mass, volume↔volume, count↔any, incompatible)
 * - Boundary value analysis (0, very small, very large quantities)
 * - Statement & decision coverage (all switch branches)
 */
class UnitConverterTest {

    // ===================== IDENTITY CONVERSIONS =====================

    @Test
    @DisplayName("GRAM → GRAM identity")
    void gramToGram() {
        assertEquals(100.0, UnitConverter.convert(100, Unit.GRAM, Unit.GRAM));
    }

    @Test
    @DisplayName("KILOGRAM → KILOGRAM identity")
    void kilogramToKilogram() {
        assertEquals(2.5, UnitConverter.convert(2.5, Unit.KILOGRAM, Unit.KILOGRAM));
    }

    @Test
    @DisplayName("LITER → LITER identity")
    void literToLiter() {
        assertEquals(3.0, UnitConverter.convert(3, Unit.LITER, Unit.LITER));
    }

    @Test
    @DisplayName("MILLILITER → MILLILITER identity")
    void milliliterToMilliliter() {
        assertEquals(500.0, UnitConverter.convert(500, Unit.MILLILITER, Unit.MILLILITER));
    }

    @Test
    @DisplayName("CUP → CUP identity")
    void cupToCup() {
        assertEquals(2.0, UnitConverter.convert(2, Unit.CUP, Unit.CUP));
    }

    @Test
    @DisplayName("SPOON → SPOON identity")
    void spoonToSpoon() {
        assertEquals(5.0, UnitConverter.convert(5, Unit.SPOON, Unit.SPOON));
    }

    @Test
    @DisplayName("PIECE → PIECE identity")
    void pieceToPiece() {
        assertEquals(3.0, UnitConverter.convert(3, Unit.PIECE, Unit.PIECE));
    }

    // ===================== MASS CONVERSIONS =====================

    @Nested
    @DisplayName("Mass conversions")
    class MassConversions {

        @Test
        void gramToKilogram() {
            assertEquals(0.5, UnitConverter.convert(500, Unit.GRAM, Unit.KILOGRAM), 1e-9);
        }

        @Test
        void kilogramToGram() {
            assertEquals(2000.0, UnitConverter.convert(2, Unit.KILOGRAM, Unit.GRAM), 1e-9);
        }

        @Test
        void gramToSpoon() {
            assertEquals(5.0, UnitConverter.convert(100, Unit.GRAM, Unit.SPOON), 1e-9);
        }

        @Test
        void gramToCup() {
            assertEquals(1.0, UnitConverter.convert(200, Unit.GRAM, Unit.CUP), 1e-9);
        }

        @Test
        void kilogramToSpoon() {
            assertEquals(50.0, UnitConverter.convert(1, Unit.KILOGRAM, Unit.SPOON), 1e-9);
        }

        @Test
        void kilogramToCup() {
            assertEquals(5.0, UnitConverter.convert(1, Unit.KILOGRAM, Unit.CUP), 1e-9);
        }
    }

    // ===================== VOLUME CONVERSIONS =====================

    @Nested
    @DisplayName("Volume conversions")
    class VolumeConversions {

        @Test
        void literToMilliliter() {
            assertEquals(1000.0, UnitConverter.convert(1, Unit.LITER, Unit.MILLILITER), 1e-9);
        }

        @Test
        void milliliterToLiter() {
            assertEquals(0.5, UnitConverter.convert(500, Unit.MILLILITER, Unit.LITER), 1e-9);
        }

        @Test
        void literToCup() {
            assertEquals(5.0, UnitConverter.convert(1, Unit.LITER, Unit.CUP), 1e-9);
        }

        @Test
        void literToSpoon() {
            assertEquals(50.0, UnitConverter.convert(1, Unit.LITER, Unit.SPOON), 1e-9);
        }

        @Test
        void milliliterToCup() {
            assertEquals(1.0, UnitConverter.convert(200, Unit.MILLILITER, Unit.CUP), 1e-9);
        }

        @Test
        void milliliterToSpoon() {
            assertEquals(5.0, UnitConverter.convert(100, Unit.MILLILITER, Unit.SPOON), 1e-9);
        }
    }

    // ===================== COUNT (CUP/SPOON) CONVERSIONS =====================

    @Nested
    @DisplayName("Cup and Spoon cross-conversions")
    class CountConversions {

        @Test
        void cupToGram() {
            assertEquals(200.0, UnitConverter.convert(1, Unit.CUP, Unit.GRAM), 1e-9);
        }

        @Test
        void cupToMilliliter() {
            assertEquals(200.0, UnitConverter.convert(1, Unit.CUP, Unit.MILLILITER), 1e-9);
        }

        @Test
        void cupToKilogram() {
            assertEquals(0.2, UnitConverter.convert(1, Unit.CUP, Unit.KILOGRAM), 1e-9);
        }

        @Test
        void cupToLiter() {
            assertEquals(0.2, UnitConverter.convert(1, Unit.CUP, Unit.LITER), 1e-9);
        }

        @Test
        void cupToSpoon() {
            assertEquals(10.0, UnitConverter.convert(1, Unit.CUP, Unit.SPOON), 1e-9);
        }

        @Test
        void spoonToGram() {
            assertEquals(20.0, UnitConverter.convert(1, Unit.SPOON, Unit.GRAM), 1e-9);
        }

        @Test
        void spoonToMilliliter() {
            assertEquals(20.0, UnitConverter.convert(1, Unit.SPOON, Unit.MILLILITER), 1e-9);
        }

        @Test
        void spoonToKilogram() {
            assertEquals(0.02, UnitConverter.convert(1, Unit.SPOON, Unit.KILOGRAM), 1e-9);
        }

        @Test
        void spoonToLiter() {
            assertEquals(0.02, UnitConverter.convert(1, Unit.SPOON, Unit.LITER), 1e-9);
        }

        @Test
        void spoonToCup() {
            assertEquals(0.5, UnitConverter.convert(5, Unit.SPOON, Unit.CUP), 1e-9);
        }
    }

    // ===================== PIECE INCOMPATIBILITY =====================

    @Nested
    @DisplayName("PIECE incompatible conversions")
    class PieceIncompatible {

        @Test
        void pieceToGramThrows() {
            assertThrows(IllegalArgumentException.class,
                    () -> UnitConverter.convert(1, Unit.PIECE, Unit.GRAM));
        }

        @Test
        void pieceToLiterThrows() {
            assertThrows(IllegalArgumentException.class,
                    () -> UnitConverter.convert(1, Unit.PIECE, Unit.LITER));
        }

        @Test
        void gramToPieceThrows() {
            assertThrows(IllegalArgumentException.class,
                    () -> UnitConverter.convert(1, Unit.GRAM, Unit.PIECE));
        }

        @Test
        void literToPieceThrows() {
            assertThrows(IllegalArgumentException.class,
                    () -> UnitConverter.convert(1, Unit.LITER, Unit.PIECE));
        }

        @Test
        void cupToPieceThrows() {
            assertThrows(IllegalArgumentException.class,
                    () -> UnitConverter.convert(1, Unit.CUP, Unit.PIECE));
        }

        @Test
        void spoonToPieceThrows() {
            assertThrows(IllegalArgumentException.class,
                    () -> UnitConverter.convert(1, Unit.SPOON, Unit.PIECE));
        }

        @Test
        void kilogramToPieceThrows() {
            assertThrows(IllegalArgumentException.class,
                    () -> UnitConverter.convert(1, Unit.KILOGRAM, Unit.PIECE));
        }

        @Test
        void milliliterToPieceThrows() {
            assertThrows(IllegalArgumentException.class,
                    () -> UnitConverter.convert(1, Unit.MILLILITER, Unit.PIECE));
        }
    }

    // ===================== INCOMPATIBLE CROSS-TYPE =====================

    @Test
    @DisplayName("GRAM → LITER throws (mass vs volume)")
    void gramToLiterThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> UnitConverter.convert(100, Unit.GRAM, Unit.LITER));
    }

    @Test
    @DisplayName("GRAM → MILLILITER throws")
    void gramToMilliliterThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> UnitConverter.convert(100, Unit.GRAM, Unit.MILLILITER));
    }

    @Test
    @DisplayName("KILOGRAM → LITER throws")
    void kilogramToLiterThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> UnitConverter.convert(1, Unit.KILOGRAM, Unit.LITER));
    }

    @Test
    @DisplayName("KILOGRAM → MILLILITER throws")
    void kilogramToMilliliterThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> UnitConverter.convert(1, Unit.KILOGRAM, Unit.MILLILITER));
    }

    @Test
    @DisplayName("LITER → GRAM throws")
    void literToGramThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> UnitConverter.convert(1, Unit.LITER, Unit.GRAM));
    }

    @Test
    @DisplayName("LITER → KILOGRAM throws")
    void literToKilogramThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> UnitConverter.convert(1, Unit.LITER, Unit.KILOGRAM));
    }

    @Test
    @DisplayName("MILLILITER → GRAM throws")
    void milliliterToGramThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> UnitConverter.convert(1, Unit.MILLILITER, Unit.GRAM));
    }

    @Test
    @DisplayName("MILLILITER → KILOGRAM throws")
    void milliliterToKilogramThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> UnitConverter.convert(1, Unit.MILLILITER, Unit.KILOGRAM));
    }

    // ===================== BOUNDARY VALUES =====================

    @Nested
    @DisplayName("Boundary value analysis")
    class BoundaryValues {

        @Test
        void zeroQuantity() {
            assertEquals(0.0, UnitConverter.convert(0, Unit.GRAM, Unit.KILOGRAM), 1e-9);
        }

        @Test
        void verySmallQuantity() {
            assertEquals(0.001, UnitConverter.convert(1, Unit.GRAM, Unit.KILOGRAM), 1e-9);
        }

        @Test
        void veryLargeQuantity() {
            assertEquals(1_000_000.0, UnitConverter.convert(1000, Unit.KILOGRAM, Unit.GRAM), 1e-9);
        }

        @Test
        void negativeQuantity() {
            // converter doesn't validate negative, just converts
            assertEquals(-1.0, UnitConverter.convert(-1000, Unit.GRAM, Unit.KILOGRAM), 1e-9);
        }
    }

    // ===================== PARAMETERIZED (roundtrip) =====================

    @ParameterizedTest(name = "{0} {1} → {2} → back = {0}")
    @CsvSource({
            "100, GRAM, KILOGRAM",
            "2, KILOGRAM, GRAM",
            "1, LITER, MILLILITER",
            "500, MILLILITER, LITER",
            "5, CUP, SPOON",
            "10, SPOON, CUP",
    })
    @DisplayName("Roundtrip conversion consistency")
    void roundtripConversion(double qty, Unit from, Unit to) {
        double converted = UnitConverter.convert(qty, from, to);
        double back = UnitConverter.convert(converted, to, from);
        assertEquals(qty, back, 1e-6, "Roundtrip should return original value");
    }
}
