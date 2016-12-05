package com.skscd91.advent2016.advents;

import com.skscd91.advent2016.utils.IOUtils;

import java.io.BufferedReader;

/**
 * Created by Sean Deneen on 12/5/16.
 * Find the password using the 6th digit as its position, and 7th as character.
 */
public class AdventDay5Part2 extends AdventDay5Part1 {

    @Override
    public String compute(BufferedReader input) {
        String doorID = IOUtils.readLineSilently(input);

        StringBuilder password = new StringBuilder("________");
        System.out.println(password);

        interestingHashes(doorID)
                .filter(h -> h[2] < 8 && password.charAt(h[2]) == '_') // Make sure index is in bounds and not set.
                .map(h -> password.replace(h[2], h[2] + 1, Integer.toHexString((h[3] & 0xff) >> 4)))
                .peek(System.out::println)
                .anyMatch(pass -> pass.indexOf("_") < 0);


        return "The password was found.";
    }

}
