package dev.gruff.jpms.tests;

import dev.gruff.jpms.opens.ReflectiveClass;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class TestOpensRemoved {

    @Test
    public void testOpensRemoved() {
        // In V1, dev.gruff.jpms.opens is OPENED. Deep reflection on private members succeeds.
        // In V2, it is NOT OPENED. Deep reflection fails with InaccessibleObjectException.
        
        boolean accessSucceeded = false;
        try {
            Field f = ReflectiveClass.class.getDeclaredField("secret");
            f.setAccessible(true); // This checks 'opens'
            String value = (String) f.get(null);
            assertEquals("secret", value);
            accessSucceeded = true;
        } catch (Exception e) {
            // InaccessibleObjectException is a RuntimeException, but let's catch generic Exception
            if (e.getClass().getName().equals("java.lang.reflect.InaccessibleObjectException")) {
                accessSucceeded = false;
            } else {
                // Other errors (NoSuchField, etc) are failures
                fail("Unexpected exception: " + e);
            }
        }

        // Check module configuration to know what to expect
        Module module = ReflectiveClass.class.getModule();
        boolean isOpen = module.isOpen("dev.gruff.jpms.opens");
        
        if (isOpen) {
            // V1
            if (!accessSucceeded) {
                fail("Package is OPEN but reflection failed!");
            }
        } else {
            // V2
            if (accessSucceeded) {
                fail("Package is NOT OPEN but reflection succeeded!");
            }
        }
    }
}
