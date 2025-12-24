package dev.gruff.hardstop.testcases.apicheck;

class SuperclassRemoved extends BaseA {}
class SuperclassReplaced extends BaseA {}
class SuperclassInserted extends BaseA {}

public class HierarchyChanges {
    public static Object getRemoved() { return new SuperclassRemoved(); }
    public static Object getReplaced() { return new SuperclassReplaced(); }
    public static Object getInserted() { return new SuperclassInserted(); }
}
