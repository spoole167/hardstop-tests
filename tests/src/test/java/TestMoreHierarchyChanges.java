import dev.gruff.hardstop.testcases.apicheck.*;
import dev.gruff.hardstop.testcases.other.SubVisible;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests additional hierarchy and kind changes.
 * Matches spec scenarios B.43, I.130, I.132.
 */
public class TestMoreHierarchyChanges {

    /**
     * Matches B.43: Class made sealed.
     * SubSealed is not permitted in V2.
     * Results in IncompatibleClassChangeError or VerifyError.
     */
    @Test
    public void testClassMadeSealed() {
        if (Version.isV1()) {
            assertNotNull(new SubSealed());
        } else {
            try {
                new SubSealed();
                fail("IncompatibleClassChangeError or VerifyError expected");
            } catch (IncompatibleClassChangeError | VerifyError e) {
                // Expected
            }
        }
    }

    /**
     * Matches I.130: Superclass made abstract.
     * Instantiation of the class itself fails.
     */
    @Test
    public void testSuperclassMadeAbstract() {
        if (Version.isV1()) {
            assertNotNull(new SuperAbstract());
        } else {
            try {
                new SuperAbstract();
                fail("InstantiationError expected");
            } catch (InstantiationError e) {
                // Expected
            }
        }
    }

    /**
     * Matches I.132: Superclass visibility reduced.
     * SubVisible extends SuperVisible. In V2, SuperVisible is package-private.
     * Results in IllegalAccessError or VerifyError.
     */
    @Test
    public void testSuperclassVisibilityReduced() {
        if (Version.isV1()) {
            assertNotNull(new SubVisible());
        } else {
            try {
                new SubVisible();
                fail("IllegalAccessError or VerifyError expected");
            } catch (IllegalAccessError | VerifyError e) {
                // Expected
            }
        }
    }
}
