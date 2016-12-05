package com.skscd91.advent2016.advents;

import com.skscd91.advent2016.utils.IOUtils;

import java.io.BufferedReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Sean Deneen on 12/5/16.
 * Given a prefix, find the 8 char password.
 */
public class AdventDay5Part1 implements Advent {

    private MessageDigest DIGEST;

    {
        try {
            DIGEST = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    protected byte[] md5(String code) {
        DIGEST.reset();
        return DIGEST.digest(code.getBytes());
    }

    // Check if first 5 hex digits are 0s.
    private boolean isPasswordHash(byte[] hash) {
        return hash[0] == 0 && hash[1] == 0 && (hash[2] & 0xf0) == 0;
    }

    @Override
    public String compute(BufferedReader input) {
        String doorID = IOUtils.readLineSilently(input);

        String password = IntStream.rangeClosed(0, Integer.MAX_VALUE)
                .mapToObj(i -> md5(doorID + i))
                .filter(this::isPasswordHash)
                .limit(8L)
                .map(h -> Integer.toString(h[2], 16))
                .collect(Collectors.joining());

        return "The password is: " + password;
    }
}
