package dev.gruff.hardstop.testcases.apicheck;

public sealed class SealedEvolution permits OtherSubSealed {}
final class OtherSubSealed extends SealedEvolution {}
