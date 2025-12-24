import dev.gruff.hardstop.testcases.apicheck.ClassToRecord;
import dev.gruff.hardstop.testcases.apicheck.SubRecord;
import dev.gruff.hardstop.testcases.apicheck.Version;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests changing a class to a record.
 * Matches spec scenario B.40: class -> record.
 * This is binary and source incompatible due to implicit finality and structural changes.
 * Results in IncompatibleClassChangeError at runtime if subclassed.
 */
public class TestClassToRecord {

    /**
     * In V1, ClassToRecord is a regular class.
     * In V2, it is a record.
     * Instantiating a subclass compiled against V1 results in IncompatibleClassChangeError
     * because records are final.
     */
    @Test
    public void testClassToRecord() {
        if (Version.isV1()) {
            SubRecord c = new SubRecord("test");
            assertEquals("test", c.name());
        } else {
            try {
                SubRecord c = new SubRecord("test");
                fail("IncompatibleClassChangeError expected");
            } catch (IncompatibleClassChangeError | VerifyError e) {
                // Expected
            }
        }
    }
}
