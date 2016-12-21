package com.skscd91.advent2016.advents;

import java.io.BufferedReader;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by Sean Deneen on 12/21/16.
 * Unscramble a password using the instructions from Part 1.
 */
public class AdventDay21Part2 extends AdventDay21Part1 {

    private static final String SCRAMBLED_PASSWORD = "fbgdceah";

    Stream<CharSequence> permutations(String str) {
        if (str.length() <= 1)
            return Stream.of(str);
        char first = str.charAt(0);
        return permutations(str.substring(1))
                .flatMap(rest -> IntStream.rangeClosed(0, rest.length())
                        .mapToObj(i -> new StringBuilder(rest).insert(i, first)));
    }

    @Override
    public String compute(BufferedReader input) {
        List<String> lines = input.lines().collect(Collectors.toList());
        // Collect to a list because findFirst does not terminate streams with flatMap.
        List<CharSequence> perms = permutations(PASSWORD).collect(Collectors.toList());

        String unscrambled = perms.stream()
                .map(CharSequence::toString)
                .peek(this::setScrambler)
                .filter(ignored -> {
                    lines.forEach(this::execInstruction);
                    return SCRAMBLED_PASSWORD.equals(getScrambledResult());
                })
                .findFirst().orElse("");

        return "The un-scrambled message is: " + unscrambled;
    }
}
