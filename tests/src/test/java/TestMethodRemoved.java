import dev.gruff.hardstop.testcases.apicheck.MethodRemoved;
import dev.gruff.hardstop.testcases.apicheck.Version;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests removing a method from a class.
 * Matches spec scenario D.64: Method removed.
 * Results in NoSuchMethodError at runtime.
 */
public class TestMethodRemoved {

    /**
     * In V1, MethodRemoved has 'removedMethod'.
     * In V2, 'removedMethod' is removed.
     * Accessing it on a V1-compiled class against V2 library results in NoSuchMethodError.
     */
    @Test
    public void testMethodRemoved() {
        MethodRemoved m = new MethodRemoved();
        if (Version.isV1()) {
            m.removedMethod();
        } else {
            try {
                m.removedMethod();
                fail("NoSuchMethodError expected");
            } catch (NoSuchMethodError e) {
                // Expected
            }
        }
    }
}
