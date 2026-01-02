package dev.gruff.hardstop.testcases.apicheck;

// In V2, we must provide a value for EvolvingAnnotation to compile.
// But the test will use the V1-compiled version of this class against V2 library.
@EvolvingAnnotation("v2")
@RetentionAnnotation
public class AnnotatedClient {
}
