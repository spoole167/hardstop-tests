import dev.gruff.hardstop.testcases.apicheck.RenamedClass;
import dev.gruff.hardstop.testcases.apicheck.Version;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests renaming a class.
 * Matches spec scenario A.27: Class renamed.
 * This is binary and source incompatible.
 * Results in NoClassDefFoundError at runtime.
 */
public class TestClassRenamed {

    /**
     * In V1, RenamedClass exists.
     * In V2, it is renamed to RenamedClassNew (effectively removed).
     * Code compiled against V1 will fail with NoClassDefFoundError at runtime against V2.
     */
    @Test
    public void testClassRenamed() {
        if (Version.isV1()) {
            RenamedClass.callme();
        } else {
            try {
                RenamedClass.callme();
                fail("NoClassDefFoundError expected");
            } catch (NoClassDefFoundError e) {
                assertEquals("dev/gruff/hardstop/testcases/apicheck/RenamedClass", e.getMessage());
            }
        }
    }
}
