import dev.gruff.hardstop.testcases.apicheck.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests "safe" evolution scenarios and complex resolution rules.
 */
public class TestSafeEvolution {

    /**
     * Matches F.94: Interface adds default method.
     * This should be safe.
     */
    @Test
    public void testInterfaceAddsDefaultMethod() {
        SafeInterface s = new SafeInterface() {};
        if (Version.isV1()) {
            // Method doesn't exist
        } else {
            // Method exists and should be callable
            try {
                java.lang.reflect.Method m = s.getClass().getMethod("safeMethod");
                assertEquals("safe", m.invoke(s));
            } catch (Exception e) {
                fail("Should be able to call new default method: " + e);
            }
        }
    }

    /**
     * Matches F.97: Abstract class implements interface methods.
     * This should be safe and absorb the obligation.
     */
    @Test
    public void testAbstractClassImplementsInterfaceMethod() {
        // Anonymous class extending AbstractTask.
        // In V1, AbstractTask doesn't implement run(), so anonymous class MUST implement it (but here we don't, so it would fail compile if we tried to instantiate it directly as a concrete class without body).
        // Wait, if AbstractTask is abstract, we can't instantiate it.
        // We need a concrete subclass.
        
        // Let's use a concrete subclass defined in the test.
        class ConcreteTask extends AbstractTask {
            @Override
            public void run() {
                // In V1, we MUST implement this.
                // In V2, we override the superclass implementation.
            }
        }
        
        ConcreteTask t = new ConcreteTask();
        t.run();
        // This is safe.
    }

    /**
     * Matches I.138: Default method superseded by superclass method.
     */
    @Test
    public void testDefaultMethodSuperseded() {
        SubDef s = new SubDef();
        if (Version.isV1()) {
            assertEquals("def", s.foo());
        } else {
            // In V2, SuperDef has foo(), which wins over DefInterface.foo()
            assertEquals("super", s.foo());
        }
    }
}
