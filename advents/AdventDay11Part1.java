package com.skscd91.advent2016.advents;

import java.io.BufferedReader;
import java.util.*;
import java.util.function.BooleanSupplier;
import java.util.regex.*;
import java.util.stream.Stream;

/**
 * Created by Sean Deneen on 12/11/16.
 * Find the minimum number of steps to move all the equipment to the top floor.
 */
public class AdventDay11Part1 implements Advent {

    private final List<String> floors = Arrays.asList("first", "second", "third", "fourth");

    private final Pattern FLOOR_CONTAINS = Pattern.compile("The (?<floor>[a-z]+) floor contains ");
    private final Pattern EQUIP_PATTERN = Pattern.compile("an? (?<element>[a-z]+)(?<type> generator|-compatible microchip)");

    protected final List<Set<String>> initialGenerators = new ArrayList<>(Collections.nCopies(4, null));
    protected final List<Set<String>> initialMicrochips = new ArrayList<>(Collections.nCopies(4, null));
    protected List<String> allElements;
    private Set<BitSet> previousStates;

    private Deque<BooleanSupplier> searches;

    private int minSteps;

    // Step 1: Parse the input.
    private void parseInstruction(String instruction) {
        Matcher matcher = FLOOR_CONTAINS.matcher(instruction);

        if (matcher.find()) {
            int floorNum = floors.indexOf(matcher.group("floor"));
            if (floorNum < 0 || floorNum >= 4)
                return;

            initialGenerators.set(floorNum, new HashSet<>());
            initialMicrochips.set(floorNum, new HashSet<>());

            matcher = EQUIP_PATTERN.matcher(instruction.substring(matcher.end()));
            while (matcher.find()) {
                String element = matcher.group("element");
                if (!allElements.contains(element))
                    allElements.add(element);
                if (" generator".equals(matcher.group("type")))
                    initialGenerators.get(floorNum).add(element);
                else
                    initialMicrochips.get(floorNum).add(element);
            }
        }
    }

    // Step 2: Use bfs to find minimum steps to complete task.
    private boolean searchMin(int steps, int floor, List<Set<String>> generators, List<Set<String>> microchips) {
        if (generators.get(generators.size() - 1).size() == allElements.size()
                && microchips.get(microchips.size() - 1).size() == allElements.size()) {
            minSteps = steps;
            return true;
        }

        if (floor > 0) {
            nextStep(steps + 1, floor, floor - 1, generators, microchips);
        }
        if (floor < generators.size() - 1) {
            nextStep(steps + 1, floor, floor + 1, generators, microchips);
        }

        return false;
    }

    // Skip over states already attempted.
    private boolean notSearched(int floor, List<Set<String>> generators, List<Set<String>> microchips) {
        BitSet marks = new BitSet();
        int len = allElements.size();
        marks.set(floor + 2 * len * generators.size());

        for (int i = 0; i < generators.size(); i++)
            for (String element : generators.get(i))
                marks.set(allElements.indexOf(element) + 2 * len * i);

        for (int i = 0; i < microchips.size(); i++)
            for (String element : microchips.get(i))
                marks.set(allElements.indexOf(element) + 2 * len * i + len);

        return previousStates.add(marks);
    }

    // If there are no generators to fry the chips, or all chips go with the generators.
    private boolean isSafeMove(Set<String> generators, Set<String> microchips) {
        return (generators.isEmpty() || generators.containsAll(microchips));
    }

    private void nextStep(int steps, int floorFrom, int floorTo,
                          List<Set<String>> generators, List<Set<String>> microchips) {
        String[] floorEquipment = Stream.concat(generators.get(floorFrom).stream(),
                    microchips.get(floorFrom).stream())
                .toArray(String[]::new);
        int genSize = generators.get(floorFrom).size();

        for (int i = 0; i < floorEquipment.length; i++) {
            // Move the first item.
            final List<Set<String>> generatorsWithFirst = cloneFloors(floorFrom, floorTo,
                    (i < genSize) ? floorEquipment[i] : null, generators);
            final List<Set<String>> microchipsWithFirst = cloneFloors(floorFrom, floorTo,
                    (i < genSize) ? null : floorEquipment[i], microchips);
            // If the move is safe, add to possible moves.
            if (isSafeMove(generatorsWithFirst.get(floorFrom), microchipsWithFirst.get(floorFrom))
                    && isSafeMove(generatorsWithFirst.get(floorTo), microchipsWithFirst.get(floorTo))
                    && notSearched(floorTo, generatorsWithFirst, microchipsWithFirst))
                searches.add(() -> searchMin(steps, floorTo, generatorsWithFirst, microchipsWithFirst));
            for (int j = i + 1; j < floorEquipment.length; j++) {
                final List<Set<String>> nextGenerators = cloneFloors(floorFrom, floorTo,
                        (j < genSize) ? floorEquipment[j] : null, generatorsWithFirst);
                final List<Set<String>> nextMicrochips = cloneFloors(floorFrom, floorTo,
                        (j < genSize) ? null : floorEquipment[j], microchipsWithFirst);
                if (isSafeMove(nextGenerators.get(floorFrom), nextMicrochips.get(floorFrom))
                        && isSafeMove(nextGenerators.get(floorTo), nextMicrochips.get(floorTo))
                        && notSearched(floorTo, nextGenerators, nextMicrochips))
                    searches.add(() -> searchMin(steps, floorTo, nextGenerators, nextMicrochips));
            }
        }
    }

    private List<Set<String>> cloneFloors(int floorFrom, int floorTo,
                                          String toMove, List<Set<String>> toCopy) {
        List<Set<String>> clone = toCopy;
        if (toMove != null) {
            clone = new ArrayList<>(toCopy);
            clone.set(floorFrom, new HashSet<>(toCopy.get(floorFrom)));
            clone.get(floorFrom).remove(toMove);
            clone.set(floorTo, new HashSet<>(toCopy.get(floorTo)));
            clone.get(floorTo).add(toMove);
        }
        return clone;
    }

    @Override
    public String compute(BufferedReader input) {
        minSteps = -1;
        allElements = new ArrayList<>();

        parseInput(input);

        searches = new ArrayDeque<>();
        searches.add(() -> searchMin(0, 0, initialGenerators, initialMicrochips));
        previousStates = new HashSet<>();
        notSearched(0, initialGenerators, initialMicrochips);

        while (!searches.isEmpty() && !searches.removeFirst().getAsBoolean())
            ;

        return "It takes " + minSteps + " steps.";
    }

    protected void parseInput(BufferedReader input) {
        input.lines().forEachOrdered(this::parseInstruction);
    }
}
