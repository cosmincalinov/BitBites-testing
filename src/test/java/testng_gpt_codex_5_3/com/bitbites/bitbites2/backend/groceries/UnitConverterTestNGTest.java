package testng_gpt_codex_5_3.com.bitbites.bitbites2.backend.groceries;

import com.bitbites.bitbites2.backend.groceries.Unit;
import org.testng.annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.testng.Assert.*;

public class UnitConverterTestNGTest {

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
    public void shouldConvertMassAndVolumeUnits() {
        assertEquals(convert(1000.0, Unit.GRAM, Unit.KILOGRAM), 1.0, 1e-9);
        assertEquals(convert(1.0, Unit.LITER, Unit.MILLILITER), 1000.0, 1e-9);
    }

    @Test
    public void shouldThrowForPieceToNonPieceConversion() {
        IllegalArgumentException ex = expectThrows(
                IllegalArgumentException.class,
                () -> convert(2.0, Unit.PIECE, Unit.GRAM)
        );

        assertTrue(ex.getMessage().contains("piece"));
        assertTrue(ex.getMessage().contains("gram"));
    }
}
