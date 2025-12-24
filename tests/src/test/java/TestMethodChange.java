import dev.gruff.hardstop.testcases.apicheck.MethodChanges;
import dev.gruff.hardstop.testcases.apicheck.Version;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests changing a method's return type.
 * Matches spec scenario D.67: Method return type changed.
 * Changing the return type changes the method descriptor, leading to NoSuchMethodError at runtime.
 */
public class TestMethodChange {

    /**
     * In V1, MethodChanges.returnInteger() returns Integer.
     * In V2, it returns Number.
     * Code compiled against V1 expects a descriptor returning Integer and fails with NoSuchMethodError.
     */
    @Test
    public void testMethodSignatureChange() {
        MethodChanges m=new MethodChanges();
        if(Version.isV1()) {
            Integer i = m.returnInteger();
            assertNotNull(i);
        } else {
            try {
                Integer i = m.returnInteger();
                fail("expected error");
            } catch(NoSuchMethodError nsme) {
                assertEquals("'java.lang.Integer dev.gruff.hardstop.testcases.apicheck.MethodChanges.returnInteger()'",nsme.getMessage());
            }
        }


    }
}
