import dev.gruff.hardstop.testcases.apicheck.EnumLosesValue;
import dev.gruff.hardstop.testcases.apicheck.Version;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the removal of an enum constant.
 * Matches spec scenario E.80: Field removed (Enum constants are static fields).
 * Results in NoSuchFieldError at runtime when the removed constant is accessed.
 */
public class TestLostEnum {

    /**
     * Checks if the number of enum constants changed.
     */
    @Test
    public void testEnumSize() {
        if(Version.isV1()) {
            assertEquals(6, EnumLosesValue.values().length);
        }
        else {
            assertEquals(5,EnumLosesValue.values().length);
            }
    }

    /**
     * In V1, EnumLosesValue.c exists.
     * In V2, it is removed.
     * Accessing EnumLosesValue.c in code compiled against V1 results in NoSuchFieldError.
     */
    @Test
    public void testLostEnum() {
        if(Version.isV1()) {
            assertEquals("c",EnumLosesValue.c.name());
        }
        else {
            try {
                assertEquals("c", EnumLosesValue.c.name());
                fail("error expected");
            } catch(NoSuchFieldError e) {
                assertEquals("Class dev.gruff.hardstop.testcases.apicheck.EnumLosesValue does not have member field 'dev.gruff.hardstop.testcases.apicheck.EnumLosesValue c'",e.getMessage());
            }
        }
    }
}
