package com.skscd91.advent2016.advents;

import java.io.BufferedReader;
import java.util.regex.Matcher;

/**
 * Created by Sean Deneen on 12/4/16.
 * Decrypt the room names, then find the sector ID for the North Pole objects.
 */
public class AdventDay4Part2 extends AdventDay4Part1 {

    private boolean isNorthPoleSector(Matcher roomMatcher) {
        final int shift = Integer.parseInt(roomMatcher.group("id"));

        String decrypted = roomMatcher.group("name").chars()
                .map(c -> (c == '-') ? ' ' : ((c + shift - 'a') % 26) + 'a')
                .collect(StringBuilder::new,
                        StringBuilder::appendCodePoint,
                        StringBuilder::append)
                .toString();

        return decrypted.contains("northpole");
    }

    @Override
    public String compute(BufferedReader input) {

        String sectorID = getRoomStream(input)
                .filter(this::isNorthPoleSector)
                .map(matcher -> matcher.group("id"))
                .findFirst().orElse("");

        return "The sector ID for North Pole objects is: " + sectorID;
    }

}
