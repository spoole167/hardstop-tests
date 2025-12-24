package dev.gruff.hardstop.testcases.apicheck;

// In V2, SealedEvolution is sealed and does not permit SubSealed.
// So SubSealed cannot extend it.
// Changing to extend Object to allow compilation.
public class SubSealed {}
