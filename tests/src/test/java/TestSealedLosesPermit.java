import dev.gruff.hardstop.testcases.apicheck.I2;
import dev.gruff.hardstop.testcases.apicheck.SealedInterfaceLosesPermit;
import dev.gruff.hardstop.testcases.apicheck.Version;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests removing a subclass from a sealed permits list.
 * Matches spec scenario B.44: Subclass removed from permits.
 * In V2, I2 no longer extends SealedInterfaceLosesPermit and is not in its permits list.
 * Code compiled against V1 expects I2 to be a subtype.
 */
public class TestSealedLosesPermit {

    @Test
    public void testSealedLosesPermit() {
        if (Version.isV1()) {
            // In V1, we can create an anonymous implementation of I2 and assign it
            I2 i2 = new I2() {};
            SealedInterfaceLosesPermit s = i2;
            assertNotNull(s);
        } else {
            try {
                // In V2, I2 no longer extends SealedInterfaceLosesPermit.
                I2 i2 = new I2() {};
                
                // Verify I2 definition at runtime
                boolean i2ExtendsSealed = SealedInterfaceLosesPermit.class.isAssignableFrom(I2.class);
                if (i2ExtendsSealed) {
                     fail("I2 should not extend SealedInterfaceLosesPermit in V2. Interfaces: " + Arrays.toString(I2.class.getInterfaces()));
                }

                // Verify instance
                if (i2 instanceof SealedInterfaceLosesPermit) {
                    fail("Instance should not be SealedInterfaceLosesPermit. Instance class: " + i2.getClass().getName() + ", Interfaces: " + Arrays.toString(i2.getClass().getInterfaces()));
                }

                // The cast is necessary because in V1 I2 extends SealedInterfaceLosesPermit
                // but in V2 it does not.
                // IMPORTANT: Since we compile against V1, a direct cast (SealedInterfaceLosesPermit) i2
                // is seen as an UPCAST (safe), so javac does NOT emit a checkcast instruction.
                // To test the runtime failure, we must force a checkcast by casting to Object first.
                SealedInterfaceLosesPermit s = (SealedInterfaceLosesPermit) (Object) i2;

                fail("IncompatibleClassChangeError or ClassCastException expected");
            } catch (IncompatibleClassChangeError | ClassCastException e) {
                // Expected
            }
        }
    }
}
