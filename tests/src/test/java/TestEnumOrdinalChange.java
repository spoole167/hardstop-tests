import dev.gruff.hardstop.testcases.apicheck.EnumOrdinalChange;
import dev.gruff.hardstop.testcases.apicheck.Version;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests the impact of reordering enum constants.
 * This is binary compatible but changes runtime behavior for code relying on ordinal().
 */
public class TestEnumOrdinalChange {

    @Test
    public void testOrdinalChange() {
        if (Version.isV1()) {
            assertEquals(0, EnumOrdinalChange.FIRST.ordinal());
            assertEquals(1, EnumOrdinalChange.SECOND.ordinal());
        } else {
            // In V2, the order is swapped
            assertEquals(1, EnumOrdinalChange.FIRST.ordinal());
            assertEquals(0, EnumOrdinalChange.SECOND.ordinal());
        }
    }
}
