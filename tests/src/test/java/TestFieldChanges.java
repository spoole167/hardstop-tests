import dev.gruff.hardstop.testcases.apicheck.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests various field-level changes.
 * Matches spec scenarios E.81, E.82, E.83, E.84, and Field Moved to Superclass.
 */
public class TestFieldChanges {

    /**
     * Matches E.81: Field renamed.
     * Results in NoSuchFieldError.
     */
    @Test
    public void testFieldRenamed() {
        RenamedField f = new RenamedField();
        if (Version.isV1()) {
            assertEquals("old", f.oldName);
        } else {
            try {
                assertEquals("old", f.oldName);
                fail("NoSuchFieldError expected");
            } catch (NoSuchFieldError e) {
                // Expected
            }
        }
    }

    /**
     * Matches E.82: Field visibility reduced.
     * Results in IllegalAccessError.
     */
    @Test
    public void testFieldVisibilityReduced() {
        FieldVisibility f = new FieldVisibility();
        if (Version.isV1()) {
            assertEquals("visible", f.field);
        } else {
            try {
                assertEquals("visible", f.field);
                fail("IllegalAccessError expected");
            } catch (IllegalAccessError e) {
                // Expected
            }
        }
    }

    /**
     * Matches E.83: Field type changed.
     * Results in NoSuchFieldError (descriptor mismatch).
     */
    @Test
    public void testFieldTypeChanged() {
        FieldTypeChange f = new FieldTypeChange();
        if (Version.isV1()) {
            assertEquals(1, f.field);
        } else {
            try {
                // Accessing f.field (Integer) will fail because it's now String
                Object o = f.field;
                fail("NoSuchFieldError expected");
            } catch (NoSuchFieldError e) {
                // Expected
            }
        }
    }

    /**
     * Matches E.84: Instance to Static field change.
     * Results in IncompatibleClassChangeError.
     */
    @Test
    public void testFieldInstanceToStatic() {
        FieldInstanceToStatic f = new FieldInstanceToStatic();
        if (Version.isV1()) {
            assertEquals("instance", f.field);
        } else {
            try {
                assertEquals("instance", f.field);
                fail("IncompatibleClassChangeError expected");
            } catch (IncompatibleClassChangeError e) {
                // Expected
            }
        }
    }

    /**
     * Matches Field moved to superclass (Section E).
     * This is BINARY COMPATIBLE because JVM field resolution searches superclasses recursively.
     * See JVMS 5.4.3.2.
     */
    @Test
    public void testFieldMovedToSuperclass() {
        FieldMovedChild c = new FieldMovedChild();
        // This should succeed in both V1 and V2
        assertEquals("moved", c.movedField);
    }
}
