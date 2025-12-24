import dev.gruff.hardstop.testcases.apicheck.InnerClassGoesStatic;
import dev.gruff.hardstop.testcases.apicheck.Version;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests changing an inner class to a static nested class.
 * Matches spec scenario C.52 (Constructor removed) or C.54 (Constructor signature changed).
 * Non-static inner class constructors have an implicit parameter for the outer class instance.
 * Static nested classes do not have this, changing the binary signature of the constructor.
 */
public class TestInnerGoesStatic {


    /**
     * In V1, InnerClassGoesStatic.Inner is a non-static inner class.
     * In V2, it is changed to static.
     * Instantiating it via 'outer.new Inner()' in code compiled against V1 will fail with NoSuchMethodError
     * because the constructor signature changed from 'Inner(Outer)' to 'Inner()'.
     */
    @Test
    public void testInnerGoesStatic() {

        if(Version.isV1()) {
            InnerClassGoesStatic i = new InnerClassGoesStatic();
            InnerClassGoesStatic.Inner i1 = i.new Inner();
            i1.callme();
        } else {
            InnerClassGoesStatic i = new InnerClassGoesStatic();
            try {
                InnerClassGoesStatic.Inner i1 = i.new Inner();
                fail("exception expected");
            } catch(NoSuchMethodError e) {
                assertEquals("'void dev.gruff.hardstop.testcases.apicheck.InnerClassGoesStatic$Inner.<init>(dev.gruff.hardstop.testcases.apicheck.InnerClassGoesStatic)'",e.getMessage());

            }

        }
    }
}
