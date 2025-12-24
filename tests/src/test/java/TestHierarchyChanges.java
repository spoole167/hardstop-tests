import dev.gruff.hardstop.testcases.apicheck.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests changes in the superclass hierarchy.
 * Matches spec scenarios I.127, I.128, I.129, I.134, I.135, I.136, I.137.
 */
public class TestHierarchyChanges {

    /**
     * Matches I.127: Superclass removed.
     * In V2, SuperclassRemoved no longer extends BaseA.
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
            // In V2 it extends BaseB, but since BaseB is package-private we can't check it here easily
        }
    }

    /**
     * Matches I.129: Superclass inserted.
     * This is generally safe.
     */
    @Test
    public void testSuperclassInserted() {
        Object obj = HierarchyChanges.getInserted();
        assertTrue(obj instanceof BaseA);
    }

    /**
     * Matches I.134: Inherited method removed.
     */
    @Test
    public void testInheritedMethodRemoved() {
        SubC c = new SubC();
        if (Version.isV1()) {
            c.toRemove();
        } else {
            try {
                c.toRemove();
                fail("NoSuchMethodError expected");
            } catch (NoSuchMethodError e) {
                // Expected
            }
        }
    }

    /**
     * Matches I.135: Inherited method signature changed.
     */
    @Test
    public void testInheritedMethodSignatureChanged() {
        SubC c = new SubC();
        if (Version.isV1()) {
            c.toChange(1);
        } else {
            try {
                c.toChange(1);
                fail("NoSuchMethodError expected");
            } catch (NoSuchMethodError e) {
                // Expected
            }
        }
    }

    /**
     * Matches I.136: Inherited method becomes final.
     * SubFinal overrides it in V1. In V2, BaseA makes it final.
     * Results in VerifyError or IncompatibleClassChangeError when loading SubFinal.
     */
    @Test
    public void testInheritedMethodBecomesFinal() {
        if (Version.isV1()) {
            new SubFinal().toBecomeFinal();
        } else {
            try {
                new SubFinal().toBecomeFinal();
                fail("VerifyError or IncompatibleClassChangeError expected");
            } catch (VerifyError | IncompatibleClassChangeError e) {
                // Expected
            }
        }
    }

    /**
     * Matches I.137: Field shadowed by superclass field.
     * In V1, shadowed is only in SubC. In V2, it's also in BaseA.
     */
    @Test
    public void testFieldShadowedBySuperclassField() throws Exception {
        SubC c = new SubC();
        assertEquals("C", c.shadowed);
        
        if (!Version.isV1()) {
            // If we cast to BaseA, we see BaseA's field in V2
            // Since BaseA.shadowed doesn't exist in V1 (test compilation), we use reflection
            BaseA a = c;
            java.lang.reflect.Field f = BaseA.class.getField("shadowed");
            assertEquals("A", f.get(a));
        }
    }
}
