import dev.gruff.hardstop.testcases.apicheck.MethodMoves;
import dev.gruff.hardstop.testcases.apicheck.Version;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests moving a method from a parent class to a child class.
 * Matches spec scenario D.71: Method moved in hierarchy.
 * This is generally safe for binary compatibility if the child's call site still resolves.
 */
public class TestMethodMoves {


    /**
     * In V1, 'callme' is in Parent.
     * In V2, 'callme' is moved to Child.
     * Invoking 'callme' on a Child instance remains compatible as the name and descriptor match.
     */
    @Test
    public void testMovesParentToChild() {

        if(Version.isV1()) {
            MethodMoves.Child c=new MethodMoves.Child();
            c.callme();
        } else {
            MethodMoves.Child c=new MethodMoves.Child();
            c.callme();
        }
    }
}
