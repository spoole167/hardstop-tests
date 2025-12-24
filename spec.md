# Java Compatibility Failure Rules (Java 21+)

This document defines **normative compatibility rules** describing how changes to Java classes, interfaces, and hierarchies result in **compile-time** or **runtime** failures.

The rules are organised along three axes:

* **Failure Phase**
    * **C** – Recompilation failure
    * **R** – Runtime failure (linkage or execution)

* **Certainty**
    * **A** – Always fails
    * **D** – Depends on downstream usage

* **Inheritance Shielding**
    * **None** – No shielding possible
    * **Partial** – Shielded only under strict conditions
    * **Full** – Inheritance fully absorbs the change

---

## A. Type & Module Identity

| Change | Phase | Certainty | Shielding | Test | Notes |
|------|------|----------|-----------|------|------|
| Class removed | C | A | None | `TestClassRenamed` | Symbol resolution failure |
| Class renamed | C | A | None | `TestClassRenamed` | Binary and source incompatible |
| Package changed | C | A | None | `TestPackageChanged` | Fully qualified name changes |
| Class made non-public | C | A | None | `TestInnerClassReducedAccess` | Accessibility violation |
| Package no longer exported (JPMS) | C | A | None | | Module boundary enforced |
| Module renamed | C | A | None | | Requires recompilation |

---

## B. Class Kind & Hierarchy

| Change | Phase | Certainty | Shielding | Test | Notes |
|------|------|----------|-----------|------|------|
| `class` ↔ `interface` | C | A | None | `TestClassIsAClass`, `TestAbstractClassToInterface` | Semantic redefinition |
| `class` → `record` | C | A | None | `TestClassToRecord` | Implicit finality |
| `class` → `enum` | C | A | None | `TestClassToEnum` | Construction semantics |
| Class made `final` | C | D | None | `TestParentGoesFinal` | Fails only if subclassed |
| Class made `sealed` | C | D | None | `TestMoreHierarchyChanges` | Fails if subclasses not permitted |
| Subclass removed from `permits` | R | A | None | `TestSealedLosesPermit` | JVM verifier failure |

---

## C. Constructors

| Change | Phase | Certainty | Shielding | Test | Notes |
|------|------|----------|-----------|------|------|
| Constructor removed | C | A | None | `TestInnerGoesStatic` | Instantiation fails |
| Constructor visibility reduced | C | A | None | `TestConstructorChanges` | Access violation |
| Constructor signature changed | C | A | None | `TestInnerGoesStatic` | Call-site mismatch |
| No-arg constructor removed | C | D | None | `TestConstructorChanges` | Fails if reflectively required |
| Record canonical constructor changed | C/R | A | None | `TestRecordGainsField` | Structural incompatibility |

---

## D. Methods

| Change | Phase | Certainty | Shielding | Test | Notes |
|------|------|----------|-----------|------|------|
| Method removed | C/R | A | None | `TestMethodRemoved` | `NoSuchMethodError` |
| Method renamed | C | A | None | `TestMethodChangesExtended` | Symbol loss |
| Method visibility reduced | C | A | None | `TestMethodChangesExtended` | Access check |
| Method return type changed | C/R | A | None | `TestMethodChange` | Descriptor change |
| Method parameters changed | C/R | A | None | `TestMethodChangesExtended` | Descriptor change |
| Instance ↔ static change | C/R | A | None | `TestMethodChangesExtended` | Binding mode change |
| Method made abstract | C/R | D | Partial | `TestMethodChangesExtended` | Concrete subclasses fail |
| Method moved to superclass | C/R | D | Partial | `TestMethodMoves` | Safe only if visible and identical |
| Checked exceptions widened | C | A | None | `TestMethodChangesExtended` | Compile-time rule |

---

## E. Fields

| Change | Phase | Certainty | Shielding | Test | Notes |
|------|------|----------|-----------|------|------|
| Field removed | C/R | A | None | `TestFieldRemoved` | `NoSuchFieldError` |
| Field renamed | C | A | None | `TestFieldChanges` | Static binding |
| Field visibility reduced | C | A | None | `TestFieldChanges` | Access check |
| Field type changed | C/R | A | None | `TestFieldChanges` | Descriptor mismatch |
| Instance ↔ static change | C/R | A | None | `TestFieldChanges` | Binding change |
| `static final` constant value changed | R | D | None | `TestStaticFieldValue` | Inlining risk |

---

## F. Interfaces & Abstract Classes

| Change | Phase | Certainty | Shielding | Test | Notes |
|------|------|----------|-----------|------|------|
| Interface adds abstract method | C/R | A | None | `TestInterfaceEvolution` | All implementors break |
| Interface adds default method | – | – | Full | | Safe evolution |
| Abstract class adds abstract method | C/R | D | Partial | `TestInterfaceEvolution` | Concrete subclasses fail |
| Abstract class adds interface | C/R | D | Partial | `TestInterfaceEvolution` | Depends on interface methods |
| Abstract class implements interface methods | – | – | Full | | Obligations absorbed |

---

## G. Sealed Types & Pattern Matching

| Change | Phase | Certainty | Shielding | Test | Notes |
|------|------|----------|-----------|------|------|
| Sealed hierarchy tightened | C/R | D | None | `TestSealedLosesPermit` | Usage dependent |
| Record component removed | C/R | D | None | `TestRecordEvolution` | Pattern usage dependent |
| Switch exhaustiveness invalidated | C | D | None | `TestSwitchEvolution` | Pattern matching only |

---

## H. Runtime-Only Failures

| Change | Phase | Certainty | Shielding | Test | Notes |
|------|------|----------|-----------|------|------|
| Abstract method invoked via interface | R | A | None | `TestRuntimeFailures` | `AbstractMethodError` |
| Method resolution ambiguity | R | D | Partial | | Call-site dependent |
| Reflection access removed | R | D | None | `TestRuntimeFailures` | Framework sensitive |
| JPMS `opens` removed | R | D | None | | Reflection only |
| Serialization incompatibility | R | D | None | `TestRuntimeFailures` | Object-stream dependent |

---

## I. Superclass Hierarchy Changes

| Change | Phase | Certainty | Shielding | Test | Notes |
|------|------|----------|-----------|------|------|
| Superclass removed (`extends A` → `Object`) | C/R | D | None | `TestHierarchyChanges` | Inherited members lost |
| Superclass replaced (`extends A` → `extends B`) | C/R | A | None | `TestHierarchyChanges` | Type contract changes |
| Superclass inserted into hierarchy | C/R | D | Partial | `TestHierarchyChanges` | Safe only if transparent |
| Superclass made abstract | C/R | D | Partial | `TestMoreHierarchyChanges` | Concrete subclasses may fail |
| Superclass made concrete | – | – | Full | | Safe |
| Superclass visibility reduced | C | A | None | `TestMoreHierarchyChanges` | Access failure |
| Superclass moved to unexported module | C | A | None | | JPMS boundary |
| Inherited method removed | C/R | A | None | `TestHierarchyChanges` | Resolution failure |
| Inherited method signature changed | C/R | A | None | `TestHierarchyChanges` | Descriptor mismatch |
| Inherited method becomes final | C/R | D | None | `TestHierarchyChanges` | Overriding subclasses fail |
| Field shadowed by superclass field | R | D | None | `TestHierarchyChanges` | Static binding change |
| Default method superseded by superclass method | R | D | Partial | | Resolution rules apply |
| Core `Object` methods overridden differently | R | D | Partial | | Behavioural impact |

---

## Normative Rules

1. **Always-fail changes SHALL be treated as incompatible** under all compatibility profiles.
2. **Depends-on changes MAY be permitted** only under explicitly defined compatibility modes.
3. **Inheritance shielding applies only when**:
    * Erased signatures are unchanged
    * Visibility is preserved
    * No new abstract obligations are introduced
    * No sealing or finality rules are violated
4. **Runtime-only failures MUST be considered high-risk**, as they evade compile-time detection.

---

_End of document_
