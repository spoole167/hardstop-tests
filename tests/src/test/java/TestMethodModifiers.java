import dev.gruff.hardstop.testcases.apicheck.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests method modifier changes (Visibility, Static/Instance, Abstract).
 * Extracted from TestMethodChangesExtended.
 */
public class TestMethodModifiers {

    /**
     * Matches D.66: Method visibility reduced.
     * Results in IllegalAccessError.
     */
    @Test
    public void testMethodVisibilityReduced() {
        MethodVisibility m = new MethodVisibility();
        if (Version.isV1()) {
            m.callme();
        } else {
            try {
                m.callme();
                fail("IllegalAccessError expected");
            } catch (IllegalAccessError e) {
                // Expected
            }
        }
    }

    /**
     * Matches D.69: Instance to Static change.
     * Results in IncompatibleClassChangeError.
     */
    @Test
    public void testInstanceToStatic() {
        InstanceToStatic m = new InstanceToStatic();
        if (Version.isV1()) {
            m.callme();
        } else {
            try {
                m.callme();
                fail("IncompatibleClassChangeError expected");
            } catch (IncompatibleClassChangeError e) {
                // Expected
            }
        }
    }

    /**
     * Matches D.70: Method made abstract.
     * Results in InstantiationError or AbstractMethodError.
     */
    @Test
    public void testMethodMadeAbstract() {
        if (Version.isV1()) {
            new MethodMadeAbstract().callme();
        } else {
            try {
                new MethodMadeAbstract().callme();
                fail("InstantiationError expected");
            } catch (InstantiationError | AbstractMethodError e) {
                // Expected
            }
        }
    }
}
