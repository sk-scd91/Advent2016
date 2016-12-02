package com.skscd91.advent2016.advents;

import com.skscd91.advent2016.utils.IOUtils;

import java.io.BufferedReader;
import java.util.regex.*;

/**
 * Created by Sean Deneen on 12/1/16.
 * Find the distance traveled to get to the HQ.
 */
@SuppressWarnings("unused")
public class AdventDay1Part1 implements Advent {

    protected final Pattern DIR_PATTERN = Pattern.compile("(?<dir>L|R)(?<num>\\d+)");

    @Override
    public String compute(BufferedReader input) {
        int x = 0;
        int y = 0;
        int xDir = 0;
        int yDir = 1;
        String directions = IOUtils.readLineSilently(input);
        Matcher matcher = DIR_PATTERN.matcher(directions);

        while (matcher.find()) {
            int dir = "L".equals(matcher.group("dir")) ? -1 : 1;
            int steps = Integer.parseInt(matcher.group("num"));
            if (xDir == 0) {
                xDir = yDir * dir;
                yDir = 0;
            } else {
                yDir = -xDir * dir;
                xDir = 0;
            }
            x += xDir * steps;
            y += yDir * steps;
        }

        int distance = Math.abs(x) + Math.abs(y); // Manhattan Distance.
        return "The distance is " + distance + " blocks.";
    }
}
