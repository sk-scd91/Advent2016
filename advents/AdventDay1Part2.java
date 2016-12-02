package com.skscd91.advent2016.advents;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;

/**
 * Created by pikacd on 12/1/16.
 * Find the distance of the first coordinate that is crossed over twice.
 */
public class AdventDay1Part2 extends AdventDay1Part1 {

    private Set<String> coordinates = new HashSet<>(Collections.singletonList("0,0"));
    private boolean hasOverlapped = false;

    @Override
    protected boolean canContinue(Matcher matcher) {
        return !hasOverlapped && matcher.find();
    }

    @Override
    protected void updateCoordinates(int dx, int dy, int steps) {
        for (int i = 0; i < steps; i++) {
            x += dx;
            y += dy;
            if (!coordinates.add(x + "," + y)) { // If we have overlapped.
                hasOverlapped = true;
                return; // x and y will be at the first point to overlap.
            }
        }
    }
}
