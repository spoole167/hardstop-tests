import dev.gruff.hardstop.testcases.apicheck.FieldAccessChecks;
import dev.gruff.hardstop.testcases.apicheck.Version;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests changes to static field values.
 * Matches spec scenario E.85: static final constant value changed.
 * 'static final' primitive or String constants are often inlined at compile-time.
 */
public class TestStaticFieldValue {

    /**
     * In V1, helloStaticFinal is "helloV1SF".
     * In V2, it is "helloV2SF".
     * However, because it is 'static final', the value is inlined into the test class at compile-time.
     * Thus, even when running against V2, the test still sees "helloV1SF".
     */
    @Test
    public void testStaticFinal() {
        // V2 has different value from V1 but
        // should not show up as value is copied at compile time
        if(Version.isV1()) {
            assertEquals("helloV1SF", FieldAccessChecks.helloStaticFinal);
        } else {
            assertEquals("helloV1SF", FieldAccessChecks.helloStaticFinal);
        }

    }

    /**
     * In V1, staticHello is "helloV1S".
     * In V2, it is "helloV2S".
     * Since it is NOT 'final', it is not inlined, and the change is visible at runtime.
     */
    @Test
    public void testStatic() {
        // V2 has different value from V1
        if(Version.isV1()) {
            assertEquals("helloV1S", FieldAccessChecks.staticHello);
        } else {
            assertEquals("helloV2S", FieldAccessChecks.staticHello);
        }

    }
}
