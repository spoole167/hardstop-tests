import dev.gruff.hardstop.testcases.apicheck.ClassToEnum;
import dev.gruff.hardstop.testcases.apicheck.SubEnum;
import dev.gruff.hardstop.testcases.apicheck.Version;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests changing a class to an enum.
 * Matches spec scenario B.41: class -> enum.
 * This is binary and source incompatible.
 * Results in IncompatibleClassChangeError at runtime if subclassed.
 */
public class TestClassToEnum {

    /**
     * In V1, ClassToEnum is a regular class.
     * In V2, it is an enum.
     * Instantiating a subclass compiled against V1 results in IncompatibleClassChangeError
     * because enums cannot be subclassed (they are effectively final).
     */
    @Test
    public void testClassToEnum() {
        if (Version.isV1()) {
            assertNotNull(new SubEnum());
        } else {
            try {
                assertNotNull(new SubEnum());
                fail("IncompatibleClassChangeError expected");
            } catch (IncompatibleClassChangeError | VerifyError e) {
                // Expected
            }
        }
    }
}
