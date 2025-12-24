import dev.gruff.hardstop.testcases.apicheck.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests changes to inherited members (Methods, Fields).
 * Extracted from TestHierarchyChanges.
 */
public class TestInheritedMemberChanges {

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
     */
    @Test
    public void testFieldShadowedBySuperclassField() throws Exception {
        SubC c = new SubC();
        assertEquals("C", c.shadowed);
        
        if (!Version.isV1()) {
            BaseA a = c;
            java.lang.reflect.Field f = BaseA.class.getField("shadowed");
            assertEquals("A", f.get(a));
        }
    }
}
