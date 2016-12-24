package com.skscd91.advent2016.advents;

import java.io.BufferedReader;
import java.util.*;
import java.util.function.BooleanSupplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by Sean Deneen on 12/24/16.
 * Find the shortest path to all the points of interest in the maze.
 */
public class AdventDay24Part1 implements Advent {

    private String[] maze;
    private Deque<BooleanSupplier> searches;
    private Map<Integer, int[]> paths;
    private BitSet points;

    private int[][] distances;

    private boolean nextStep(int steps, int origin, int x, int y) {
        int count = distances.length;
        int[] closest = paths.computeIfAbsent(x * maze.length + y, k -> new int[count]);
        closest[origin] = steps + 1; // Add 1 so that zero means not visited yet.

        for (int i = 0; i < count; i++) {
            int tile = i;
            if (closest[tile] == 0 || !points.get(origin * count + tile))
                continue; // Skip if the tile has not been visited or the minimum distance is already reached.
            System.out.println("Found: " + origin + "->" + tile); // Display result in case search is slow.
            // When two paths overlap, add the distance to the other tile with the distance from origin.
            distances[origin][tile] = distances[tile][origin] = steps + closest[tile] - 1;
            points.clear(origin * count + tile); // Mark origin->tile as reached.
            points.clear(tile * count + origin);
            if (points.isEmpty()) { // All points have been visited.
                return true;
            }
        }

        for (int i = 0; i < 4; i++) { // Up, Left, Down, Right.
            int nextX = x + ((i - 2) % 2);
            int nextY = y + ((i - 1) % 2);
            if (maze[nextY].charAt(nextX) != '#') { // If not wall or already visited.
                int[] nextPath = paths.computeIfAbsent(nextX * maze.length + nextY, k -> new int[count]);
                if (nextPath[origin] == 0)
                    searches.add(() -> nextStep(steps + 1, origin, nextX, nextY));
            }
        }

        return false;
    }

    // Find the coordinate where character c is located.
    private int[] findCoordinateFor(char c) {
        int x, y;
        for(y = 1; y < maze.length - 1; y++) {
            x = maze[y].indexOf(c);
            if (x >= 0)
                return new int[]{x, y};
        }
        return null;
    }

    @Override
    public String compute(BufferedReader input) {
        maze = input.lines().toArray(String[]::new);

        // Extract point of interest count.
        int count = (int)Arrays.stream(maze)
                .flatMapToInt(String::chars)
                .filter(Character::isDigit)
                .count();

        distances = new int[count][count];

        int[][] coords = IntStream.range(0, count)
                .mapToObj(i -> findCoordinateFor((char)(i + '0')))
                .toArray(int[][]::new);
        performBfs(coords);

        System.out.println("Performing sums...");

        // Use brute force Traveling Salesman solution (7! is a trivial amount of iterations).
        long minSteps = intPermutations(count)
                .filter(l -> l.get(0) == 0) // Always start at 0.
                .mapToLong(l -> IntStream.range(0, l.size() - 1)
                        .mapToLong(i -> distances[l.get(i)][l.get(i + 1)])
                        .sum())
                .min().orElse(-1);

        return "The minimum number of steps to visit all the points is " + minSteps;
    }

    // Perform BFS for multiple coordinates at the same time.
    // The search is much faster when computing the distance where two paths overlap than from A to B.
    private void performBfs(int[][] origins) {
        System.out.println("Performing search...");
        searches = new ArrayDeque<>();
        paths = new HashMap<>();
        points = new BitSet();
        points.set(0, origins.length * origins.length);
        for (int i = 0; i < origins.length; i++) {
            int origin = i;
            int startX = origins[i][0];
            int startY = origins[i][1];
            searches.add(() -> nextStep(0, origin, startX, startY));
        }

        while (!searches.isEmpty() && !searches.removeFirst().getAsBoolean())
            ;

        searches = null;
        points = null;
        paths = null;
    }

    protected Stream<List<Integer>> intPermutations(int count) {
        Stream<List<Integer>> result = Stream.of(Arrays.asList(count - 1));

        for(int i = count - 2; i >= 0; i--) {
            final int finalI = i;
            result = result.flatMap(l -> IntStream.rangeClosed(0, l.size())
                    .mapToObj(j -> {
                        List<Integer> permutation = new ArrayList<Integer>(l);
                        permutation.add(j, finalI);
                        return permutation;
                    }));
        }
        return result;
    }
}
