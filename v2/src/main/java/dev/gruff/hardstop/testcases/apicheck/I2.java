package dev.gruff.hardstop.testcases.apicheck;

public interface I2 {
    // Adds conflicting default method in V2
    default String conflict() { return "I2"; }
}
