module dev.gruff.jpms {
    exports dev.gruff.jpms;
    exports dev.gruff.jpms.opens; // Must export it so we can access the class to reflect on it
    opens dev.gruff.jpms.opens;   // Allows deep reflection
}
