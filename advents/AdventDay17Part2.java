package com.skscd91.advent2016.advents;

import java.io.BufferedReader;

/**
 * Created by Sean Deneen on 12/17/16.
 * Find the longest path length that reaches the vault.
 */
public class AdventDay17Part2 extends AdventDay17Part1 {
    @Override
    public String compute(BufferedReader input) {
        startSearch();

        while (continueSearch())
            ;

        return "The longest path length is " + minPath.length();
    }

    @Override
    protected boolean continueSearch() {
        if (!searches.isEmpty()) {
            searches.removeFirst().getAsBoolean();
            return true;
        }
        return false;
    }
}
