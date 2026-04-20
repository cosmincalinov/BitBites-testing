package testng_gpt_codex_5_3.com.bitbites.bitbites2.backend.users;

import com.bitbites.bitbites2.backend.users.PasswordUtils;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class PasswordUtilsTestNGTest {

    @Test
    public void shouldHashDeterministically() {
        String first = PasswordUtils.hashPassword("my-secret");
        String second = PasswordUtils.hashPassword("my-secret");

        assertEquals(first, second);
        assertEquals(first.length(), 64);
        assertTrue(first.matches("^[0-9a-f]{64}$"));
    }

    @Test
    public void shouldMatchKnownHash() {
        assertEquals(
                PasswordUtils.hashPassword("password"),
                "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8"
        );
    }
}
