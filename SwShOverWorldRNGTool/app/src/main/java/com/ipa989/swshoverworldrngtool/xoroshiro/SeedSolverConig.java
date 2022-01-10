package com.ipa989.swshoverworldrngtool.xoroshiro;


import java.util.OptionalLong;

public class SeedSolverConig {
    public byte[] motions;
    public OptionalLong s0, s1;
//    public FrameConfig frame;
    public int startInclusive, endExclusive;

    public void setMotions(byte[] motions) {
        this.motions = motions;
    }

    public void setStartInclusive(int startInclusive) {
        this.startInclusive = startInclusive;
    }

    public void setEndExclusive(int endExclusive) {
        this.endExclusive = endExclusive;
    }

    public void setS0(String s0) {
        this.s0 = toUnsignedOptionalLong(s0);
    }

    public void setS1(String s1) {
        this.s1 = toUnsignedOptionalLong(s1);
    }

    public static OptionalLong toUnsignedOptionalLong(String s) {
        if (s == null) {
            return OptionalLong.empty();
        } else {
            return OptionalLong.of(Long.parseUnsignedLong(s, 16));
        }
    }

    public SeedSolverConig() {
    }
}

