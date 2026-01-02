module dev.gruff.jpms.tests {
    requires dev.gruff.jpms;
    requires org.junit.jupiter.api;
    
    // We need to open the test package to JUnit so it can find tests
    opens dev.gruff.jpms.tests to org.junit.platform.commons;
}
