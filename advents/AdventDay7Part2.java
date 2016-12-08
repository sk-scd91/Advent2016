package com.skscd91.advent2016.advents;

import java.io.BufferedReader;
import java.util.regex.*;

/**
 * Created by Sean Deneen on 12/7/16.
 * Find the IP Addresses that support SSL.
 */
public class AdventDay7Part2 implements Advent {

    private final Pattern ABA_BAB = Pattern.compile("([a-z])(?!\\1)([a-z])\\1[a-z]*"
            + "([\\[\\]])(?:.*\\3)?[a-z]*?\\2\\1\\2");

    private boolean isSSL(String ip) {
        return ABA_BAB.matcher(ip).find();
    }

    @Override
    public String compute(BufferedReader input) {

        long count = input.lines()
                .filter(this::isSSL)
                .count();

        return "The amount of IP adresses supporting SSL is " + count;
    }
}
