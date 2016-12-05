package com.skscd91.advent2016.advents;

import java.io.BufferedReader;
import java.util.Comparator;
import java.util.Map;
import java.util.regex.*;
import java.util.stream.Collectors;

/**
 * Created by Sean Deneen on 12/4/16.
 * Filter out the room names that are not valid, then sum up their sector IDs.
 * Valid rooms have letters separated by dashes, then a number, then a checksum between brackets.
 * The checksum is all the unique letters arranged by frequency, then alphabetical order.
 */
public class AdventDay4Part1 implements Advent {

    private final Pattern ROOM_PATTERN = Pattern.compile("(?<name>(?:[a-z]+-)+)(?<id>\\d+)\\[(?<checksum>[a-z]+)]");

    private boolean isRealRoom(Matcher roomMatcher) {

        Map<Character, Long> letterFrequencies = roomMatcher.group("name").chars()
                .filter(c -> c != '-')
                .mapToObj(i -> (char)i)
                .collect(Collectors.groupingBy(i -> i, Collectors.counting()));

        String compChecksum = letterFrequencies.entrySet().stream()
                .sorted(Map.Entry.<Character, Long>comparingByValue().reversed()
                        .thenComparing(Map.Entry::getKey))
                .map(e -> e.getKey().toString())
                .limit(5L)
                .collect(Collectors.joining());

        return roomMatcher.group("checksum").equals(compChecksum);
    }

    @Override
    public String compute(BufferedReader input) {
        long sum = input.lines()
                .map(ROOM_PATTERN::matcher)
                .filter(Matcher::matches)
                .filter(this::isRealRoom)
                .mapToLong(matcher -> Long.parseLong(matcher.group("id")))
                .sum();

        return "The sum of the sector IDs is: " + sum;
    }
}
