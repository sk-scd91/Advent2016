package com.skscd91.advent2016.advents;

import java.math.BigInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Sean Deneen on 12/16/16.
 * Part 1 with optimizations for larger disk sizes.
 * Uses all strings now, since BigInteger.toString is horrendously SLOW on large numbers.
 */
public class AdventDay16Part2 extends AdventDay16Part1 {

    @Override
    protected int getLimit() {
        return 35651584;
    }

    @Override
    protected String calcChecksumString(BigInteger initialState, int limit) {
        String state = initialState.toString(2).substring(1);

        while(state.length() < limit) {
            StringBuilder lastHalf = new StringBuilder(state.replace('0', ' ')
                    .replace('1', '0')
                    .replace(' ', '1'));
            state += '0' + lastHalf.reverse().toString();
        }

        final String finalState = state;

        int checksumCount = Integer.numberOfTrailingZeros(limit); // The number of iterations before checksum is odd.

        return IntStream.range(0, limit >> checksumCount)
                .map(i -> buildChecksumFragment(finalState, i << checksumCount, 1 << (checksumCount - 1)))
                .mapToObj(String::valueOf)
                .collect(Collectors.joining());

    }

    // Recursively build a final checksum.
    private int buildChecksumFragment(String state, int index, int stride) {
        if (stride > 0)
            return 1 ^ (buildChecksumFragment(state, index, stride >> 1)
                    ^ buildChecksumFragment(state, index + stride, stride >> 1));
        return 1 & (int)state.charAt(index);
    }
}
