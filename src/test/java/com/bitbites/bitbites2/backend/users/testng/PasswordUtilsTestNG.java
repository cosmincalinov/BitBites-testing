package com.bitbites.bitbites2.backend.users.testng;

import com.bitbites.bitbites2.backend.users.PasswordUtils;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import static org.testng.Assert.*;

/**
 * TestNG tests for PasswordUtils.
 *
 * Comparative note vs JUnit:
 * - Uses @DataProvider for parameterized input testing (vs @ParameterizedTest + @ValueSource)
 * - Uses SoftAssert for bundled assertions
 * - Uses groups and priority for test ordering
 */
public class PasswordUtilsTestNG {

    @Test(priority = 1, groups = "basic")
    public void hashNotNull() {
        assertNotNull(PasswordUtils.hashPassword("password"));
    }

    @Test(priority = 1, groups = "basic")
    public void hashLength() {
        assertEquals(PasswordUtils.hashPassword("test").length(), 64);
    }

    @Test(priority = 1, groups = "basic")
    public void hashHexOnly() {
        String hash = PasswordUtils.hashPassword("hello");
        assertTrue(hash.matches("[0-9a-f]{64}"));
    }

    @Test(groups = "determinism")
    public void deterministic() {
        String hash1 = PasswordUtils.hashPassword("mySecret123");
        String hash2 = PasswordUtils.hashPassword("mySecret123");
        assertEquals(hash1, hash2);
    }

    @Test(groups = "determinism")
    public void differentPasswords() {
        String hash1 = PasswordUtils.hashPassword("password1");
        String hash2 = PasswordUtils.hashPassword("password2");
        assertNotEquals(hash1, hash2);
    }

    @Test(groups = "boundary")
    public void emptyString() {
        SoftAssert sa = new SoftAssert();
        String hash = PasswordUtils.hashPassword("");
        sa.assertNotNull(hash);
        sa.assertEquals(hash.length(), 64);
        sa.assertEquals(hash, "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855");
        sa.assertAll();
    }

    @Test(groups = "boundary")
    public void singleChar() {
        assertEquals(PasswordUtils.hashPassword("a").length(), 64);
    }

    @Test(groups = "boundary")
    public void veryLongPassword() {
        String longPassword = "a".repeat(10_000);
        assertEquals(PasswordUtils.hashPassword(longPassword).length(), 64);
    }

    @Test(groups = "special")
    public void specialCharacters() {
        assertEquals(PasswordUtils.hashPassword("p@$$w0rd!#%^&*()").length(), 64);
    }

    @Test(groups = "special")
    public void unicodeCharacters() {
        assertEquals(PasswordUtils.hashPassword("pârölé🔑").length(), 64);
    }

    @Test(groups = "known")
    public void knownHash() {
        assertEquals(
                PasswordUtils.hashPassword("password"),
                "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8"
        );
    }

    @Test(groups = "caseSensitivity")
    public void upperAndLowerDiffer() {
        assertNotEquals(
                PasswordUtils.hashPassword("Password"),
                PasswordUtils.hashPassword("password")
        );
    }

    @DataProvider(name = "variousInputs")
    public Object[][] variousInputs() {
        return new Object[][]{
                {"abc"}, {"123"}, {"abc123"}, {"!@#"}, {" "}, {"\t"}, {"\n"},
        };
    }

    @Test(dataProvider = "variousInputs", groups = "parameterized")
    public void variousInputsProduceValidHash(String input) {
        SoftAssert sa = new SoftAssert();
        String hash = PasswordUtils.hashPassword(input);
        sa.assertNotNull(hash);
        sa.assertEquals(hash.length(), 64);
        sa.assertTrue(hash.matches("[0-9a-f]{64}"));
        sa.assertAll();
    }
}
