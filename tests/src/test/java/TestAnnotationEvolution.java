import dev.gruff.hardstop.testcases.apicheck.EvolvingAnnotation;
import dev.gruff.hardstop.testcases.apicheck.RetentionAnnotation;
import dev.gruff.hardstop.testcases.apicheck.Version;
import org.junit.jupiter.api.Test;

import java.lang.annotation.IncompleteAnnotationException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests binary incompatibilities related to Annotations.
 * Matches spec scenarios in Section 11.
 */
public class TestAnnotationEvolution {

    // Define a local client class.
    // This class is compiled with the Test module.
    // If the Test module is compiled against V1, this class uses @EvolvingAnnotation without arguments.
    // When running against V2, this class is loaded, but the annotation definition in V2 requires an argument.
    @EvolvingAnnotation
    @RetentionAnnotation
    static class LocalAnnotatedClient {}

    /**
     * Matches 11.1: Adding Elements without Defaults.
     * V1 client uses @EvolvingAnnotation (no args).
     * V2 annotation adds 'String value()'.
     * Loading the annotation on V1 client fails with IncompleteAnnotationException.
     */
    @Test
    public void testAddingElementWithoutDefault() {
        if (Version.isV1()) {
            EvolvingAnnotation ann = LocalAnnotatedClient.class.getAnnotation(EvolvingAnnotation.class);
            assertNotNull(ann);
        } else {
            try {
                // In V2, loading the annotation proxy should fail because 'value' is missing
                EvolvingAnnotation ann = LocalAnnotatedClient.class.getAnnotation(EvolvingAnnotation.class);
                
                if (ann != null) {
                    // Use reflection to access the missing element
                    Method valueMethod = EvolvingAnnotation.class.getMethod("value");
                    valueMethod.invoke(ann);
                }
                fail("IncompleteAnnotationException expected");
            } catch (IncompleteAnnotationException e) {
                // Expected
                assertEquals("dev.gruff.hardstop.testcases.apicheck.EvolvingAnnotation", e.annotationType().getName());
                assertEquals("value", e.elementName());
            } catch (Exception e) {
                if (e instanceof java.lang.reflect.InvocationTargetException) {
                    Throwable cause = e.getCause();
                    if (cause instanceof IncompleteAnnotationException) {
                        IncompleteAnnotationException iae = (IncompleteAnnotationException) cause;
                        assertEquals("dev.gruff.hardstop.testcases.apicheck.EvolvingAnnotation", iae.annotationType().getName());
                        assertEquals("value", iae.elementName());
                        return; // Success
                    }
                }
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Matches 11.2: Retention Policy Changes.
     * V1: RUNTIME. V2: CLASS.
     * V1 client expects to see it. V2 client sees null.
     */
    @Test
    public void testRetentionPolicyChange() {
        if (Version.isV1()) {
            RetentionAnnotation ann = LocalAnnotatedClient.class.getAnnotation(RetentionAnnotation.class);
            assertNotNull(ann);
        } else {
            // In V2, retention is CLASS, so getAnnotation returns null.
            RetentionAnnotation ann = LocalAnnotatedClient.class.getAnnotation(RetentionAnnotation.class);
            assertNull(ann, "Annotation should not be visible at runtime in V2");
        }
    }
}
