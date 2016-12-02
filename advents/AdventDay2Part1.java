package com.skscd91.advent2016.advents;

import java.io.BufferedReader;

/**
 * Created by Sean Deneen on 12/2/16.
 * Find the bathroom code given the instructions.
 */
public class AdventDay2Part1 implements Advent {

    private int keypadWidth;
    private int keypadPosition;

    private void nextKey(int direction) {
        switch (direction) {
            case 'U':
                if (keypadPosition >= keypadWidth)
                    keypadPosition -= keypadWidth;
                break;
            case 'D':
                if (keypadPosition < keypadWidth * (keypadWidth - 1))
                    keypadPosition += keypadWidth;
                break;
            case 'L':
                if (keypadPosition % keypadWidth > 0)
                    keypadPosition--;
                break;
            case 'R':
                if (keypadPosition % keypadWidth < keypadWidth - 1)
                    keypadPosition++;
                break;
        }
    }

    @Override
    public String compute(BufferedReader input) {
        keypadWidth = 3;
        keypadPosition = (keypadWidth * keypadWidth) / 2;

        String keycode = input.lines()
                .peek(directions -> directions.chars().forEachOrdered(this::nextKey))
                .mapToInt(ignored -> keypadPosition + 1)
                .collect(StringBuilder::new,
                        StringBuilder::append,
                        StringBuilder::append)
                .toString();

        return "The bathroom code is: " + keycode;
    }
}
