package com.skscd91.advent2016.advents;

/**
 * Created by Sean Deneen on 12/14/16.
 * Part 1, but key stretched 2016 times.
 */
public class AdventDay14Part2 extends AdventDay14Part1 {

    private int lastIndex = -1;
    private String lastHash = null;

    @Override
    protected String generateHashForIndex(int index) {
        if (index == lastIndex)
            return lastHash;
        String hash = super.generateHashForIndex(index);
        for (int i = 0; i < 2016; i++) // Hash an additional 2016 times.
            hash = generateHash(hash);
        lastIndex = index;
        lastHash = hash;
        return hash;
    }
}
