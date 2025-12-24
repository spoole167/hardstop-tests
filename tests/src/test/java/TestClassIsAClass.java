import dev.gruff.hardstop.testcases.apicheck.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests the conversion of a class to an interface.
 * Matches spec scenario B.39: class ↔ interface change.
 * This change is binary and source incompatible.
 */
public class TestClassIsAClass {

    /**
     * Checks if JustAClass is an interface via reflection.
     * In V1 it is a class (isInterface=false), in V2 it is an interface (isInterface=true).
     */
    @Test
    public void testme() {

        if(Version.isV1()) {
            assertFalse(JustAClass.class.isInterface());
        } else {
            assertTrue(JustAClass.class.isInterface());
        }


    }
}
