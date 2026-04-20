package com.bitbites.bitbites2.backend.users;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 tests for PasswordUtils.
 *
 * Strategies:
 * - Equivalence partitioning (normal, empty, special chars, unicode)
 * - Boundary analysis (empty string, single char, very long string)
 * - Decision coverage (SHA-256 always available, consistent output)
 */
class PasswordUtilsTest {

    @Test
    @DisplayName("Hash is not null")
    void hashNotNull() {
        assertNotNull(PasswordUtils.hashPassword("password"));
    }

    @Test
    @DisplayName("Hash is 64 hex characters (SHA-256)")
    void hashLength() {
        String hash = PasswordUtils.hashPassword("test");
        assertEquals(64, hash.length());
    }

    @Test
    @DisplayName("Hash contains only hex characters")
    void hashHexOnly() {
        String hash = PasswordUtils.hashPassword("hello");
        assertTrue(hash.matches("[0-9a-f]{64}"));
    }

    @Test
    @DisplayName("Same password produces same hash (deterministic)")
    void deterministic() {
        String hash1 = PasswordUtils.hashPassword("mySecret123");
        String hash2 = PasswordUtils.hashPassword("mySecret123");
        assertEquals(hash1, hash2);
    }

    @Test
    @DisplayName("Different passwords produce different hashes")
    void differentPasswords() {
        String hash1 = PasswordUtils.hashPassword("password1");
        String hash2 = PasswordUtils.hashPassword("password2");
        assertNotEquals(hash1, hash2);
    }

    @Test
    @DisplayName("Empty string can be hashed")
    void emptyString() {
        String hash = PasswordUtils.hashPassword("");
        assertNotNull(hash);
        assertEquals(64, hash.length());
        // Known SHA-256 of empty string
        assertEquals("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855", hash);
    }

    @Test
    @DisplayName("Single character password")
    void singleChar() {
        String hash = PasswordUtils.hashPassword("a");
        assertEquals(64, hash.length());
    }

    @Test
    @DisplayName("Very long password")
    void veryLongPassword() {
        String longPassword = "a".repeat(10_000);
        String hash = PasswordUtils.hashPassword(longPassword);
        assertEquals(64, hash.length());
    }

    @Test
    @DisplayName("Special characters in password")
    void specialCharacters() {
        String hash = PasswordUtils.hashPassword("p@$$w0rd!#%^&*()");
        assertEquals(64, hash.length());
    }

    @Test
    @DisplayName("Unicode characters in password")
    void unicodeCharacters() {
        String hash = PasswordUtils.hashPassword("pârölé🔑");
        assertEquals(64, hash.length());
    }

    @Test
    @DisplayName("Known SHA-256 value for 'password'")
    void knownHash() {
        // SHA-256("password") is well-known
        assertEquals(
                "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8",
                PasswordUtils.hashPassword("password")
        );
    }

    @Nested
    @DisplayName("Case sensitivity")
    class CaseSensitivity {

        @Test
        void upperAndLowerDiffer() {
            assertNotEquals(
                    PasswordUtils.hashPassword("Password"),
                    PasswordUtils.hashPassword("password")
            );
        }

        @Test
        void allUpperDiffers() {
            assertNotEquals(
                    PasswordUtils.hashPassword("PASSWORD"),
                    PasswordUtils.hashPassword("password")
            );
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc", "123", "abc123", "!@#", " ", "\t", "\n"})
    @DisplayName("Various inputs all produce valid 64-char hex hashes")
    void variousInputs(String input) {
        String hash = PasswordUtils.hashPassword(input);
        assertNotNull(hash);
        assertEquals(64, hash.length());
        assertTrue(hash.matches("[0-9a-f]{64}"));
    }
}
