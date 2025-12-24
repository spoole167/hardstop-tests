import dev.gruff.hardstop.testcases.apicheck.RecordComponentRemoved;
import dev.gruff.hardstop.testcases.apicheck.Version;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests evolution of records.
 * Matches spec scenario G.106: Record component removed.
 */
public class TestRecordEvolution {

    @Test
    public void testRecordComponentRemoved() {
        if (Version.isV1()) {
            RecordComponentRemoved r = new RecordComponentRemoved("test", 25);
            assertEquals("test", r.name());
            assertEquals(25, r.age());
        } else {
            try {
                // In V2, 'age' is removed.
                RecordComponentRemoved r = new RecordComponentRemoved("test", 25);
                fail("NoSuchMethodError expected for 2-arg constructor");
            } catch (NoSuchMethodError e) {
                // Expected
            }
        }
    }
}
