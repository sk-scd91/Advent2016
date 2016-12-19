package com.skscd91.advent2016.advents;

import com.skscd91.advent2016.utils.IOUtils;

import java.io.BufferedReader;
import java.util.BitSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by Sean Deneen on 12/18/16.
 * Find the number of tiles in a 40 tile high room that are not traps, using a 1D cellular automata.
 */
public class AdventDay18Part1 implements Advent {

    private static final char SAFE_TILE = '.';
    private static final char TRAP_TILE = '^';

    private BitSet rule;

    private BitSet getTrapRule() {
        BitSet rule = new BitSet();
        rule.set(0b110); // left and center.
        rule.set(0b011); // center and right.
        rule.set(0b100); // left only.
        rule.set(0b001); // right only.
        return rule; // Rule 90.
    }

    private boolean isTrap(String line, int index) {
        return (index >= 0 && index < line.length() // Out of bounds tiles are treated as safe.
                && line.charAt(index) == TRAP_TILE);
    }

    private String generateNextLine(String lastLine) {
        return IntStream.range(0, lastLine.length())
                .map(i -> IntStream.rangeClosed(i - 1, i + 1)
                        .filter(iCenter -> isTrap(lastLine, iCenter))
                        .reduce(0, (n ,shift) -> n | 1 << (1 - (shift - i))))
                .mapToObj(n -> String.valueOf(rule.get(n) ? TRAP_TILE : SAFE_TILE))
                .collect(Collectors.joining());
    }

    protected long getRoomHeight() {
        return 40L;
    }

    @Override
    public String compute(BufferedReader input) {
        rule = getTrapRule();

        long count = Stream.iterate(IOUtils.readLineSilently(input),
                this::generateNextLine)
                .limit(getRoomHeight())
                .peek(System.out::println)
                .flatMapToInt(String::chars)
                .filter(c -> c == SAFE_TILE)
                .count();

        return "The number of safe tiles in the room is " + count;
    }
}
