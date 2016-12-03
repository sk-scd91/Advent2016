package com.skscd91.advent2016.advents;

import java.io.BufferedReader;
import java.util.Scanner;

/**
 * Created by Sean Deneen on 12/3/16.
 * Counts the number of possible triangles from data oriented vertically.
 */
public class AdventDay3Part2 extends AdventDay3Part1 {

    private long getVerticalTriangleCount(Scanner scanner) {
        long count = 0L;
        while (scanner.hasNextInt()) {
            int a1 = scanner.nextInt(), a2 = scanner.nextInt(), a3 = scanner.nextInt();
            int b1 = scanner.nextInt(), b2 = scanner.nextInt(), b3 = scanner.nextInt();
            int c1 = scanner.nextInt(), c2 = scanner.nextInt(), c3 = scanner.nextInt();

            if (isTriangle(a1, b1, c1))
                count++;
            if (isTriangle(a2, b2, c2))
                count++;
            if (isTriangle(a3, b3, c3))
                count++;
        }
        return count;
    }

    @Override
    public String compute(BufferedReader input) {
        return "The number of possible triangles is: " + getVerticalTriangleCount(new Scanner(input));
    }

}
