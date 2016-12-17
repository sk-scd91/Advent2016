package com.skscd91.advent2016.advents;

import java.io.BufferedReader;
import java.math.BigInteger;
import java.util.stream.Stream;

/**
 * Created by Sean Deneen on 12/16/16.
 * Fill up a 272 bit disk, then calculate its checksum.
 */
public class AdventDay16Part1 implements Advent {

    private static final int SEED = 0b10001110011110000;

    protected BigInteger generateDragonCurve(BigInteger input) {
        BigInteger a = input.shiftLeft(input.bitLength());
        StringBuilder b = new StringBuilder(input.toString(2)).reverse();
        b.setLength(b.length() - 1); // remove marker.
        return a.or(new BigInteger(b.toString(), 2)
                .xor(BigInteger.ZERO.setBit(b.length()).subtract(BigInteger.ONE)));
    }

    protected BigInteger computeChecksum(BigInteger input) {
        BigInteger evenBitMask = BigInteger.ZERO.setBit(input.bitLength() - 1)
                .divide(BigInteger.valueOf(3)); // Every even bit set.
        // Bits are 1 if pairs are same, 0 otherwise.
        BigInteger x = input.xor(input.shiftRight(1)).and(evenBitMask).xor(evenBitMask);
        // Convert 4-ary with [0, 1] to binary, and append marker.
        return new BigInteger(x.toString(4), 2).setBit((input.bitLength() - 1) / 2);
    }

    protected int getLimit() {
        return 272;
    }

    @Override
    public String compute(BufferedReader input) {
        BigInteger initialState = BigInteger.valueOf(SEED | Integer.highestOneBit(SEED) << 1);
        final int limit = getLimit();

        String checksum = calcChecksumString(initialState, limit);

        return "The checksum for the data is " + checksum;
    }

    protected String calcChecksumString(BigInteger initialState, int limit) {
        return Stream.iterate(initialState, this::generateDragonCurve)
                .filter(state -> state.bitLength() > limit)
                .map(state -> state.shiftRight(state.bitLength() - 1 - limit)) // Eliminate all but the first LIMIT bits.
                .findFirst()
                .flatMap(state -> Stream.iterate(computeChecksum(state.setBit(limit)), this::computeChecksum)
                        .filter(check -> check.bitLength() % 2 == 0) // If there is an odd number of bits plus marker.
                        .map(check -> check.toString(2).substring(1)) // Make binary String without marker.
                        .findFirst())
                .orElse("");
    }
}
