package gpt_codex_5_3.com.bitbites.bitbites2.backend.users;

import com.bitbites.bitbites2.backend.users.PasswordUtils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordUtilsTest {

    @Test
    void hashPasswordShouldBeDeterministic() {
        String first = PasswordUtils.hashPassword("my-secret");
        String second = PasswordUtils.hashPassword("my-secret");

        assertEquals(first, second);
    }

    @Test
    void hashPasswordShouldMatchKnownSha256Value() {
        String hash = PasswordUtils.hashPassword("password");

        assertEquals("5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8", hash);
    }

    @Test
    void hashPasswordShouldProduceDifferentHashesForDifferentInputs() {
        String first = PasswordUtils.hashPassword("alpha");
        String second = PasswordUtils.hashPassword("beta");

        assertNotEquals(first, second);
    }

    @Test
    void hashPasswordShouldReturn64CharacterLowercaseHex() {
        String hash = PasswordUtils.hashPassword("anything");

        assertEquals(64, hash.length());
        assertTrue(hash.matches("^[0-9a-f]{64}$"));
    }
}
