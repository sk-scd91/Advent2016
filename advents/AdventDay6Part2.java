package com.skscd91.advent2016.advents;

import java.util.Map;
import java.util.stream.Stream;

/**
 * Created by Sean Deneen on 12/6/16.
 * Find the message using least frequent repetition code.
 */
public class AdventDay6Part2 extends AdventDay6Part1 {

    @Override
    protected Stream<Map.Entry<String, Long>> sortFrequencies(Stream<Map.Entry<String, Long>> frequencyStream) {
        return frequencyStream.sorted(Map.Entry.<String, Long>comparingByValue());
    }

}
