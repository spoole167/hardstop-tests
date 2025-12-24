import dev.gruff.hardstop.testcases.apicheck.DroppedRuntimeAnnotation;
import dev.gruff.hardstop.testcases.apicheck.Version;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the removal of an annotation class.
 * Matches spec scenario A.26: Class removed.
 * Results in NoClassDefFoundError if the annotation is accessed at runtime.
 */
public class TestDroppedAnnotation {

    /**
     * In V1, DroppedRuntimeAnnotation exists.
     * In V2, the class is removed entirely.
     * Accessing the annotation on a class that was compiled with it results in NoClassDefFoundError.
     */
    @Test
    public void testDroppedAnnotation() {

        if(Version.isV1()) {
            DroppedRuntimeAnnotation dr = AnnotatedClassWithDroppedAnnotation.class.getAnnotation(DroppedRuntimeAnnotation.class);
            assertNotNull(dr);
        } else {
            try {
                DroppedRuntimeAnnotation dr = AnnotatedClassWithDroppedAnnotation.class.getAnnotation(DroppedRuntimeAnnotation.class);
                fail("exception expected");
            } catch(NoClassDefFoundError e) {
                assertEquals("dev/gruff/hardstop/testcases/apicheck/DroppedRuntimeAnnotation",e.getMessage());
            }
        }
    }
}

@DroppedRuntimeAnnotation
class AnnotatedClassWithDroppedAnnotation {

}
