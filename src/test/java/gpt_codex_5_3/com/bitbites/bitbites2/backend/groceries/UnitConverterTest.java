package gpt_codex_5_3.com.bitbites.bitbites2.backend.groceries;

import com.bitbites.bitbites2.backend.groceries.Unit;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class UnitConverterTest {

    private double convert(double quantity, Unit from, Unit to) {
        try {
            Class<?> converterClass = Class.forName("com.bitbites.bitbites2.backend.groceries.UnitConverter");
            Method method = converterClass.getDeclaredMethod("convert", double.class, Unit.class, Unit.class);
            method.setAccessible(true);
            return (double) method.invoke(null, quantity, from, to);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException runtimeException) {
                throw runtimeException;
            }
            throw new RuntimeException(cause);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldConvertMassUnits() {
        assertEquals(1.0, convert(1000.0, Unit.GRAM, Unit.KILOGRAM), 1e-9);
        assertEquals(500.0, convert(0.5, Unit.KILOGRAM, Unit.GRAM), 1e-9);
    }

    @Test
    void shouldConvertVolumeUnits() {
        assertEquals(1000.0, convert(1.0, Unit.LITER, Unit.MILLILITER), 1e-9);
        assertEquals(0.25, convert(250.0, Unit.MILLILITER, Unit.LITER), 1e-9);
    }

    @Test
    void shouldConvertCountCompatibleUnits() {
        assertEquals(200.0, convert(1.0, Unit.CUP, Unit.GRAM), 1e-9);
        assertEquals(2.0, convert(20.0, Unit.SPOON, Unit.CUP), 1e-9);
    }

    @Test
    void shouldReturnSameQuantityForIdentityConversion() {
        assertEquals(42.0, convert(42.0, Unit.GRAM, Unit.GRAM), 1e-9);
        assertEquals(7.0, convert(7.0, Unit.PIECE, Unit.PIECE), 1e-9);
    }

    @Test
    void shouldThrowForPieceToNonPieceConversion() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> convert(3.0, Unit.PIECE, Unit.GRAM)
        );

        assertTrue(ex.getMessage().contains("gram"));
        assertTrue(ex.getMessage().contains("piece"));
    }
}
