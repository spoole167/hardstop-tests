import dev.gruff.hardstop.testcases.apicheck.MovedClass;
import dev.gruff.hardstop.testcases.apicheck.Version;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests changing the package of a class.
 * Matches spec scenario A.28: Package changed.
 * This changes the fully qualified name and is binary and source incompatible.
 * Results in NoClassDefFoundError at runtime.
 */
public class TestPackageChanged {

    /**
     * In V1, MovedClass is in dev.gruff.hardstop.testcases.apicheck.
     * In V2, it is moved to dev.gruff.hardstop.testcases.apicheck.other.
     * Code compiled against V1 will fail with NoClassDefFoundError at runtime against V2.
     */
    @Test
    public void testPackageChanged() {
        if (Version.isV1()) {
            MovedClass.callme();
        } else {
            try {
                MovedClass.callme();
                fail("NoClassDefFoundError expected");
            } catch (NoClassDefFoundError e) {
                assertEquals("dev/gruff/hardstop/testcases/apicheck/MovedClass", e.getMessage());
            }
        }
    }
}
