import dev.gruff.hardstop.testcases.apicheck.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests method signature changes (Name, Parameters, Exceptions).
 * Extracted from TestMethodChangesExtended.
 */
public class TestMethodSignatures {

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
     * Matches D.72: Checked exceptions widened.
     */
    @Test
    public void testCheckedExceptionsWidened() {
        MethodExceptions m = new MethodExceptions();
        try {
            m.callme();
        } catch (IOException e) {
            // Expected
        } catch (Exception e) {
            // In V2 it throws Exception, but V1 code catches IOException.
            // If V2 throws a non-IOException, it would bubble up.
        }
    }
}
