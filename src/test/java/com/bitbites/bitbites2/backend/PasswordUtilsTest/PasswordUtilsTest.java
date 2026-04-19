package com.bitbites.bitbites2.backend.PasswordUtilsTest;

import com.bitbites.bitbites2.backend.users.PasswordUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PasswordUtilsTest {

    // ---------------
    //  hashPassword()
    // ---------------

    // a) Partitionare de echivalenta
    /*
     Partitii:
         P1: Parola vida
         P2: Parola normala (alfanumerica)
         P3: Parola cu caractere speciale/Unicode
     */

    @Test
    public void testHashPasswordNullPswd() {
        // P1
        String hash = PasswordUtils.hashPassword("");
        assertNotNull(hash);
        assertEquals(64, hash.length()); // SHA-256 are intotdeauna 64 caractere hex
    }

    @Test
    public void testHashPasswordOkPswd() {
        // P2
        String p1 = "parola123";
        String hash1 = PasswordUtils.hashPassword(p1);
        String hash2 = PasswordUtils.hashPassword(p1);

        assertEquals(hash1, hash2); // Determinism: aceeasi parola = acelasi hash
        assertNotEquals(p1, hash1); // Securitate: hash-ul nu trebuie sa fie textul clar
    }

    // b) Boundary Value Analysis
    /*
     * Limite:
     * - Lungime 1: Cel mai mic input non-vid
     * - Lungime foarte mare: Verificarea performantei/stabilitatii
     */

    @Test
    public void testHashPasswordMinLen() {
        String hash = PasswordUtils.hashPassword("a");
        assertEquals(64, hash.length());
    }

    @Test
    public void testHashPasswordLong() {
        String lunga = "a".repeat(1000);
        String hash = PasswordUtils.hashPassword(lunga);
        assertEquals(64, hash.length());
    }

    // c) Category Partitioning
    /*
     * Parametri: password
     * Categorii:
     * - Tip caractere: Doar cifre, Doar litere, Simboluri, Spatii
     */

    @Test
    public void testHashPasswordSpecial() {
        String hash = PasswordUtils.hashPassword("!@#$%^&*()_+ 🧠");
        assertNotNull(hash);
        assertTrue(hash.matches("^[a-f0-9]+$")); // Trebuie sa fie doar caractere hexazecimale
    }

    // d) Statement Coverage
    @Test
    public void TestStmt_hashPassword() {
        // Executa toate liniile din blocul try, inclusiv append-ul in loop
        String result = PasswordUtils.hashPassword("test");
        assertNotNull(result);
    }

    // e) Decision Coverage
    @Test
    public void TestDec_hashPassword() {
        // Decizia 1: Bucla for (are elemente de parcurs? DA)
        PasswordUtils.hashPassword("a");

        // Decizia 2: Catch block (NoSuchAlgorithmException)
        // Aceasta decizie este aproape imposibil de declansat pe un JVM standard
        // (SHA-256 e un algoritm obligatoriu)
    }

    // Cyclomatic Complexity
    // V(G) = 1 (start point + initial sequence) + 1 (for) + 1 (catch) = 3
    @Test
    public void TestCirc_hashPassword() {
        // C1: Calea de succes (fara exceptie, bucla se executa)
        String hash = PasswordUtils.hashPassword("abc");
        assertNotNull(hash);

        // C2: Calea de exceptie (teoretic)
        // Ar merge doar cu injectia unui Mock pentru MessageDigest,
        // altfel ramura catch ramane neacoperita functional dar calculata matematic.
    }
}
