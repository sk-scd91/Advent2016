package com.skscd91.advent2016.advents;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.BooleanSupplier;

/**
 * Created by Sean Deneen on 12/11/16.
 * Like Part 1, but with additional material.
 * Uses A* with an element distance based heuristic instead of BFS.
 */
public class AdventDay11Part2 extends AdventDay11Part1 {

    private PriorityQueue<SearchNode> searches;

    @Override
    protected void parseInput(BufferedReader input) {
        super.parseInput(input);

        List<String> newElements = Arrays.asList("elerium", "dilithium");
        allElements.addAll(newElements);
        initialGenerators.get(0).addAll(newElements);
        initialMicrochips.get(0).addAll(newElements);
    }

    @Override
    protected void initSearches() {
        searches = new PriorityQueue<>();
    }

    @Override
    protected void addSearch(int steps, int floor, List<Set<String>> generators, List<Set<String>> microchips) {
        int size = generators.size();
        int distance = steps * (size / 2); // g(n).

        // h(n) = sigma([1, n], 2 * (1 - n/size) * (|generators| + |microchips|))
        for (int i = 0; i < size - 1; i++)
            distance += (size - i - 1) * (generators.get(i).size() + microchips.get(i).size());

        searches.add(new SearchNode(distance, () -> searchMin(steps, floor, generators, microchips)));
    }

    @Override
    protected boolean continueSearch() {
        return (!searches.isEmpty() && !searches.poll().getAsBoolean());
    }

    private class SearchNode implements BooleanSupplier, Comparable<SearchNode> {

        private int distance;
        private BooleanSupplier composite;

        public SearchNode(int distance, BooleanSupplier composite) {
            this.distance = distance;
            this.composite = composite;
        }

        @Override
        public int compareTo(SearchNode o) {
            return Integer.compare(distance, o.distance);
        }

        @Override
        public boolean getAsBoolean() {
            return composite.getAsBoolean();
        }
    }

}
