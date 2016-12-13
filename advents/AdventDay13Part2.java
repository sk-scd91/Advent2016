package com.skscd91.advent2016.advents;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * Created by Sean Deneen on 12/13/16.
 * Find the number of locations within 50 steps in the same maze.
 */
public class AdventDay13Part2 extends AdventDay13Part1 {

    private static final int LIMIT = 50;
    private List<BitSet> visited;

    // Stop when depth is above 50, and mark as visited.
    @Override
    protected boolean shouldStopSearch(int x, int y, List<BitSet> path) {
        if (path.stream().mapToInt(BitSet::cardinality).sum() > LIMIT)
            return true;
        setCoordinate(visited, x, y);
        return false;
    }

    @Override
    public String compute(BufferedReader input) {
        visited = new ArrayList<>();

        runSearch();

        int count = visited.stream().mapToInt(BitSet::cardinality).sum();

        return "There are " + count + " locations one can reach.";
    }

}
