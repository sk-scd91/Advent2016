package com.skscd91.advent2016.advents;

import java.io.BufferedReader;
import java.util.Scanner;

/**
 * Created by Sean Deneen on 12/3/16.
 * Counts the number of possible triangles.
 */
public class AdventDay3Part1 implements Advent {

    protected boolean isTriangle(int a, int b, int c) {
        return (a + b > c)
                && (a + c > b)
                && (b + c > a);
    }

    @Override
    public String compute(BufferedReader input) {
        long triangleCount = input.lines()
                .map(Scanner::new)
                .filter(scanner -> isTriangle(scanner.nextInt(),
                        scanner.nextInt(),
                        scanner.nextInt()))
                .count();

        return "The number of possible triangles is: " + triangleCount;
    }
}
