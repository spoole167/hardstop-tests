import dev.gruff.hardstop.testcases.apicheck.AbstractClassToInterface;
import dev.gruff.hardstop.testcases.apicheck.Version;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests the conversion of an abstract class to an interface.
 * Matches spec scenario B.39: class ↔ interface change.
 * This change is a semantic redefinition and is binary and source incompatible.
 */
public class TestAbstractClassToInterface {


    /**
     * In V1, AbstractClassToInterface is an abstract class.
     * In V2, it is changed to an interface.
     * An anonymous class extending the abstract class in V1 will fail with IncompatibleClassChangeError
     * when run against V2 because it attempts to 'extend' an interface as if it were a class.
     */
    @Test
    public void test() {

        if(Version.isV1()) {
            AbstractClassToInterface a = new AbstractClassToInterface() {
                @Override
                public void callme() {

                }
            };

            a.callme();
        }
        else {
            try {
                AbstractClassToInterface a = new AbstractClassToInterface() {
                    @Override
                    public void callme() {

                    }
                };

                a.callme();
                fail("should throw error");

            } catch( IncompatibleClassChangeError icce) {
               ;
            }
        }

    }
}
