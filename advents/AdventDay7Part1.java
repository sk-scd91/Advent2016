package com.skscd91.advent2016.advents;

import java.io.BufferedReader;
import java.util.regex.*;
import java.util.stream.Stream;

/**
 * Created by Sean Deneen on 12/7/16.
 * Find the IP Addresses that support TLS.
 */
public class AdventDay7Part1 implements Advent {

    private final Pattern ABBA = Pattern.compile("(.)(?!\\1)(.)\\2\\1");
    private final Pattern SEQ = Pattern.compile("(?<bracket>\\[?)(?<seq>[a-z]+)]?");

    private boolean isTLS(String ip) {
        boolean abbaFound = false;
        Matcher seqMatcher = SEQ.matcher(ip);

        while (seqMatcher.find()) {
            if ("[".equals(seqMatcher.group("bracket")) &&
                    ABBA.matcher(seqMatcher.group("seq")).find())
                return false;
            else if (!abbaFound)
                abbaFound = ABBA.matcher(seqMatcher.group("seq")).find();
        }

        return abbaFound;
    }

    private Stream<String> tlsStream(BufferedReader input) {
        return input.lines().filter(this::isTLS);
    }

    @Override
    public String compute(BufferedReader input) {
        long tlsCount = tlsStream(input)
                .count();

        return "The amount of IP adresses supporting TLS is " + tlsCount;
    }

}
