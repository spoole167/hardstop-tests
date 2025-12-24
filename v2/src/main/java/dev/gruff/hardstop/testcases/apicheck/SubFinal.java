package dev.gruff.hardstop.testcases.apicheck;

public class SubFinal extends BaseA {
    // In V2, BaseA.toBecomeFinal is final, so we cannot override it in source.
    // Removing the override to allow compilation.
    // @Override
    // public void toBecomeFinal() {}
}
