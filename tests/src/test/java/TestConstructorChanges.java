import dev.gruff.hardstop.testcases.apicheck.ConstructorVisibility;
import dev.gruff.hardstop.testcases.apicheck.NoArgConstructor;
import dev.gruff.hardstop.testcases.apicheck.Version;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests changes to constructors.
 * Matches spec scenarios C.53 and C.55.
 */
public class TestConstructorChanges {

    /**
     * Matches C.53: Constructor visibility reduced.
     * In V1, it's public. In V2, it's private.
     * Results in IllegalAccessError.
     */
    @Test
    public void testConstructorVisibilityReduced() {
        if (Version.isV1()) {
            new ConstructorVisibility();
        } else {
            try {
                new ConstructorVisibility();
                fail("IllegalAccessError expected");
            } catch (IllegalAccessError e) {
                // Expected
            }
        }
    }

    /**
     * Matches C.55: No-arg constructor removed.
     * In V1, it exists. In V2, only a parameterized one exists.
     * Results in NoSuchMethodError.
     */
    @Test
    public void testNoArgConstructorRemoved() {
        if (Version.isV1()) {
            new NoArgConstructor();
        } else {
            try {
                new NoArgConstructor();
                fail("NoSuchMethodError expected");
            } catch (NoSuchMethodError e) {
                // Expected
            }
        }
    }
}
