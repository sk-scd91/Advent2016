package com.skscd91.advent2016.advents;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Sean Deneen on 12/11/16.
 * Like Part 1, but with additional material.
 */
public class AdventDay11Part2 extends AdventDay11Part1 {

    @Override
    protected void parseInput(BufferedReader input) {
        super.parseInput(input);

        List<String> newElements = Arrays.asList("elerium", "dilithium");
        allElements.addAll(newElements);
        initialGenerators.get(0).addAll(newElements);
        initialMicrochips.get(0).addAll(newElements);
    }

}
