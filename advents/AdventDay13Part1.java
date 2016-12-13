package com.skscd91.advent2016.advents;

import java.io.BufferedReader;
import java.util.*;
import java.util.function.BooleanSupplier;

/**
 * Created by Sean Deneen on 12/13/16.
 * Find the shortest path to a coordinate using a BFS.
 */
public class AdventDay13Part1 implements Advent {

    private static final int FAVORITE_NUMBER = 1362;

    private static final int DEST_X = 31;
    private static final int DEST_Y = 39;

    private Deque<BooleanSupplier> searches;
    private int pathLength;

    // Generates a wall based on if the formula below has an odd number of bits.
    private boolean isWall(int x, int y) {
        return Integer.bitCount(x*x + 3*x + 2*x*y + y + y*y + FAVORITE_NUMBER) % 2 == 1;
    }

    private boolean canPassThrough(List<BitSet> maze, int x, int y) {
        if(!getCoordinate(maze, 2 * x + 1, y)) { // If location hasn't been memoized yet.
            setCoordinate(maze, 2 * x + 1, y); // Set as memoized
            boolean isWall = isWall(x, y);
            if (isWall)
                setCoordinate(maze, 2 * x, y);
            return !isWall;
        }
        return !getCoordinate(maze, 2 * x, y);
    }

    protected void setCoordinate(List<BitSet> maze, int x, int y) {
        while (y >= maze.size())
            maze.add(new BitSet(x));
        maze.get(y).set(x);
    }

    protected boolean getCoordinate(List<BitSet> maze, int x, int y) {
        while (y >= maze.size())
            maze.add(new BitSet(x));
        return maze.get(y).get(x);
    }

    // Clones the elements of the path grid that will be modified later.
    private List<BitSet> clonePath(List<BitSet> path, int y) {
        List<BitSet> copy = new ArrayList<>(path);
        copy.set(y, (BitSet)copy.get(y).clone());
        return copy;
    }

    private boolean searchPath(int x, int y, List<BitSet> maze, List<BitSet> path) {
        if (shouldStopSearch(x, y, path))
            return true;

        setCoordinate(path, x, y); // Set current location.
        if (!getCoordinate(path, x + 1, y) && canPassThrough(maze, x + 1, y)) { // If not visited and not wall.
            final List<BitSet> pathCopy = clonePath(path, y);
            searches.add(() -> searchPath(x + 1, y, maze, pathCopy));
        }
        if (!getCoordinate(path, x, y + 1) && canPassThrough(maze, x, y + 1)) {
            final List<BitSet> pathCopy = clonePath(path, y + 1);
            searches.add(() -> searchPath(x, y + 1, maze, pathCopy));
        }
        if (x > 0 && !getCoordinate(path, x - 1, y) && canPassThrough(maze, x - 1, y)) {
            final List<BitSet> pathCopy = clonePath(path, y);
            searches.add(() -> searchPath(x - 1, y, maze, pathCopy));
        }
        if (y > 0 && !getCoordinate(path, x, y - 1) && canPassThrough(maze, x, y - 1)) {
            final List<BitSet> pathCopy = clonePath(path, y - 1);
            searches.add(() -> searchPath(x, y - 1, maze, pathCopy));
        }

        return false;
    }

    protected boolean shouldStopSearch(int x, int y, List<BitSet> path) {
        if (x == DEST_X && y == DEST_Y) {
            pathLength = path.stream().mapToInt(BitSet::cardinality).sum();
            return true;
        }
        return false;
    }

    @Override
    public String compute(BufferedReader input) {
        pathLength = -1;

        runSearch();

        return "It takes at least " + pathLength + " steps to reach the coordinate.";
    }

    protected void runSearch() {
        searches = new ArrayDeque<>();
        searches.add(() -> searchPath(1, 1, new ArrayList<>(), new ArrayList<>()));

        while (!searches.isEmpty() && !searches.removeFirst().getAsBoolean())
            ;
    }
}
