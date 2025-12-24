package dev.gruff.hardstop.testcases.apicheck;

public class BaseA {
    public void foo() {}
    // toRemove is gone
    public void toChange(String s) {}
    public final void toBecomeFinal() {}
    public String shadowed = "A";
}
