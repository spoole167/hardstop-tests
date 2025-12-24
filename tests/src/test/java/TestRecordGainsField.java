import dev.gruff.hardstop.testcases.apicheck.RecordGainsField;
import dev.gruff.hardstop.testcases.apicheck.Version;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests adding a field to a record, which changes its canonical constructor.
 * Matches spec scenario C.56: Record canonical constructor changed.
 * This results in a structural incompatibility and NoSuchMethodError if the old constructor is called.
 */
public class TestRecordGainsField {


    /**
     * In V1, RecordGainsField has 2 components.
     * In V2, it has 3 components.
     * Code compiled against V1 calls the 2-argument constructor, which no longer exists in V2
     * (the canonical constructor now takes 3 arguments), resulting in NoSuchMethodError.
     */
    @Test
    public void test() {

        if(Version.isV1()) {
                RecordGainsField r=new RecordGainsField("a",1);
             }
        else {
            try {
                RecordGainsField r=new RecordGainsField("a",1);
               fail("should throw error");

            } catch( NoSuchMethodError nme) {
               ; // expected
            }
        }

    }
}
