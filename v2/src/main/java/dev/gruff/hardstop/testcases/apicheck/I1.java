package dev.gruff.hardstop.testcases.apicheck;

public non-sealed interface I1 extends SealedInterfaceLosesPermit {
    default String conflict() { return "I1"; }
}
