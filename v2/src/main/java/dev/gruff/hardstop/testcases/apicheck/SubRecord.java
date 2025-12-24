package dev.gruff.hardstop.testcases.apicheck;

// In V2, ClassToRecord is a record, so it cannot be extended.
// This class would fail to compile in V2.
// But we only need it to exist in V1 for the test.
// However, to make the project compile in V2, we might need a dummy or just not include it in V2 source?
// The test harness compiles tests against V1.
// The V2 library is just a dependency.
// So SubRecord doesn't necessarily need to be in V2 source if it's not part of the library API itself but a test helper.
// But if it IS part of the library, it would be removed or changed.
// Let's assume it's a test helper class in the library for now, or just omitted in V2.
// If omitted, we can't load it from V2 jar.
// But the test creates an instance of SubRecord. SubRecord is in the test classpath (from V1).
// So when it loads, it tries to load its superclass ClassToRecord from V2.
// So SubRecord doesn't need to be in V2.
public class SubRecord {}
