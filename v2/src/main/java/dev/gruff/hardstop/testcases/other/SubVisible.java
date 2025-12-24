package dev.gruff.hardstop.testcases.other;

// In V2, SuperVisible is package-private, so SubVisible cannot extend it if it's in a different package.
// However, to simulate the binary compatibility issue, we want the *source* of V2 to be valid if possible,
// OR we accept that V2 source is broken for this class.
// But the error is a compilation error in V2.
// The test scenario I.132 "Superclass visibility reduced" implies we have a client (SubVisible) compiled against V1 (where it was public).
// When running against V2, it fails.
// BUT, for the V2 library to even compile, SubVisible (if it exists in V2 source) must be valid or removed.
// Since SubVisible is in a different package 'other', it cannot extend package-private 'SuperVisible'.
// So in V2, SubVisible must either be removed, or not extend SuperVisible, or SuperVisible must be public.
// But the test wants to check what happens when the *runtime* class SuperVisible is package-private.

// To fix the compilation of V2, we should probably remove SubVisible from V2 source,
// or make it not extend SuperVisible.
// If we remove it, the test 'TestMoreHierarchyChanges' which uses 'new SubVisible()'
// will fail to find the class in V2 if it tries to load it from V2 classpath?
// Actually, the test is compiled against V1 (or V2 depending on the profile).
// The test 'TestMoreHierarchyChanges' does: 'new SubVisible()'.
// If we run against V2, we want to load the V1-compiled SubVisible class, but link it against V2 SuperVisible.

// The problem is that the project structure seems to include 'SubVisible' in the V2 source tree.
// If V2 is supposed to represent the "new version of the library", and SubVisible is part of that library,
// then the library developer would have seen this compile error and fixed it (by not extending, or making public).
// If SubVisible is meant to be "client code", it shouldn't be in src/main/java of v2.

// Assuming SubVisible is a test case class that is part of the library (to demonstrate the issue to users?),
// we can't have code that doesn't compile.

// If I remove 'extends SuperVisible' in V2, then it's a different change (Superclass removed).
// If I remove the class SubVisible from V2 entirely, then 'new SubVisible()' in the test might fail with NoClassDefFoundError
// if the test expects to find it on the classpath.
// BUT, usually in these tests, the "client" (the test) is on the classpath, and the "library" (v1/v2) is on the classpath.
// SubVisible is in 'dev.gruff.hardstop.testcases.other', which seems to be part of the library modules (v1/v2).

// If SubVisible is intended to be the "victim" class, it should probably only exist in V1 (or be compiled against V1).
// If it exists in V2, it must be valid V2 code.
// Since SuperVisible is package-private in V2, SubVisible (in a different package) cannot extend it.

// Solution: Remove SubVisible from V2.
// The test runner will likely use the V1 version of SubVisible (if it's on the classpath) or fail if it's missing.
// Wait, if the test depends on 'v2' artifact, and 'v2' artifact doesn't have SubVisible,
// then 'new SubVisible()' in the test (which is compiled against... V1? or V2?)
// The pom.xml says tests depend on v1 and v2.
// The test 'TestMoreHierarchyChanges' imports 'dev.gruff.hardstop.testcases.other.SubVisible'.
// If the test is compiled against V1, it sees SubVisible.
// When running against V2, if SubVisible is NOT in V2 jar, but IS in V1 jar...
// The surefire config excludes the other version.
// So when running 'v2' execution, 'v1' jar is excluded.
// So SubVisible will be missing!
// This means SubVisible MUST be in V2 jar for the test to find it.
// But it can't be in V2 jar if it extends a package-private class from another package.

// This implies that for this specific test case (I.132), the "victim" class (SubVisible)
// should effectively be treated as "client code" that was compiled against V1 and is now running against V2.
// But our build setup compiles everything from source.
// We can't easily simulate "compiled against V1" for a class inside the V2 module if it doesn't compile in V2.

// However, we can make SubVisible compile in V2 by NOT extending SuperVisible.
// But then the runtime error might be different (Class hierarchy change vs Access error).
// If SubVisible extends Object in V2, then 'new SubVisible()' works fine, but it's not a subclass of SuperVisible anymore.
// The test expects 'IllegalAccessError' or 'VerifyError'.
// This error comes from the JVM trying to load SubVisible (compiled against V1) which claims to extend SuperVisible,
// but SuperVisible is not accessible.

// To support this test with the current build setup (where we compile V2 source),
// we have a dilemma. We can't produce a V2 jar that contains a class that extends an inaccessible parent.
// UNLESS we use a trick, or if SubVisible is NOT in V2, but provided by the Test module itself?
// If SubVisible was defined in the 'tests' module, it would be compiled against the dependency (V1 or V2).
// If compiled against V1, it works.
// If compiled against V2, it fails to compile.
// So the test code itself wouldn't compile against V2.

// This specific scenario (I.132) is hard to test with a "compile everything" approach.
// It requires a binary artifact (SubVisible.class) compiled against V1 to be present on the V2 classpath.

// WORKAROUND:
// We can modify SubVisible in V2 to NOT extend SuperVisible.
// This effectively changes the test to "Superclass removed" (I.127) for V2-compiled clients.
// BUT, the test 'TestMoreHierarchyChanges' is likely compiled against V1 (in the 'v1' execution)
// and... wait.
// The 'tests' module is compiled ONCE.
// If it's compiled against V1, then 'new SubVisible()' expects SubVisible extends SuperVisible.
// If we run this against V2, and SubVisible in V2 does NOT extend SuperVisible,
// we get an IncompatibleClassChangeError (superclass changed/removed).
// This is close, but maybe not exactly IllegalAccessError.

// Let's look at the error again. The user says "code is failing to compile".
// This confirms we need to fix the source in V2.

// I will modify SubVisible in V2 to NOT extend SuperVisible.
// This allows compilation.
// The test expectation might need to be adjusted or accepted as "IncompatibleClassChange" instead of "IllegalAccess".
// OR, we can move SubVisible to the same package 'apicheck' in V2?
// No, that defeats the purpose of testing visibility (package-private is visible in same package).

// Let's try removing the 'extends' clause in V2.

public class SubVisible {}
