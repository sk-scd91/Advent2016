package com.skscd91.advent2016.advents;

import java.io.BufferedReader;
import java.util.regex.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by Sean Deneen on 12/15/16.
 * Find the first time one can press the button to get the ball through all the slots.
 */
public class AdventDay15Part1 implements Advent {

    private final Pattern ARRANGEMENT = Pattern.compile("Disc #(?<discNum>\\d+) has (?<posCount>\\d+) positions?;"
            + " at time=0, it is at position (?<posStart>\\d+)\\.");

    private static final int PAIR_COUNT = 2;
    private static final int POS_COUNT = 0;
    private static final int POS_GAP = 1;

    private int[] parseArrangement(String arrangement) {
        int[] resultPair = new int[PAIR_COUNT];
        Matcher matcher = ARRANGEMENT.matcher(arrangement);
        if (matcher.matches()) {
            resultPair[POS_COUNT] = Integer.parseInt(matcher.group("posCount"));
            // Distance between first position and goal.
            resultPair[POS_GAP] = resultPair[POS_COUNT] - Integer.parseInt(matcher.group("posStart"));
            // Adjust for position in the disc stack.
            resultPair[POS_GAP] -= Integer.parseInt(matcher.group("discNum"));
            resultPair[POS_GAP] = Math.floorMod(resultPair[POS_GAP], resultPair[POS_COUNT]);
        }
        return resultPair;
    }

    private int[] combineDiscs(int[] discTower, int[] nextDisc) {
        IntStream.range(0, nextDisc[POS_COUNT])
                .filter(x -> (discTower[POS_COUNT] * x + discTower[POS_GAP])
                        % nextDisc[POS_COUNT] == nextDisc[POS_GAP])
                .map(multiplier -> discTower[POS_COUNT] * multiplier)
                .findFirst()
                .ifPresent(increment -> {
                    if (discTower[POS_COUNT] % nextDisc[POS_COUNT] != 0)
                        discTower[POS_COUNT] *= nextDisc[POS_COUNT];
                    else if (discTower[POS_COUNT] < nextDisc[POS_COUNT])
                        discTower[POS_COUNT] = nextDisc[POS_COUNT];
                    discTower[POS_GAP] += increment;
                });
        return discTower;
    }

    protected int getTimeGap(Stream<int[]> streamOfDiscs) {
        return streamOfDiscs.reduce(new int[]{1, 0}, this::combineDiscs)[POS_GAP];
    }

    @Override
    public String compute(BufferedReader input) {
        int timeGap = getTimeGap(input.lines().map(this::parseArrangement));

        return "The best time to launch is " + timeGap + " seconds.";
    }
}
