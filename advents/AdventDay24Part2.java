package com.skscd91.advent2016.advents;

import java.util.List;
import java.util.stream.Stream;

/**
 * Created by Sean Deneen on 12/24/16.
 * Find the shortest circuit around all the points of interest.
 */
public class AdventDay24Part2 extends AdventDay24Part1 {

    @Override
    protected Stream<List<Integer>> intPermutations(int count) {
        return super.intPermutations(count)
                .peek(l -> l.add(l.get(0))); // Wrap around.
    }
}
