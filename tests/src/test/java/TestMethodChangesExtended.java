import dev.gruff.hardstop.testcases.apicheck.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests various method-level changes.
 * Matches spec scenarios D.65, D.66, D.68, D.69, D.70, D.72.
 */
public class TestMethodChangesExtended {

    /**
     * Matches D.65: Method renamed.
     * Results in NoSuchMethodError.
     */
    @Test
    public void testMethodRenamed() {
        RenamedMethod m = new RenamedMethod();
        if (Version.isV1()) {
            m.oldName();
        } else {
            try {
                m.oldName();
                fail("NoSuchMethodError expected");
            } catch (NoSuchMethodError e) {
                // Expected
            }
        }
    }

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
     * Matches D.68: Method parameters changed.
     * Results in NoSuchMethodError.
     */
    @Test
    public void testMethodParametersChanged() {
        MethodParams m = new MethodParams();
        if (Version.isV1()) {
            m.callme("test");
        } else {
            try {
                m.callme("test");
                fail("NoSuchMethodError expected");
            } catch (NoSuchMethodError e) {
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
     * In V1, we can instantiate and call. In V2, instantiation fails if done reflectively,
     * or calling the method might fail if the class was already instantiated (which is hard here).
     * Actually, the class itself was made abstract too in V2 to be valid.
     * Results in InstantiationError or similar.
     */
    @Test
    public void testMethodMadeAbstract() {
        if (Version.isV1()) {
            new MethodMadeAbstract().callme();
        } else {
            try {
                new MethodMadeAbstract().callme();
                fail("InstantiationError expected");
            } catch (InstantiationError e) {
                // Expected
            }
        }
    }

    /**
     * Matches D.72: Checked exceptions widened.
     * This is a source incompatibility, but let's see how it behaves.
     * Since the test is compiled against V1, it catches IOException.
     * If run against V2, and if the method were to throw something else (not possible with empty body),
     * it might not be caught.
     * But the spec says Phase C (Compile), so it's mainly about source.
     */
    @Test
    public void testCheckedExceptionsWidened() {
        MethodExceptions m = new MethodExceptions();
        try {
            m.callme();
        } catch (IOException e) {
            // Expected
        }
    }
}
