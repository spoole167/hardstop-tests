import dev.gruff.hardstop.testcases.apicheck.FieldRemoved;
import dev.gruff.hardstop.testcases.apicheck.Version;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests removing a field from a class.
 * Matches spec scenario E.80: Field removed.
 * Results in NoSuchFieldError at runtime.
 */
public class TestFieldRemoved {

    /**
     * In V1, FieldRemoved has 'removedField'.
     * In V2, 'removedField' is removed.
     * Accessing it on a V1-compiled class against V2 library results in NoSuchFieldError.
     */
    @Test
    public void testFieldRemoved() {
        FieldRemoved f = new FieldRemoved();
        if (Version.isV1()) {
            assertEquals("removed", f.removedField);
        } else {
            try {
                assertEquals("removed", f.removedField);
                fail("NoSuchFieldError expected");
            } catch (NoSuchFieldError e) {
                // Expected
            }
        }
    }
}
