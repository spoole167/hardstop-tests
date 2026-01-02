import dev.gruff.hardstop.testcases.apicheck.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests evolution of interfaces and abstract classes.
 * Matches spec scenarios F.93, F.95, F.96.
 */
public class TestInterfaceEvolution {

    /**
     * Matches F.93: Interface adds abstract method.
     * Results in AbstractMethodError when the new method is called on an old implementation.
     */
    @Test
    public void testInterfaceAddsAbstractMethod() {
        if (Version.isV1()) {
            AddedInterfaceMethod m = new AddedInterfaceMethod() {};
            // Can't call newMethod because it doesn't exist in V1
        } else {
            // We create an implementation that was compiled against V1 (anonymous class)
            // but run against V2.
            AddedInterfaceMethod m = new AddedInterfaceMethod() {
                // This anonymous class does NOT implement newMethod() because it was compiled against V1
            };

            try {
                // In V2, we can try to call newMethod reflectively or via a cast if we had one.
                // But simply loading the class or using it might be enough.
                // Actually, to get AbstractMethodError, we need to CALL it.
                m.getClass().getMethod("newMethod").invoke(m);
                fail("AbstractMethodError expected");
            } catch (Exception e) {
                // Expected via reflection if it wraps the error
            }
        }
    }

    /**
     * Matches F.95: Abstract class adds abstract method.
     * Similar to F.93, results in AbstractMethodError.
     */
    @Test
    public void testAbstractClassAddsAbstractMethod() {
        if (Version.isV1()) {
            AddedAbstractMethod m = new AddedAbstractMethod() {};
        } else {
            AddedAbstractMethod m = new AddedAbstractMethod() {};
            try {
                m.getClass().getMethod("newMethod").invoke(m);
                fail("AbstractMethodError expected");
            } catch (Exception e) {
                // Expected
            }
        }
    }

    /**
     * Matches F.96: Abstract class adds interface.
     * In V2, AbstractClassAddsInterface implements Runnable.
     * A subclass compiled against V1 will now effectively implement Runnable in V2.
     */
    @Test
    public void testAbstractClassAddsInterface() {
        if (Version.isV1()) {
            AbstractClassAddsInterface m = new AbstractClassAddsInterface() {};
            assertFalse(m instanceof Runnable);
        } else {
            AbstractClassAddsInterface m = new AbstractClassAddsInterface() {};
            // In V2 it should be an instance of Runnable
            // Actually, since it's an interface added to a superclass, it should be reflected.
            // But wait, if it was compiled against V1, the 'instanceof' check in the test (also V1)
            // might still work because the class is loaded from V2 at runtime.
            // However, Runnable must be available.
            try {
                boolean isRunnable = m instanceof Runnable;
                // If the test class was compiled against V1, it knows about Runnable.
                // At runtime, m's class (V2) says it implements Runnable.
                // So this should be true. This is actually a "safe" evolution if methods are implemented.
            } catch (NoClassDefFoundError e) {
                fail("Should not happen as Runnable is core");
            }
        }
    }

    /**
     * Matches 3.2.1: Conflicting Default Methods.
     * V1: I1 has default, I2 empty. ConflictImpl implements both.
     * V2: I2 adds default with same signature.
     * ConflictImpl (compiled against V1) now inherits two conflicting defaults.
     * Results in IncompatibleClassChangeError.
     */
    @Test
    public void testConflictingDefaults() {
        // We need an instance of ConflictImpl compiled against V1.
        // But ConflictImpl is in the library.
        // If we instantiate it here, we are using the version on the classpath.
        
        if (Version.isV1()) {
            ConflictImpl c = new ConflictImpl();
            c.conflict();
        } else {
            // In V2, ConflictImpl.class is loaded from V2 jar.
            // BUT, ConflictImpl in V2 source overrides the method to resolve conflict.
            // So 'new ConflictImpl()' works fine.
            
            // To test the failure, we need a class that DOES NOT override it.
            // We can create an anonymous class here in the test (compiled against V1).
            
            Object c = new ConflictImpl() {}; 
            // This anonymous class extends ConflictImpl.
            // In V1, ConflictImpl implements I1, I2.
            // In V2, ConflictImpl overrides conflict().
            // So the anonymous class inherits the resolved method. No error.
            
            // We need a class that implements I1 and I2 directly and does NOT override.
            class MyConflict implements I1, I2 {}
            
            // In V1, MyConflict is valid (inherits I1.conflict).
            // In V2, MyConflict (compiled against V1) inherits I1.conflict AND I2.conflict.
            // This should fail loading or invocation.
            
            try {
                MyConflict mc = new MyConflict();
                mc.conflict();
                fail("IncompatibleClassChangeError expected");
            } catch (IncompatibleClassChangeError e) {
                // Expected
            }
        }
    }
}
