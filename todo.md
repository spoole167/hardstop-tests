# Test Coverage Gaps & TODOs

Based on the analysis of `research.md` against the current test suite, the following scenarios are missing test coverage.

## High Priority (Common Incompatibilities)

### 1. Generics & Type Erasure (Research Section 4)
The current test suite lacks coverage for Generics-related binary incompatibilities.
*   **Erasure Drift (4.2):** Test changing a method signature from `process(List<String>)` to `process(Collection<String>)`. The descriptor changes, causing `NoSuchMethodError`.
*   **Missing Bridge Anomaly (4.3):** Test evolving a generic interface or hierarchy such that the compiler-generated bridge methods change or disappear, causing `AbstractMethodError` in legacy clients.
*   **Heap Pollution (4.4):** Test changing return type bounds (e.g., `List<String>` to `List<Object>`) causing `ClassCastException` at runtime in client code.

### 2. Interface Evolution (Research Section 3)
*   **Conflicting Default Methods (3.2.1):** Test a class implementing two interfaces that both add the same default method in V2. Should cause `IncompatibleClassChangeError`.
*   **Static Method Moves (3.3):** Test moving a static method from a class to an interface (or vice versa). The invocation instruction (`invokestatic`) differs, causing `NoSuchMethodError`.

### 3. Field Resolution (Research Section 2.2)
*   **Move to Superclass (2.2.1):** Test moving a field from a child class to a parent class. Unlike methods, field resolution is often strict about the class ref, potentially causing `NoSuchFieldError`.

### 4. Annotations (Research Section 11)
*   **Adding Elements without Defaults (11.1):** Test adding a new method `String region();` to an annotation type in V2. V1 clients loading this annotation should fail with `IncompleteAnnotationException`.
*   **Retention Policy Change (11.2):** Test changing `@Retention` from `RUNTIME` to `CLASS`. V1 clients calling `getAnnotation()` will get `null`.

## Medium Priority (Edge Cases & Modern Features)

### 5. Records (Research Section 6)
*   **JavaBean to Record Migration (6.2):** Test converting a standard POJO with `getName()` to a Record with `name()`. Clients calling `getName()` will fail with `NoSuchMethodError`.

### 6. Pattern Matching & Switch (Research Section 8)
*   **Null Handling Evolution (8.2):** Test behavioral changes when a switch selector evolves from primitive/legacy to pattern-matching capable types regarding `null`.
*   **Guarded Pattern Exceptions (8.3):** Test exceptions thrown from pattern guards crashing the switch execution.

### 7. Methods (Research Section 2.3)
*   **Varargs Evolution (2.3.2):** Test changing `String[]` to `String...` and vice versa. While often binary compatible, it can affect reflection or source compatibility.

## Low Priority / Infrastructure Heavy

### 8. JPMS (Research Section 5)
*   **Split Package (5.1):** Requires creating two modules exporting the same package.
*   **Transitive Dependency Visibility (5.3):** Requires a 3-module setup (A -> B -> C) where B drops `requires transitive C`.

### 9. Nestmates (Research Section 9)
*   **Nest Host Mismatch (9.1):** Hard to simulate with standard compilation; requires manipulating attributes or separate compilation of inner/outer classes in specific ways.

### 10. Project Valhalla (Research Section 10)
*   **Synchronization on Value Class (10.1):** Requires a JDK with Valhalla preview features enabled.
*   **Identity Operations (10.2):** Requires Valhalla preview features.

## Recommended Next Steps

1.  Create `TestGenerics.java` to cover the High Priority Generics gaps.
2.  Create `TestAnnotationEvolution.java` to cover Annotation gaps.
3.  Add `testConflictingDefaults` to `TestInterfaceEvolution.java`.
4.  Add `testFieldMovedToSuperclass` to `TestFieldChanges.java`.
