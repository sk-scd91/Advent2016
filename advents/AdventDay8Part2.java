package com.skscd91.advent2016.advents;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by Sean Deneen on 12/8/16.
 */
public class AdventDay8Part2 extends AdventDay8Part1 {

    @Override
    public String compute(BufferedReader input) {
        super.compute(input);

        String screenDisplay = Arrays.stream(screen)
                .mapToObj(line -> Long.toBinaryString(Long.reverse((line << 1) | 1L)))
                .map(bString -> bString.substring(1).replace('0', ' ').replace('1', 'x'))
                .map(pString -> String.join(" ", pString.split("(?<=\\G.{5})")))
                .collect(Collectors.joining("\n"));

        return "The display: \n" + screenDisplay;
    }

}
