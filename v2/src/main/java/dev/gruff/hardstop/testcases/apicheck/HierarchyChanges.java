package dev.gruff.hardstop.testcases.apicheck;

class SuperclassRemoved {}
class SuperclassReplaced extends BaseB {}
class SuperclassInserted extends BaseA {}
class IntermediateB extends BaseA {}

public class HierarchyChanges {
    public static Object getRemoved() { return new SuperclassRemoved(); }
    public static Object getReplaced() { return new SuperclassReplaced(); }
    public static Object getInserted() { return new SuperclassInserted(); }
}
