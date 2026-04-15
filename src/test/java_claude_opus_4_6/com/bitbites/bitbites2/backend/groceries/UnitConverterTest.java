package com.bitbites.bitbites2.backend.groceries;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UnitConverter Tests")
class UnitConverterTest {

    // ==================== Identity conversions ====================

    @Nested
    @DisplayName("Identity conversions (same unit)")
    class IdentityConversions {

        @Test
        @DisplayName("GRAM -> GRAM returns same quantity")
        void gramToGram() {
            assertEquals(100.0, UnitConverter.convert(100.0, Unit.GRAM, Unit.GRAM));
        }

        @Test
        @DisplayName("KILOGRAM -> KILOGRAM returns same quantity")
        void kilogramToKilogram() {
            assertEquals(5.0, UnitConverter.convert(5.0, Unit.KILOGRAM, Unit.KILOGRAM));
        }

        @Test
        @DisplayName("LITER -> LITER returns same quantity")
        void literToLiter() {
            assertEquals(2.5, UnitConverter.convert(2.5, Unit.LITER, Unit.LITER));
        }

        @Test
        @DisplayName("MILLILITER -> MILLILITER returns same quantity")
        void milliliterToMilliliter() {
            assertEquals(300.0, UnitConverter.convert(300.0, Unit.MILLILITER, Unit.MILLILITER));
        }

        @Test
        @DisplayName("CUP -> CUP returns same quantity")
        void cupToCup() {
            assertEquals(4.0, UnitConverter.convert(4.0, Unit.CUP, Unit.CUP));
        }

        @Test
        @DisplayName("SPOON -> SPOON returns same quantity")
        void spoonToSpoon() {
            assertEquals(3.0, UnitConverter.convert(3.0, Unit.SPOON, Unit.SPOON));
        }

        @Test
        @DisplayName("PIECE -> PIECE returns same quantity")
        void pieceToPiece() {
            assertEquals(10.0, UnitConverter.convert(10.0, Unit.PIECE, Unit.PIECE));
        }
    }

    // ==================== Mass conversions ====================

    @Nested
    @DisplayName("Mass conversions (GRAM <-> KILOGRAM)")
    class MassConversions {

        @Test
        @DisplayName("1000 GRAM -> 1 KILOGRAM")
        void gramToKilogram() {
            assertEquals(1.0, UnitConverter.convert(1000.0, Unit.GRAM, Unit.KILOGRAM), 1e-9);
        }

        @Test
        @DisplayName("500 GRAM -> 0.5 KILOGRAM")
        void gramToKilogramHalf() {
            assertEquals(0.5, UnitConverter.convert(500.0, Unit.GRAM, Unit.KILOGRAM), 1e-9);
        }

        @Test
        @DisplayName("1 KILOGRAM -> 1000 GRAM")
        void kilogramToGram() {
            assertEquals(1000.0, UnitConverter.convert(1.0, Unit.KILOGRAM, Unit.GRAM), 1e-9);
        }

        @Test
        @DisplayName("2.5 KILOGRAM -> 2500 GRAM")
        void kilogramToGramDecimal() {
            assertEquals(2500.0, UnitConverter.convert(2.5, Unit.KILOGRAM, Unit.GRAM), 1e-9);
        }
    }

    // ==================== Volume conversions ====================

    @Nested
    @DisplayName("Volume conversions (LITER <-> MILLILITER)")
    class VolumeConversions {

        @Test
        @DisplayName("1 LITER -> 1000 MILLILITER")
        void literToMilliliter() {
            assertEquals(1000.0, UnitConverter.convert(1.0, Unit.LITER, Unit.MILLILITER), 1e-9);
        }

        @Test
        @DisplayName("1000 MILLILITER -> 1 LITER")
        void milliliterToLiter() {
            assertEquals(1.0, UnitConverter.convert(1000.0, Unit.MILLILITER, Unit.LITER), 1e-9);
        }

        @Test
        @DisplayName("500 MILLILITER -> 0.5 LITER")
        void milliliterToLiterHalf() {
            assertEquals(0.5, UnitConverter.convert(500.0, Unit.MILLILITER, Unit.LITER), 1e-9);
        }
    }

    // ==================== CUP conversions ====================

    @Nested
    @DisplayName("CUP conversions")
    class CupConversions {

        @Test
        @DisplayName("1 CUP -> 200 GRAM")
        void cupToGram() {
            assertEquals(200.0, UnitConverter.convert(1.0, Unit.CUP, Unit.GRAM), 1e-9);
        }

        @Test
        @DisplayName("1 CUP -> 200 MILLILITER")
        void cupToMilliliter() {
            assertEquals(200.0, UnitConverter.convert(1.0, Unit.CUP, Unit.MILLILITER), 1e-9);
        }

        @Test
        @DisplayName("5 CUP -> 1 KILOGRAM")
        void cupToKilogram() {
            assertEquals(1.0, UnitConverter.convert(5.0, Unit.CUP, Unit.KILOGRAM), 1e-9);
        }

        @Test
        @DisplayName("5 CUP -> 1 LITER")
        void cupToLiter() {
            assertEquals(1.0, UnitConverter.convert(5.0, Unit.CUP, Unit.LITER), 1e-9);
        }

        @Test
        @DisplayName("1 CUP -> 10 SPOON")
        void cupToSpoon() {
            assertEquals(10.0, UnitConverter.convert(1.0, Unit.CUP, Unit.SPOON), 1e-9);
        }

        @Test
        @DisplayName("200 GRAM -> 1 CUP")
        void gramToCup() {
            assertEquals(1.0, UnitConverter.convert(200.0, Unit.GRAM, Unit.CUP), 1e-9);
        }

        @Test
        @DisplayName("200 MILLILITER -> 1 CUP")
        void milliliterToCup() {
            assertEquals(1.0, UnitConverter.convert(200.0, Unit.MILLILITER, Unit.CUP), 1e-9);
        }
    }

    // ==================== SPOON conversions ====================

    @Nested
    @DisplayName("SPOON conversions")
    class SpoonConversions {

        @Test
        @DisplayName("1 SPOON -> 20 GRAM")
        void spoonToGram() {
            assertEquals(20.0, UnitConverter.convert(1.0, Unit.SPOON, Unit.GRAM), 1e-9);
        }

        @Test
        @DisplayName("1 SPOON -> 20 MILLILITER")
        void spoonToMilliliter() {
            assertEquals(20.0, UnitConverter.convert(1.0, Unit.SPOON, Unit.MILLILITER), 1e-9);
        }

        @Test
        @DisplayName("50 SPOON -> 1 KILOGRAM")
        void spoonToKilogram() {
            assertEquals(1.0, UnitConverter.convert(50.0, Unit.SPOON, Unit.KILOGRAM), 1e-9);
        }

        @Test
        @DisplayName("50 SPOON -> 1 LITER")
        void spoonToLiter() {
            assertEquals(1.0, UnitConverter.convert(50.0, Unit.SPOON, Unit.LITER), 1e-9);
        }

        @Test
        @DisplayName("10 SPOON -> 1 CUP")
        void spoonToCup() {
            assertEquals(1.0, UnitConverter.convert(10.0, Unit.SPOON, Unit.CUP), 1e-9);
        }

        @Test
        @DisplayName("1 GRAM -> 0.05 SPOON")
        void gramToSpoon() {
            assertEquals(0.05, UnitConverter.convert(1.0, Unit.GRAM, Unit.SPOON), 1e-9);
        }

        @Test
        @DisplayName("1 KILOGRAM -> 50 SPOON")
        void kilogramToSpoon() {
            assertEquals(50.0, UnitConverter.convert(1.0, Unit.KILOGRAM, Unit.SPOON), 1e-9);
        }

        @Test
        @DisplayName("1 LITER -> 50 SPOON")
        void literToSpoon() {
            assertEquals(50.0, UnitConverter.convert(1.0, Unit.LITER, Unit.SPOON), 1e-9);
        }

        @Test
        @DisplayName("1 KILOGRAM -> 5 CUP")
        void kilogramToCup() {
            assertEquals(5.0, UnitConverter.convert(1.0, Unit.KILOGRAM, Unit.CUP), 1e-9);
        }

        @Test
        @DisplayName("1 LITER -> 5 CUP")
        void literToCup() {
            assertEquals(5.0, UnitConverter.convert(1.0, Unit.LITER, Unit.CUP), 1e-9);
        }
    }

    // ==================== Incompatible conversions ====================

    @Nested
    @DisplayName("Incompatible unit conversions throw IllegalArgumentException")
    class IncompatibleConversions {

        @Test
        @DisplayName("GRAM -> LITER throws exception")
        void gramToLiter() {
            assertThrows(IllegalArgumentException.class,
                    () -> UnitConverter.convert(100.0, Unit.GRAM, Unit.LITER));
        }

        @Test
        @DisplayName("GRAM -> MILLILITER throws exception")
        void gramToMilliliter() {
            assertThrows(IllegalArgumentException.class,
                    () -> UnitConverter.convert(100.0, Unit.GRAM, Unit.MILLILITER));
        }

        @Test
        @DisplayName("KILOGRAM -> LITER throws exception")
        void kilogramToLiter() {
            assertThrows(IllegalArgumentException.class,
                    () -> UnitConverter.convert(1.0, Unit.KILOGRAM, Unit.LITER));
        }

        @Test
        @DisplayName("KILOGRAM -> MILLILITER throws exception")
        void kilogramToMilliliter() {
            assertThrows(IllegalArgumentException.class,
                    () -> UnitConverter.convert(1.0, Unit.KILOGRAM, Unit.MILLILITER));
        }

        @Test
        @DisplayName("LITER -> GRAM throws exception")
        void literToGram() {
            assertThrows(IllegalArgumentException.class,
                    () -> UnitConverter.convert(1.0, Unit.LITER, Unit.GRAM));
        }

        @Test
        @DisplayName("LITER -> KILOGRAM throws exception")
        void literToKilogram() {
            assertThrows(IllegalArgumentException.class,
                    () -> UnitConverter.convert(1.0, Unit.LITER, Unit.KILOGRAM));
        }

        @Test
        @DisplayName("MILLILITER -> GRAM throws exception")
        void milliliterToGram() {
            assertThrows(IllegalArgumentException.class,
                    () -> UnitConverter.convert(100.0, Unit.MILLILITER, Unit.GRAM));
        }

        @Test
        @DisplayName("MILLILITER -> KILOGRAM throws exception")
        void milliliterToKilogram() {
            assertThrows(IllegalArgumentException.class,
                    () -> UnitConverter.convert(100.0, Unit.MILLILITER, Unit.KILOGRAM));
        }
    }

    // ==================== PIECE incompatibility ====================

    @Nested
    @DisplayName("PIECE incompatible with other units")
    class PieceIncompatible {

        @Test
        @DisplayName("PIECE -> GRAM throws exception")
        void pieceToGram() {
            assertThrows(IllegalArgumentException.class,
                    () -> UnitConverter.convert(1.0, Unit.PIECE, Unit.GRAM));
        }

        @Test
        @DisplayName("PIECE -> KILOGRAM throws exception")
        void pieceToKilogram() {
            assertThrows(IllegalArgumentException.class,
                    () -> UnitConverter.convert(1.0, Unit.PIECE, Unit.KILOGRAM));
        }

        @Test
        @DisplayName("PIECE -> LITER throws exception")
        void pieceToLiter() {
            assertThrows(IllegalArgumentException.class,
                    () -> UnitConverter.convert(1.0, Unit.PIECE, Unit.LITER));
        }

        @Test
        @DisplayName("PIECE -> MILLILITER throws exception")
        void pieceToMilliliter() {
            assertThrows(IllegalArgumentException.class,
                    () -> UnitConverter.convert(1.0, Unit.PIECE, Unit.MILLILITER));
        }

        @Test
        @DisplayName("PIECE -> CUP throws exception")
        void pieceToCup() {
            assertThrows(IllegalArgumentException.class,
                    () -> UnitConverter.convert(1.0, Unit.PIECE, Unit.CUP));
        }

        @Test
        @DisplayName("PIECE -> SPOON throws exception")
        void pieceToSpoon() {
            assertThrows(IllegalArgumentException.class,
                    () -> UnitConverter.convert(1.0, Unit.PIECE, Unit.SPOON));
        }
    }

    // ==================== Zero and edge cases ====================

    @Nested
    @DisplayName("Edge cases")
    class EdgeCases {

        @Test
        @DisplayName("Zero quantity converts to zero")
        void zeroQuantity() {
            assertEquals(0.0, UnitConverter.convert(0.0, Unit.GRAM, Unit.KILOGRAM), 1e-9);
        }

        @Test
        @DisplayName("Very large quantity converts correctly")
        void largeQuantity() {
            assertEquals(1_000_000.0, UnitConverter.convert(1_000_000_000.0, Unit.GRAM, Unit.KILOGRAM), 1e-6);
        }

        @Test
        @DisplayName("Very small quantity converts correctly")
        void smallQuantity() {
            assertEquals(0.001, UnitConverter.convert(1.0, Unit.GRAM, Unit.KILOGRAM), 1e-9);
        }

        @Test
        @DisplayName("Negative quantity converts correctly")
        void negativeQuantity() {
            assertEquals(-1.0, UnitConverter.convert(-1000.0, Unit.GRAM, Unit.KILOGRAM), 1e-9);
        }
    }

    // ==================== Roundtrip conversions ====================

    @Nested
    @DisplayName("Roundtrip conversions")
    class RoundtripConversions {

        @Test
        @DisplayName("GRAM -> KILOGRAM -> GRAM roundtrip")
        void gramKilogramRoundtrip() {
            double original = 1500.0;
            double converted = UnitConverter.convert(original, Unit.GRAM, Unit.KILOGRAM);
            double roundtrip = UnitConverter.convert(converted, Unit.KILOGRAM, Unit.GRAM);
            assertEquals(original, roundtrip, 1e-9);
        }

        @Test
        @DisplayName("LITER -> MILLILITER -> LITER roundtrip")
        void literMilliliterRoundtrip() {
            double original = 2.5;
            double converted = UnitConverter.convert(original, Unit.LITER, Unit.MILLILITER);
            double roundtrip = UnitConverter.convert(converted, Unit.MILLILITER, Unit.LITER);
            assertEquals(original, roundtrip, 1e-9);
        }

        @Test
        @DisplayName("CUP -> SPOON -> CUP roundtrip")
        void cupSpoonRoundtrip() {
            double original = 3.0;
            double converted = UnitConverter.convert(original, Unit.CUP, Unit.SPOON);
            double roundtrip = UnitConverter.convert(converted, Unit.SPOON, Unit.CUP);
            assertEquals(original, roundtrip, 1e-9);
        }

        @Test
        @DisplayName("SPOON -> GRAM -> SPOON roundtrip")
        void spoonGramRoundtrip() {
            double original = 5.0;
            double converted = UnitConverter.convert(original, Unit.SPOON, Unit.GRAM);
            double roundtrip = UnitConverter.convert(converted, Unit.GRAM, Unit.SPOON);
            assertEquals(original, roundtrip, 1e-9);
        }
    }

    // ==================== Parameterized tests ====================

    @ParameterizedTest(name = "{0} {1} -> {3} {2}")
    @MethodSource("conversionProvider")
    @DisplayName("Parameterized conversions")
    void parameterizedConversions(double input, Unit from, Unit to, double expected) {
        assertEquals(expected, UnitConverter.convert(input, from, to), 1e-9);
    }

    static Stream<Arguments> conversionProvider() {
        return Stream.of(
                Arguments.of(1000.0, Unit.GRAM, Unit.KILOGRAM, 1.0),
                Arguments.of(1.0, Unit.KILOGRAM, Unit.GRAM, 1000.0),
                Arguments.of(1000.0, Unit.MILLILITER, Unit.LITER, 1.0),
                Arguments.of(1.0, Unit.LITER, Unit.MILLILITER, 1000.0),
                Arguments.of(1.0, Unit.CUP, Unit.SPOON, 10.0),
                Arguments.of(10.0, Unit.SPOON, Unit.CUP, 1.0),
                Arguments.of(1.0, Unit.CUP, Unit.GRAM, 200.0),
                Arguments.of(1.0, Unit.SPOON, Unit.GRAM, 20.0)
        );
    }
}
