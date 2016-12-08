package com.skscd91.advent2016.advents;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.regex.*;

/**
 * Created by Sean Deneen on 12/8/16.
 * Count the pixels on the display after following the commands
 */
public class AdventDay8Part1 implements Advent {

    private final Pattern RECT_PATTERN = Pattern.compile("rect (?<width>\\d+)x(?<height>\\d+)");
    private final Pattern ROTATE_PATTERN = Pattern.compile("rotate (?<type>row y|column x)="
            + "(?<line>\\d+) by (?<shift>\\d+)");

    private static final long WIDTH = 50L;
    private static final int HEIGHT = 6;
    private static final long COLUMN_MASK = (1L << WIDTH) - 1;

    private final long[] screen = new long[HEIGHT];

    private boolean fillRect(String instruction) {
        Matcher rectMatcher = RECT_PATTERN.matcher(instruction);

        if (rectMatcher.matches()) {
            long rectWidth = Math.min(WIDTH, Long.parseLong(rectMatcher.group("width")));
            int rectHeight = Math.min(HEIGHT, Integer.parseInt(rectMatcher.group("height")));
            long fillMask = (1L << rectWidth) - 1;

            for (int i = 0; i < rectHeight; i++)
                screen[i] |= fillMask;

            return true;
        }

        return false;
    }

    private void rotateRect(String instruction) {
        Matcher rotateMatcher = ROTATE_PATTERN.matcher(instruction);

        if (rotateMatcher.matches()) {
            if ("row y".equals(rotateMatcher.group("type"))) {
                int row = Integer.parseInt(rotateMatcher.group("line")) % HEIGHT;
                int shift = Integer.parseInt(rotateMatcher.group("shift")) % (int)WIDTH;
                screen[row] = ((screen[row] << shift) | (screen[row] >> (WIDTH - shift))) & COLUMN_MASK;
            } else {
                long column = Long.parseLong(rotateMatcher.group("line")) % WIDTH;
                int shift = Integer.parseInt(rotateMatcher.group("shift")) % HEIGHT;
                int bits = 0;
                for (int i = 0; i < HEIGHT; i++)
                    if ((screen[Math.floorMod(i - shift, HEIGHT)] & (1L << column)) != 0L)
                        bits |= 1 << i;
                for (int i = 0; i < HEIGHT; i++) {
                    long rowBit = ((bits & (1 << i)) == 0) ? 0 : 1L << column;
                    screen[i] = (screen[i] & ~(1L << column)) | rowBit;
                }
            }
        }
    }

    @Override
    public String compute(BufferedReader input) {
        Arrays.fill(screen, 0L);

        input.lines().filter(instruction -> !fillRect(instruction)).forEach(this::rotateRect);

        int bitCount = Arrays.stream(screen).mapToInt(Long::bitCount).sum();

        return "The number of pixels lit are: " + bitCount;
    }
}
