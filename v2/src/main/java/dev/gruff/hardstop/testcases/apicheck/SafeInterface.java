package dev.gruff.hardstop.testcases.apicheck;

public interface SafeInterface {
    default String safeMethod() { return "safe"; }
}
