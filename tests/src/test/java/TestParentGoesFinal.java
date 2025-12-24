import dev.gruff.hardstop.testcases.apicheck.ParentGoesFinal;
import dev.gruff.hardstop.testcases.apicheck.Version;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests making a class final.
 * Matches spec scenario B.42: Class made final.
 * Fails at runtime with IncompatibleClassChangeError if the class is subclassed.
 */
public class TestParentGoesFinal {

    /**
     * In V1, ParentGoesFinal is not final.
     * In V2, it is made final.
     * An anonymous class extending it in code compiled against V1 will fail at runtime against V2.
     */
    @Test
    public void test() {

        if(Version.isV1()) {
            ParentGoesFinal p=new ParentGoesFinal(){};
        }
         else {
             try {
                 ParentGoesFinal p = new ParentGoesFinal() {};
                 fail("exception expected");

             } catch(IncompatibleClassChangeError icce) {
                 assertEquals("class TestParentGoesFinal$2 cannot inherit from final class dev.gruff.hardstop.testcases.apicheck.ParentGoesFinal",icce.getMessage());
             }
        }
    }
}
