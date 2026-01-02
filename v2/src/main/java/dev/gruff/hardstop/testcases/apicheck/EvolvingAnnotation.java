package dev.gruff.hardstop.testcases.apicheck;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface EvolvingAnnotation {
    // V2: Adds a mandatory element
    String value();
}
