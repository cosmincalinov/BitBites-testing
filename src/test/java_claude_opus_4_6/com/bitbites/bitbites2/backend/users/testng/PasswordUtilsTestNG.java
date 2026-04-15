package com.bitbites.bitbites2.backend.users.testng;

import com.bitbites.bitbites2.backend.users.PasswordUtils;
import org.testng.Assert;
import org.testng.annotations.*;

/**
 * TestNG tests for PasswordUtils.
 * Uses DataProvider for parameterized tests and groups for test organization.
 */
public class PasswordUtilsTestNG {

    // ==================== Basic hashing ====================

    @Test(groups = {"basic"})
    public void hashPassword_notNull() {
        Assert.assertNotNull(PasswordUtils.hashPassword("password123"));
    }

    @Test(groups = {"basic"})
    public void hashPassword_notEmpty() {
        Assert.assertFalse(PasswordUtils.hashPassword("password123").isEmpty());
    }

    @Test(groups = {"basic"})
    public void hashPassword_returns64CharSHA256Hex() {
        String hash = PasswordUtils.hashPassword("test");
        Assert.assertEquals(hash.length(), 64, "SHA-256 hex should be 64 characters");
    }

    @Test(groups = {"basic"})
    public void hashPassword_isLowercaseHex() {
        String hash = PasswordUtils.hashPassword("test");
        Assert.assertTrue(hash.matches("[0-9a-f]{64}"),
                "Hash should be lowercase hexadecimal, got: " + hash);
    }

    // ==================== Determinism ====================

    @Test(groups = {"determinism"})
    public void hashPassword_sameInputSameOutput() {
        String hash1 = PasswordUtils.hashPassword("myPassword");
        String hash2 = PasswordUtils.hashPassword("myPassword");
        Assert.assertEquals(hash1, hash2);
    }

    @Test(groups = {"determinism"}, invocationCount = 10)
    public void hashPassword_consistentAcrossMultipleCalls() {
        String expected = PasswordUtils.hashPassword("consistencyTest!@#");
        Assert.assertEquals(PasswordUtils.hashPassword("consistencyTest!@#"), expected);
    }

    // ==================== Different passwords ====================

    @DataProvider(name = "differentPasswordPairs")
    public Object[][] differentPasswordPairs() {
        return new Object[][]{
                {"password1", "password2"},
                {"Password", "password"},
                {"password", " password"},
                {"password", "password "},
                {" password", "password "},
                {"abc", "abd"},
                {"short", "shorter"},
                {"UPPER", "upper"},
        };
    }

    @Test(dataProvider = "differentPasswordPairs", groups = {"uniqueness"})
    public void hashPassword_differentInputsDifferentOutputs(String pw1, String pw2) {
        Assert.assertNotEquals(PasswordUtils.hashPassword(pw1), PasswordUtils.hashPassword(pw2),
                "Hashes should differ for '" + pw1 + "' and '" + pw2 + "'");
    }

    // ==================== Known SHA-256 values ====================

    @DataProvider(name = "knownSHA256Values")
    public Object[][] knownSHA256Values() {
        return new Object[][]{
                {"", "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855"},
                {"test", "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08"},
                {"password", "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8"},
        };
    }

    @Test(dataProvider = "knownSHA256Values", groups = {"knownValues"})
    public void hashPassword_matchesKnownSHA256(String input, String expectedHash) {
        Assert.assertEquals(PasswordUtils.hashPassword(input), expectedHash);
    }

    // ==================== Edge cases ====================

    @Test(groups = {"edge"})
    public void hashPassword_emptyString() {
        String hash = PasswordUtils.hashPassword("");
        Assert.assertNotNull(hash);
        Assert.assertEquals(hash.length(), 64);
    }

    @Test(groups = {"edge"})
    public void hashPassword_veryLongInput() {
        String longPassword = "a".repeat(10000);
        String hash = PasswordUtils.hashPassword(longPassword);
        Assert.assertNotNull(hash);
        Assert.assertEquals(hash.length(), 64);
    }

    @Test(groups = {"edge"})
    public void hashPassword_specialCharacters() {
        String hash = PasswordUtils.hashPassword("!@#$%^&*()_+-=[]{}|;':\",./<>?");
        Assert.assertNotNull(hash);
        Assert.assertEquals(hash.length(), 64);
    }

    @Test(groups = {"edge"})
    public void hashPassword_unicodeCharacters() {
        String hash = PasswordUtils.hashPassword("пароль密码パスワード");
        Assert.assertNotNull(hash);
        Assert.assertEquals(hash.length(), 64);
    }

    @Test(groups = {"edge"})
    public void hashPassword_newlines() {
        String hash = PasswordUtils.hashPassword("line1\nline2\nline3");
        Assert.assertNotNull(hash);
        Assert.assertEquals(hash.length(), 64);
    }

    @Test(groups = {"edge"})
    public void hashPassword_nullBytes() {
        String hash = PasswordUtils.hashPassword("null\0byte");
        Assert.assertNotNull(hash);
        Assert.assertEquals(hash.length(), 64);
    }

    // ==================== Security properties ====================

    @Test(groups = {"security"})
    public void hashPassword_hashIsNotTheOriginal() {
        String password = "mySecret123";
        Assert.assertNotEquals(PasswordUtils.hashPassword(password), password);
    }

    @DataProvider(name = "variousPasswords")
    public Object[][] variousPasswords() {
        return new Object[][]{
                {"short"},
                {"medium_length_password"},
                {"a very long password with spaces"},
        };
    }

    @Test(dataProvider = "variousPasswords", groups = {"security"})
    public void hashPassword_hashNotEqualToInput(String password) {
        Assert.assertNotEquals(PasswordUtils.hashPassword(password), password);
    }

    @Test(groups = {"security"})
    public void hashPassword_doesNotContainOriginal() {
        String password = "secretPassword";
        String hash = PasswordUtils.hashPassword(password);
        Assert.assertFalse(hash.contains(password),
                "Hash should not contain the original password");
    }
}
