import dev.gruff.hardstop.testcases.apicheck.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests changes to the superclass hierarchy (Removed, Replaced, Inserted).
 * Extracted from TestHierarchyChanges.
 */
public class TestSuperclassChanges {

    /**
     * Matches I.127: Superclass removed.
     */
    @Test
    public void testSuperclassRemoved() {
        Object obj = HierarchyChanges.getRemoved();
        if (Version.isV1()) {
            assertTrue(obj instanceof BaseA);
            ((BaseA)obj).foo();
        } else {
            assertFalse(obj instanceof BaseA);
            try {
                ((BaseA)obj).foo();
                fail("ClassCastException expected");
            } catch (ClassCastException e) {
                // Expected
            }
        }
    }

    /**
     * Matches I.128: Superclass replaced.
     */
    @Test
    public void testSuperclassReplaced() {
        Object obj = HierarchyChanges.getReplaced();
        if (Version.isV1()) {
            assertTrue(obj instanceof BaseA);
        } else {
            assertFalse(obj instanceof BaseA);
        }
    }

    /**
     * Matches I.129: Superclass inserted.
     */
    @Test
    public void testSuperclassInserted() {
        Object obj = HierarchyChanges.getInserted();
        assertTrue(obj instanceof BaseA);
    }
}
