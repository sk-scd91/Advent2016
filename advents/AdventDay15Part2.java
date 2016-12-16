package com.skscd91.advent2016.advents;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Created by Sean Deneen on 12/15/16.
 * Add another disc to the tower with 11 positions, starting at position 0.
 */
public class AdventDay15Part2 extends AdventDay15Part1 {
    @Override
    protected int getTimeGap(Stream<int[]> streamOfDiscs) {
        int[][] discs = streamOfDiscs.toArray(int[][]::new); // Collect as array to measure length.
        int[] lastDisc = new int[] {11, Math.floorMod(11 - (discs.length + 1), 11)}; // Add last disc with 11 positions starting at 0 to tower.
        return super.getTimeGap(Stream.concat(Arrays.stream(discs), Stream.of(lastDisc)));
    }
}
