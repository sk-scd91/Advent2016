package com.skscd91.advent2016.advents;

import java.io.BufferedReader;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by Sean Deneen on 12/14/16.
 */
public class AdventDay14Part1 implements Advent {

    private static final String SALT = "zpqevtbw";

    private final Pattern IS_POTENTIAL_KEY = Pattern.compile("([\\p{XDigit}])\\1\\1");
    private final Pattern IS_KEY_QUALIFIER = Pattern.compile("([\\p{XDigit}])\\1{4}");

    private MessageDigest md5Digest;
    private List<List<Integer>> potentialKeyIndices;

    private String generateHash(int index) {
        md5Digest.reset();
        String digestable = SALT + index;
        byte[] hash = md5Digest.digest(digestable.getBytes());
        String result =  new java.math.BigInteger(1, hash).toString(16); // Generate hex representation.
        // Left pad with 0.
        int padLength = (hash.length * 2) - result.length();
        char[] padding = new char[padLength];
        Arrays.fill(padding, '0');
        return new String(padding) + result;
    }

    private boolean storePotentialKeys(int index) {
        String hash = generateHash(index);
        Matcher potentialKeyMatcher = IS_POTENTIAL_KEY.matcher(hash);
        if (potentialKeyMatcher.find()) {
            int tripletHex = Integer.parseInt(potentialKeyMatcher.group(1),16);
            potentialKeyIndices.get(tripletHex).add(index);
            return true;
        }
        return false;
    }

    private Stream<Integer> getIndicesOfQualifiedKeys(int index) {
        return Stream.of(generateHash(index))
                .map(IS_KEY_QUALIFIER::matcher)
                .filter(Matcher::find)
                .flatMap(matcher -> {
                    int quintHex = Integer.parseInt(matcher.group(1), 16);
                    int indexLimit = index - 1000;
                    potentialKeyIndices.get(quintHex).removeIf(i -> i < indexLimit);
                    return potentialKeyIndices.get(quintHex).stream();
                })
                .filter(i -> i != index);
    }

    @Override
    public String compute(BufferedReader input) {
        try {
            md5Digest = MessageDigest.getInstance("MD5");
        } catch (java.security.NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        potentialKeyIndices = Stream.generate(ArrayList<Integer>::new)
                .limit(16L)
                .collect(Collectors.toList());

        int keyIndex = IntStream.rangeClosed(0, Integer.MAX_VALUE)
                .filter(this::storePotentialKeys)
                .boxed()
                .flatMap(this::getIndicesOfQualifiedKeys)
                .skip(64L - 1L)
                .findFirst().orElse(-1);

        return "The index of the 64th key is: " + keyIndex;
    }
}
