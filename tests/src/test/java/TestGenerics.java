import dev.gruff.hardstop.testcases.apicheck.ErasureDrift;
import dev.gruff.hardstop.testcases.apicheck.HeapPollution;
import dev.gruff.hardstop.testcases.apicheck.Version;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests binary incompatibilities related to Generics and Type Erasure.
 * Matches spec scenarios in Section 4.
 */
public class TestGenerics {

    /**
     * Matches 4.2: Erasure Drift.
     * Changing process(List) to process(Collection) changes the method descriptor.
     * Results in NoSuchMethodError.
     */
    @Test
    public void testErasureDrift() {
        ErasureDrift e = new ErasureDrift();
        if (Version.isV1()) {
            e.process(new ArrayList<>());
        } else {
            try {
                e.process(new ArrayList<>());
                fail("NoSuchMethodError expected");
            } catch (NoSuchMethodError ex) {
                // Expected
            }
        }
    }

    /**
     * Matches 4.4: Heap Pollution.
     * Changing return type from List<String> to List<Integer>.
     * Erasure is identical (List), so linking succeeds.
     * But the client has an implicit checkcast String, which fails at runtime.
     */
    @Test
    public void testHeapPollution() {
        HeapPollution h = new HeapPollution();
        if (Version.isV1()) {
            List<String> list = h.getList();
            String s = list.get(0);
            assertEquals("string", s);
        } else {
            try {
                // In V2, getList() returns List<Integer> containing 123.
                // The compiler (V1) thinks it returns List<String>.
                // The call list.get(0) returns an Object (Integer 123).
                // The assignment to String s triggers checkcast String.
                List<String> list = h.getList();
                String s = list.get(0);
                fail("ClassCastException expected");
            } catch (ClassCastException e) {
                // Expected
            }
        }
    }
}
