import dev.gruff.hardstop.testcases.apicheck.InnerClassReducedAccess;
import dev.gruff.hardstop.testcases.apicheck.Version;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests reducing the visibility of an inner class.
 * Matches spec scenario A.29: Class made non-public.
 * Results in IllegalAccessError at runtime.
 */
public class TestInnerClassReducedAccess {


    /**
     * In V1, InnerClassReducedAccess.Inner is public.
     * In V2, it is changed to private (or package-private).
     * Code compiled against V1 will fail with IllegalAccessError when trying to instantiate the now-inaccessible inner class.
     */
    @Test
    public void testInnerClassReducedAccess() {

        if(Version.isV1()) {
            InnerClassReducedAccess ica = new InnerClassReducedAccess();
            InnerClassReducedAccess.Inner i1 = ica.new Inner();
            i1.callme();
        }
        else {
            InnerClassReducedAccess ica = new InnerClassReducedAccess();
            try {
                InnerClassReducedAccess.Inner i1 = ica.new Inner();
                fail("error expected");
            }
            catch(java.lang.IllegalAccessError e) {
                assertEquals("failed to access class dev.gruff.hardstop.testcases.apicheck.InnerClassReducedAccess$Inner from class TestInnerClassReducedAccess (dev.gruff.hardstop.testcases.apicheck.InnerClassReducedAccess$Inner and TestInnerClassReducedAccess are in unnamed module of loader 'app')",e.getMessage());
                }
        }
    }
}
