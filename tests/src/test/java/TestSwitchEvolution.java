import dev.gruff.hardstop.testcases.apicheck.ExhaustiveSwitch;
import dev.gruff.hardstop.testcases.apicheck.Version;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests evolution of enums and its impact on switches.
 * Matches spec scenario G.107: Switch exhaustiveness invalidated.
 */
public class TestSwitchEvolution {

    @Test
    public void testExhaustiveSwitch() {
        if (Version.isV1()) {
            assertEquals("A", check(ExhaustiveSwitch.A));
            assertEquals("B", check(ExhaustiveSwitch.B));
        } else {
            assertEquals("A", check(ExhaustiveSwitch.A));
            assertEquals("B", check(ExhaustiveSwitch.B));
            // In V2, C exists. If we pass C, and the switch was compiled in V1
            // without a default, it might behave unexpectedly or throw an error
            // depending on the switch type.
            // Traditional switch on enum might just fall through if not for default.
            // Pattern matching switch throws MatchException.
        }
    }

    private String check(ExhaustiveSwitch s) {
        switch (s) {
            case A: return "A";
            case B: return "B";
        }
        return "DEFAULT";
    }
}
