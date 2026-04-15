package com.bitbites.bitbites2.backend.users;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PasswordUtils Tests")
class PasswordUtilsTest {

    // ==================== Basic hashing ====================

    @Nested
    @DisplayName("Basic hashing behavior")
    class BasicHashingTests {

        @Test
        @DisplayName("Hash of a password is not null")
        void hashNotNull() {
            assertNotNull(PasswordUtils.hashPassword("password123"));
        }

        @Test
        @DisplayName("Hash of a password is not empty")
        void hashNotEmpty() {
            assertFalse(PasswordUtils.hashPassword("password123").isEmpty());
        }

        @Test
        @DisplayName("Hash has 64 characters (SHA-256 hex)")
        void hashLength() {
            String hash = PasswordUtils.hashPassword("test");
            assertEquals(64, hash.length());
        }

        @Test
        @DisplayName("Hash is lowercase hexadecimal")
        void hashIsLowercaseHex() {
            String hash = PasswordUtils.hashPassword("test");
            assertTrue(hash.matches("[0-9a-f]{64}"));
        }
    }

    // ==================== Determinism ====================

    @Nested
    @DisplayName("Deterministic behavior")
    class DeterministicTests {

        @Test
        @DisplayName("Same password produces same hash")
        void samePasswordSameHash() {
            String hash1 = PasswordUtils.hashPassword("myPassword");
            String hash2 = PasswordUtils.hashPassword("myPassword");
            assertEquals(hash1, hash2);
        }

        @Test
        @DisplayName("Same password multiple calls consistent")
        void samePasswordMultipleCalls() {
            String password = "consistencyTest!@#";
            String first = PasswordUtils.hashPassword(password);
            for (int i = 0; i < 10; i++) {
                assertEquals(first, PasswordUtils.hashPassword(password));
            }
        }
    }

    // ==================== Different passwords ====================

    @Nested
    @DisplayName("Different passwords produce different hashes")
    class DifferentPasswordsTests {

        @Test
        @DisplayName("Different passwords produce different hashes")
        void differentPasswordsDifferentHashes() {
            String hash1 = PasswordUtils.hashPassword("password1");
            String hash2 = PasswordUtils.hashPassword("password2");
            assertNotEquals(hash1, hash2);
        }

        @Test
        @DisplayName("Case sensitive - uppercase vs lowercase")
        void caseSensitive() {
            String hash1 = PasswordUtils.hashPassword("Password");
            String hash2 = PasswordUtils.hashPassword("password");
            assertNotEquals(hash1, hash2);
        }

        @Test
        @DisplayName("Leading/trailing whitespace matters")
        void whitespaceSensitive() {
            String hash1 = PasswordUtils.hashPassword("password");
            String hash2 = PasswordUtils.hashPassword(" password");
            String hash3 = PasswordUtils.hashPassword("password ");
            assertNotEquals(hash1, hash2);
            assertNotEquals(hash1, hash3);
            assertNotEquals(hash2, hash3);
        }

        @Test
        @DisplayName("Similar passwords produce different hashes")
        void similarPasswords() {
            String hash1 = PasswordUtils.hashPassword("abc");
            String hash2 = PasswordUtils.hashPassword("abd");
            assertNotEquals(hash1, hash2);
        }
    }

    // ==================== Known SHA-256 values ====================

    @Nested
    @DisplayName("Known SHA-256 hash values")
    class KnownHashTests {

        @Test
        @DisplayName("Empty string SHA-256 hash matches known value")
        void emptyStringHash() {
            // SHA-256 of empty string
            String expected = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";
            assertEquals(expected, PasswordUtils.hashPassword(""));
        }

        @Test
        @DisplayName("'test' SHA-256 hash matches known value")
        void testStringHash() {
            // SHA-256 of "test"
            String expected = "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08";
            assertEquals(expected, PasswordUtils.hashPassword("test"));
        }

        @Test
        @DisplayName("'password' SHA-256 hash matches known value")
        void passwordStringHash() {
            // SHA-256 of "password"
            String expected = "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8";
            assertEquals(expected, PasswordUtils.hashPassword("password"));
        }
    }

    // ==================== Edge cases ====================

    @Nested
    @DisplayName("Edge cases")
    class EdgeCaseTests {

        @Test
        @DisplayName("Empty string can be hashed")
        void emptyString() {
            assertDoesNotThrow(() -> PasswordUtils.hashPassword(""));
            assertNotNull(PasswordUtils.hashPassword(""));
        }

        @Test
        @DisplayName("Very long password can be hashed")
        void veryLongPassword() {
            String longPassword = "a".repeat(10000);
            String hash = PasswordUtils.hashPassword(longPassword);
            assertNotNull(hash);
            assertEquals(64, hash.length());
        }

        @Test
        @DisplayName("Password with special characters can be hashed")
        void specialCharacters() {
            String hash = PasswordUtils.hashPassword("!@#$%^&*()_+-=[]{}|;':\",./<>?");
            assertNotNull(hash);
            assertEquals(64, hash.length());
        }

        @Test
        @DisplayName("Password with unicode characters can be hashed")
        void unicodeCharacters() {
            String hash = PasswordUtils.hashPassword("пароль密码パスワード");
            assertNotNull(hash);
            assertEquals(64, hash.length());
        }

        @Test
        @DisplayName("Password with newlines can be hashed")
        void newlineCharacters() {
            String hash = PasswordUtils.hashPassword("line1\nline2\nline3");
            assertNotNull(hash);
            assertEquals(64, hash.length());
        }

        @Test
        @DisplayName("Password with null bytes can be hashed")
        void nullByteCharacters() {
            String hash = PasswordUtils.hashPassword("null\0byte");
            assertNotNull(hash);
            assertEquals(64, hash.length());
        }
    }

    // ==================== Hash is not the original password ====================

    @Nested
    @DisplayName("Security properties")
    class SecurityTests {

        @Test
        @DisplayName("Hash is not the original password")
        void hashIsNotOriginal() {
            String password = "mySecret123";
            String hash = PasswordUtils.hashPassword(password);
            assertNotEquals(password, hash);
        }

        @ParameterizedTest(name = "Hash of \"{0}\" is not the original")
        @ValueSource(strings = {"short", "medium_length_password", "a very long password with spaces"})
        @DisplayName("Various passwords produce hashes different from input")
        void hashNotEqualToInput(String password) {
            assertNotEquals(password, PasswordUtils.hashPassword(password));
        }

        @Test
        @DisplayName("Hash does not contain original password")
        void hashDoesNotContainOriginal() {
            String password = "secretPassword";
            String hash = PasswordUtils.hashPassword(password);
            assertFalse(hash.contains(password));
        }
    }
}
