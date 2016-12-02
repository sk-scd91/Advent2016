package com.skscd91.advent2016.advents;

import java.io.BufferedReader;

/**
 * Created by Sean Deneen on 12/1/16.
 * A puzzle solution used for Advent of Code 2016.
 */
public interface Advent {

    /**
     * Solve an advent puzzle given a file.
     * @param input The text used for the puzzle.
     * @return The solution to a puzzle.
     */
    String compute(BufferedReader input);

}
