package com.skscd91.advent2016.advents;

import com.skscd91.advent2016.utils.IOUtils;

import java.io.BufferedReader;
import java.util.regex.*;

/**
 * Created by Sean Deneen on 12/1/16.
 * Find the distance traveled to get to the HQ.
 */
public class AdventDay1Part1 implements Advent {

    private final Pattern DIR_PATTERN = Pattern.compile("(?<dir>L|R)(?<num>\\d+)");

    protected int x;
    protected int y;

    @Override
    public String compute(BufferedReader input) {
        String directions = IOUtils.readLineSilently(input);
        travel(directions);

        int distance = Math.abs(x) + Math.abs(y); // Manhattan Distance.
        return "The distance is " + distance + " blocks.";
    }

    private final void travel(String directions) {
        x = 0;
        y = 0;
        int xDir = 0;
        int yDir = 1;
        Matcher matcher = DIR_PATTERN.matcher(directions);

        while (canContinue(matcher)) {
            int dir = "L".equals(matcher.group("dir")) ? -1 : 1;
            int steps = Integer.parseInt(matcher.group("num"));
            if (xDir == 0) {
                xDir = yDir * dir;
                yDir = 0;
            } else {
                yDir = -xDir * dir;
                xDir = 0;
            }
            updateCoordinates(xDir, yDir, steps);
        }
    }

    protected boolean canContinue(Matcher matcher) {
        return matcher.find();
    }

    protected void updateCoordinates(int dx, int dy, int steps) {
        x += dx * steps;
        y += dy * steps;
    }

}
