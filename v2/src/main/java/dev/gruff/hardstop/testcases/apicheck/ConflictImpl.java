package dev.gruff.hardstop.testcases.apicheck;

public class ConflictImpl implements I1, I2 {
    // In V2, both I1 and I2 have 'conflict()'.
    // To compile this class in V2, we MUST override it.
    // BUT, the test uses the V1-compiled version of this class.
    // So we can leave this as is (if we assume we are simulating the V1 binary).
    // However, to make V2 compile, we must resolve the conflict.
    
    @Override
    public String conflict() {
        return "Resolved";
    }
}
