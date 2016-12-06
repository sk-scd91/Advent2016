package com.skscd91.advent2016.advents;

import java.io.BufferedReader;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Sean Deneen on 12/6/16.
 */
public class AdventDay6Part1 implements Advent {
    @Override
    public String compute(BufferedReader input) {
        List<String> lines = input.lines().collect(Collectors.toList());
        int len = lines.get(0).length();

        String message = IntStream.range(0, len)
                .mapToObj(i -> lines.stream().map(s -> s.substring(i, i + 1)))
                .map(stringStream -> stringStream.collect(Collectors.groupingBy(i -> i, Collectors.counting())))
                .map(map -> map.entrySet().stream())
                .map(entryStream -> entryStream.sorted(Map.Entry.<String, Long>comparingByValue().reversed()))
                .map(entryStream -> entryStream.map(Map.Entry::getKey).findFirst().orElse("-"))
                .collect(Collectors.joining());

        return "The message is " + message;
    }
}
